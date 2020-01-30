//
//  F1sBLEManager.swift
//  F1s-SDK-iOS
//
//  Created by Goran Blažič on 24/01/2020.
//  Copyright © 2020 LELO. All rights reserved.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

import Foundation
import CoreBluetooth

final class F1sBluetoothManager: NSObject {

	let lastConnectedKey = "lastConnectedF1s"
	let maxLastSeenInterval = 5.0
	let purgeInterval = 1.0

	static let shared = F1sBluetoothManager()

	var connected: Bool {
		return connectedPeripheral != nil
	}

	var lastConnectedF1sID: String? {
		get {
			return UserDefaults.standard.string(forKey: lastConnectedKey)
		}
		set {
			UserDefaults.standard.set(newValue, forKey: lastConnectedKey)
		}
	}

	private (set) var centralManager: CBCentralManager!
	private var connectedPeripheral: CBPeripheral? = nil

	private (set) var scannedItems: [F1sBluetoothDevice] = []

	var lastConnectedF1sFound: F1sBluetoothDevice? {
		return scannedItems.first(where: { $0.lastConnected })
	}

	private override init() {
		super.init()
		centralManager = CBCentralManager(delegate: self, queue: nil)
	}

	@discardableResult func startScanning() -> Bool {
		guard centralManager.state == .poweredOn, !centralManager.isScanning, !connected else {
			return false
		}

//		scannedItems.removeAll()

		// NOTE: We know the F1s advertises a service with UUID 0xfff0
		centralManager.scanForPeripherals(withServices: [CBUUID(string: "0xfff0")], options: [CBCentralManagerScanOptionAllowDuplicatesKey: NSNumber(booleanLiteral: true)])

		let _ = Timer.scheduledTimer(withTimeInterval: purgeInterval, repeats: true) { [weak self] timer in
			guard let self = self, self.centralManager.isScanning, !self.connected else {
				timer.invalidate()
				return
			}
			let now = Date()
			let removeItems = self.scannedItems.filter { now.timeIntervalSince($0.lastSeen) > self.maxLastSeenInterval }
			for item in removeItems {
				if let index = self.scannedItems.firstIndex(of: item) {
					self.scannedItems.remove(at: index)
					NotificationCenter.default.post(name: F1sScannedItem.notificationName, object: F1sScannedItem.removed(item: item))
				}
			}
		}

		return true
	}

	@discardableResult func stopScanning(removeItems: Bool = false) -> Bool {
		guard centralManager.isScanning else {
			return false
		}
		centralManager.stopScan()
		if removeItems {
			scannedItems.removeAll()
			NotificationCenter.default.post(name: F1sScannedItem.notificationName, object: F1sScannedItem.removedAll)
		}
		return true
	}

	@discardableResult func refresh() -> Bool {
		guard !connected, stopScanning() else {
			return false
		}
		return startScanning()
	}

	@discardableResult func connect(to item: F1sBluetoothDevice) -> Bool {
		guard !connected else {
			return false
		}
		centralManager.connect(item.peripheral, options: nil)
		return true
	}

	@discardableResult func disconnect(startScan: Bool = false) -> Bool {
		guard connected, let peripheral = connectedPeripheral else {
			return false
		}
		centralManager.cancelPeripheralConnection(peripheral)
		connectedPeripheral = nil
		return startScan ? startScanning() : true
	}
}

extension F1sBluetoothManager: CBCentralManagerDelegate {

	func centralManagerDidUpdateState(_ central: CBCentralManager) {
		NotificationCenter.default.post(name: F1sBluetoothState.notificationName, object: F1sBluetoothState.from(cbManagerState: central.state))
		switch central.state {
		case .poweredOn:
			startScanning()
			// NOTE: We're not handling any of the other cases in this example,
		// but have listed them below for your convenience.
		case .unknown:
			break
		case .resetting:
			break
		case .unsupported:
			break
		case .unauthorized:
			break
		case .poweredOff:
			break
		@unknown default:
			fatalError()
		}
	}

	func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String : Any], rssi RSSI: NSNumber) {
		guard !connected, let name = peripheral.name, name == "F1s" else {
			return
		}
		let uuid = peripheral.identifier.uuidString

		if let foundItem = scannedItems.first(where: { $0.uuid == uuid }) {
			foundItem.justSeen()
		} else {
			let lastConnected = uuid == lastConnectedF1sID
			let item = F1sBluetoothDevice(name: name, uuid: uuid, peripheral: peripheral, lastConnected: lastConnected)
			scannedItems.append(item)
			NotificationCenter.default.post(name: F1sScannedItem.notificationName, object: F1sScannedItem.added(item: item, lastConnected: lastConnected))
		}
	}

	func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral) {
		connectedPeripheral = peripheral
		lastConnectedF1sID = peripheral.identifier.uuidString
		guard let item = scannedItems.first(where: { $0.peripheral == peripheral }) else {
			// NOTE: Don't use fatalError for production code
			fatalError("There is no item for this peripheral")
		}
		NotificationCenter.default.post(name: F1sConnectionState.notificationName, object: F1sConnectionState.connected(item))

		item.startUsingPeripheral()
	}

	func centralManager(_ central: CBCentralManager, didDisconnectPeripheral peripheral: CBPeripheral, error: Error?) {
		guard let item = scannedItems.first(where: { $0.peripheral == peripheral }) else {
			// NOTE: Don't use fatalError for production code
			fatalError("There is no item for this peripheral")
		}
		NotificationCenter.default.post(name: F1sConnectionState.notificationName, object: F1sConnectionState.error(item, error))
		// TODO: We could check for conditions where we'd like to reconnect to the device!!!
		connectedPeripheral = nil
	}

	func centralManager(_ central: CBCentralManager, didFailToConnect peripheral: CBPeripheral, error: Error?) {
		NotificationCenter.default.post(name: F1sConnectionState.notificationName, object: F1sConnectionState.disconnected)
	}
}
