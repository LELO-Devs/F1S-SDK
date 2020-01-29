//
//  F1sMonitor.swift
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

final class F1sMonitor {

	var silence: Bool = false

	var bluetoothStatus: CBManagerState {
		return F1sBluetoothManager.shared.centralManager.state
	}

	var onBluetoothStatusChange: ((CBManagerState) -> Void)? = nil

	var onF1sItemFound: ((F1sBluetoothDevice, Bool) -> Bool)? = nil

	var onF1sItemRemoved: ((F1sBluetoothDevice) -> Void)? = nil

	var onAllF1sRemoved: (() -> Void)? = nil

	var onF1sConnected: ((F1sBluetoothDevice) -> Void)? = nil

	var onF1sDisconnected: ((F1sBluetoothDevice) -> Void)? = nil

	var onF1sConnectFailed: ((F1sBluetoothDevice, Error?) -> Void)? = nil

	init() {
		NotificationCenter.default.addObserver(forName: F1sBluetoothState.notificationName, object: nil, queue: nil) { notification in
			guard !self.silence else {
				return
			}
			if let object = notification.object as? F1sBluetoothState {
				self.onBluetoothStatusChange?(object.cbManagerState)
			}
		}

		NotificationCenter.default.addObserver(forName: F1sScannedItem.notificationName, object: nil, queue: nil) { notification in
			guard !self.silence else {
				return
			}
			if let object = notification.object as? F1sScannedItem {
				switch object {
				case .added(let item, let lastConnected):
					if self.onF1sItemFound?(item, lastConnected) ?? false {
						F1sBluetoothManager.shared.connect(to: item)
					}
				case .removed(let item):
					self.onF1sItemRemoved?(item)
				case .removedAll:
					self.onAllF1sRemoved?()
				}
			}
		}

		NotificationCenter.default.addObserver(forName: F1sConnectionState.notificationName, object: nil, queue: nil) { notification in
			guard !self.silence else {
				return
			}
			if let object = notification.object as? F1sConnectionState {
				switch object {
				case .connected(let item):
					self.onF1sConnected?(item)
				case .disconnected(let item):
					self.onF1sDisconnected?(item)
				case .error(let item, let error):
					self.onF1sConnectFailed?(item, error)
				}
			}
		}
	}
}
