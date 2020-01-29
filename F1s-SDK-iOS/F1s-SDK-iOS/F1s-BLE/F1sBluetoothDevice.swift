//
//  F1sBLEDevice.swift
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

#if DEBUG
func debug(_ string: String) {
	print(string)
}
#else
func debug(_ string: String) { }
#endif

final class F1sBluetoothDevice: NSObject {

	// NOTE: This could be done using a generic typealias, but to improve
	// readibility we're leaving it as is.
	typealias BoolUpdatedHandler = ((Bool) -> Void)
	typealias UInt8UpdatedHandler = ((UInt8) -> Void)
	typealias UInt16UpdatedHandler = ((UInt16) -> Void)
	typealias StringUpdatedHandler = ((String) -> Void)
	typealias DoubleUpdatedHandler = ((Double) -> Void)
	typealias MotorSpeedUpdatedHandler = ((F1sMotorSpeed) -> Void)
	typealias UInt8ArrayUpdatedHandler = (([UInt8]) -> Void)
	typealias AccelerationUpdatedHandler = ((F1sAcceleration) -> Void)
	typealias EnvironmentUpdatedHandler = ((F1sEnvironment) -> Void)
	typealias ButtonsUpdatedHandler = ((F1sButtons) -> Void)

	var onManufacturerNameUpdated: StringUpdatedHandler?
	var manufacturerName: String? {
		didSet {
			guard let manufacturerName = manufacturerName else {
				return
			}
			onManufacturerNameUpdated?(manufacturerName)
		}
	}

	var onModelNumberUpdated: StringUpdatedHandler?
	var modelNumber: String? {
		didSet {
			guard let modelNumber = modelNumber else {
				return
			}
			onManufacturerNameUpdated?(modelNumber)
		}
	}

	var onHardwareRevisionUpdated: StringUpdatedHandler?
	var hardwareRevision: String? {
		didSet {
			guard let hardwareRevision = hardwareRevision else {
				return
			}
			onHardwareRevisionUpdated?(hardwareRevision)
		}
	}

	var onFirmwareRevisionUpdated: StringUpdatedHandler?
	var firmwareRevision: String? {
		didSet {
			guard let firmwareRevision = firmwareRevision else {
				return
			}
			onFirmwareRevisionUpdated?(firmwareRevision)
		}
	}

	var onSoftwareRevisionUpdated: StringUpdatedHandler?
	var softwareRevision: String? {
		didSet {
			guard let softwareRevision = softwareRevision else {
				return
			}
			onSoftwareRevisionUpdated?(softwareRevision)
		}
	}

	var onMacAddressUpdated: StringUpdatedHandler?
	var macAddress: String? {
		didSet {
			guard let macAddress = macAddress else {
				return
			}
			onMacAddressUpdated?(macAddress)
		}
	}

	var onSerialNumberUpdated: StringUpdatedHandler?
	var serialNumber: String? {
		didSet {
			guard let serialNumber = serialNumber else {
				return
			}
			onSerialNumberUpdated?(serialNumber)
		}
	}

	var onBatteryLevelUpdated: UInt8UpdatedHandler?
	var batteryLevel: UInt8? {
		didSet {
			guard let batteryLevel = batteryLevel else {
				return
			}
			onBatteryLevelUpdated?(batteryLevel)
		}
	}

	var onMotorSpeedUpdated: MotorSpeedUpdatedHandler?
	var onCruiseControlUpdated: BoolUpdatedHandler?
	var onVibratorSettingsUpdated: UInt8ArrayUpdatedHandler?
	var onKeyStateUpdated: BoolUpdatedHandler?
	var onWakeUpUpdated: BoolUpdatedHandler?
	var onHallSensorUpdated: UInt16UpdatedHandler?
	var onDepthSensorUpdated: UInt16UpdatedHandler?
	var onAccelerationUpdated: AccelerationUpdatedHandler?
	var onEnvironmentUpdated: EnvironmentUpdatedHandler?
	var onButtonsUpdated: ButtonsUpdatedHandler?
	var onUseLogUpdated: UInt16UpdatedHandler?

	let name: String
	let uuid: String
	let peripheral: CBPeripheral
	let lastConnected: Bool
	private (set) var lastSeen: Date
	private var discoveredServices: Int8 = 0

	private (set) var ready: Bool = false

	// MARK: Object lifecycle

	init(name: String, uuid: String, peripheral: CBPeripheral, lastConnected: Bool = false) {
		self.name = name
		self.uuid = uuid
		self.peripheral = peripheral
		self.lastConnected = lastConnected
		lastSeen = Date()
	}

	func justSeen() {
		lastSeen = Date()
	}

	func startUsingPeripheral() {
		peripheral.delegate = self
		peripheral.discoverServices([])
	}

	private (set) var connectionConfirmed: Bool = false {
		didSet {
			guard connectionConfirmed != oldValue else {
				return
			}
			onConnectionConfirmationChanged?(connectionConfirmed)
		}
	}
	var onConnectionConfirmationChanged: ((Bool) -> Void)?

	// MARK: Read, Write and Notify methods

	@discardableResult func read(from characteristic: F1sCharacteristic) -> Bool {
		guard let bleCharacteristic = findCharacteristic(with: characteristic.uuid), bleCharacteristic.properties.contains(.read) else {
			return false
		}
		peripheral.readValue(for: bleCharacteristic)
		return true
	}

	@discardableResult func write(_ data: Data, to characteristic:F1sCharacteristic) -> Bool {
		guard let bleCharacteristic = findCharacteristic(with: characteristic.uuid), bleCharacteristic.properties.contains(.writeWithoutResponse) else {
			guard let bleCharacteristic = findCharacteristic(with: characteristic.uuid), bleCharacteristic.properties.contains(.write) else {
				return false
			}
			peripheral.writeValue(data, for: bleCharacteristic, type: .withResponse)
			return true
		}
		// NOTE: We don't require for a response from the peripheral in this example,
		// but this could be added for production grade code.
		peripheral.writeValue(data, for: bleCharacteristic, type: .withoutResponse)
		return true
	}

	@discardableResult func write(_ bytes: [UInt8], to characteristic:F1sCharacteristic) -> Bool {
		let data = Data(bytes: bytes, count: bytes.count)
		return write(data, to: characteristic)
	}

	private func updateNotifications(for characteristic: F1sCharacteristic, enable: Bool) -> Bool {
		guard let bleCharacteristic = findCharacteristic(with: characteristic.uuid), bleCharacteristic.properties.contains(.notify) else {
			return false
		}
		peripheral.setNotifyValue(enable, for: bleCharacteristic)
		return true
	}

	@discardableResult func enableNotifications(for characteristic: F1sCharacteristic) -> Bool {
		return updateNotifications(for: characteristic, enable: true)
	}

	@discardableResult func disableNotifications(for characteristic: F1sCharacteristic) -> Bool {
		return updateNotifications(for: characteristic, enable: false)
	}

	// MARK: Common characteristics methods

	private func findCharacteristic(with uuid: CBUUID) -> CBCharacteristic? {
		guard let services = peripheral.services else {
			return nil
		}
		return services.flatMap({($0.characteristics ?? [])}).first(where: { $0.uuid == uuid })
	}
}

extension F1sBluetoothDevice {

	static func == (lhs: F1sBluetoothDevice, rhs: F1sBluetoothDevice) -> Bool {
		return lhs.uuid == rhs.uuid
	}
}

extension F1sBluetoothDevice: CBPeripheralDelegate {

	func peripheral(_ peripheral: CBPeripheral, didDiscoverServices error: Error?) {
		guard let services = peripheral.services else {
			return
		}
		for service in services {
			discoveredServices += 1
			peripheral.discoverCharacteristics([], for: service)
		}
	}

	func peripheral(_ peripheral: CBPeripheral, didDiscoverCharacteristicsFor service: CBService, error: Error?) {
		discoveredServices -= 1
		if discoveredServices < 1, let keyStateCharacteristic = findCharacteristic(with: F1sCharacteristic.keyState.uuid) {
			peripheral.setNotifyValue(true, for: keyStateCharacteristic)
			peripheral.readValue(for: keyStateCharacteristic)

			ready = true
		}
	}

	func peripheral(_ peripheral: CBPeripheral, didUpdateValueFor characteristic: CBCharacteristic, error: Error?) {
		switch characteristic.uuid {
		case F1sCharacteristic.manufacturerName.uuid:
			manufacturerName = characteristic.value?.asciiString()
			debug("Read Manufacturer name: \(manufacturerName ?? "<unknown/nil>")")
		case F1sCharacteristic.modelNumber.uuid:
			modelNumber = characteristic.value?.asciiString()
			debug("Read Model number: \(modelNumber ?? "<unknown/nil>")")
		case F1sCharacteristic.hardwareRevision.uuid:
			hardwareRevision = characteristic.value?.asciiString()
			debug("Read Hardware revision: \(hardwareRevision ?? "<unknown/nil>")")
		case F1sCharacteristic.firmwareRevision.uuid:
			firmwareRevision = characteristic.value?.asciiString()
			debug("Read Firmware revision: \(firmwareRevision ?? "<unknown/nil>")")
		case F1sCharacteristic.softwareRevision.uuid:
			softwareRevision = characteristic.value?.asciiString()
			debug("Read Software revision: \(softwareRevision ?? "<unknown/nil>")")
		case F1sCharacteristic.macAddress.uuid:
			macAddress = characteristic.value?.macAddressString()
			debug("Read MAC address: \(macAddress ?? "<unknown/nil>")")
		case F1sCharacteristic.serialNumber.uuid:
			serialNumber = characteristic.value?.asciiString()
			debug("Read Serial number: \(serialNumber ?? "<unknown/nil>")")
		case F1sCharacteristic.batteryLevel.uuid:
			guard let batteryLevelValue = characteristic.value?.uintValue() else {
				debug("Encountered invalid battery level")
				batteryLevel = nil
				return
			}
			batteryLevel = UInt8(batteryLevelValue)
			if let batteryLevel = batteryLevel {
				debug("Read Battery level: \(String(batteryLevel))")
			} else {
				debug("Read Battery level: <unknown/nil>)")
			}
		case F1sCharacteristic.motorControl.uuid:
			guard let motorSpeed = F1sMotorSpeed(from: characteristic.value) else {
				debug("Failed to create F1sMotorSpeed from received data")
				return
			}
			onMotorSpeedUpdated?(motorSpeed)
			debug("Read Motor control: main: \(motorSpeed.main); vibration: \(motorSpeed.vibration)")
		case F1sCharacteristic.cruiseControl.uuid:
			let cruiseControl = (characteristic.value?.uintValue() ?? 0x00) == 0x01
			onCruiseControlUpdated?(cruiseControl)
			debug("Read Cruise control: \(cruiseControl)")
		case F1sCharacteristic.vibratorSetting.uuid:
			guard let data = characteristic.value, data.count == 8 else {
				debug("Failed to create Array from received data")
				return
			}
			let vibratorSettings = data.map { $0 }
			onVibratorSettingsUpdated?(vibratorSettings)
			debug("Read Vibrator setting: \(vibratorSettings)")
		case F1sCharacteristic.keyState.uuid:
			let keyState = characteristic.value?.hexEncodedString() ?? "" == "01"
			connectionConfirmed = keyState
			// NOTE: Since this characteristic is enabled for notifications by default,
			// the console output can be a distracting, so it was commented out.
			// debug("Read Key state: \(keyState)")
		case F1sCharacteristic.wakeUp.uuid:
			let wakeUp = (characteristic.value?.uintValue() ?? 0x00) == 0x01
			onWakeUpUpdated?(wakeUp)
			debug("Read Wake up: \(wakeUp)")
		case F1sCharacteristic.hall.uuid:
			let hallValue = characteristic.value?.uintValue() ?? 0x00
			onHallSensorUpdated?(UInt16(hallValue))
			debug("Read Hall: \(hallValue)")
		case F1sCharacteristic.depth.uuid:
			let depth = characteristic.value?.uintValue() ?? 0x00
			onDepthSensorUpdated?(UInt16(depth))
			debug("Read Depth: \(depth)")
		case F1sCharacteristic.accelerometer.uuid:
			guard let acceleration = F1sAcceleration(from: characteristic.value) else {
				debug("Failed to create F1sAcceleration from received data")
				return
			}
			onAccelerationUpdated?(acceleration)
			debug("Read Accelerometer: \(String(format: "%04x %04x %04x", acceleration.x, acceleration.y, acceleration.z))")
		case F1sCharacteristic.pressureTemperature.uuid:
			guard let environment = F1sEnvironment(from: characteristic.value) else {
				debug("Failed to create F1sEnvironment from received data")
				return
			}
			onEnvironmentUpdated?(environment)
			debug(String(format: "Read Pressure and Temperature: temperature %0.2f℃; pressure %0.2fmbar", environment.temperature, environment.pressure))
		case F1sCharacteristic.button.uuid:
			guard let buttonState = F1sButtons(from: characteristic.value) else {
				debug("Failed to create F1sButtons from received data")
				return
			}
			onButtonsUpdated?(buttonState)
			debug("Button state: power: \(buttonState.central); plus: \(buttonState.plus); minus: \(buttonState.minus)")
		case F1sCharacteristic.usageLog.uuid:
			let useCount = characteristic.value?.uintValue() ?? 0x00
			onUseLogUpdated?(UInt16(useCount))
			debug("Read Use log: \(useCount)")
		default:
			debug("Got value update for unexpected characteristic: \(characteristic.uuid.uuidString)")
		}
	}
}
