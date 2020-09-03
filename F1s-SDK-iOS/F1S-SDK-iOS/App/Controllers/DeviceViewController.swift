//
//  DeviceViewController.swift
//  F1S-SDK-iOS
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

import UIKit

class DeviceViewController: UITableViewController, F1sInjectable {

	var f1sDevice: F1sBluetoothDevice?

	@IBOutlet var manufacturerNameCell: UITableViewCell!
	@IBOutlet var modelNumberCell: UITableViewCell!
	@IBOutlet var hardwareRevisionCell: UITableViewCell!
	@IBOutlet var firmwareRevisionCell: UITableViewCell!
	@IBOutlet var softwareRevisionCell: UITableViewCell!
	@IBOutlet var macAddressCell: UITableViewCell!
	@IBOutlet var serialNumberCell: UITableViewCell!

	@IBOutlet var batteryLevelCell: UITableViewCell!

	@IBOutlet var hallCell: UITableViewCell!
	@IBOutlet var depthCell: UITableViewCell!
	@IBOutlet var accelerationCell: UITableViewCell!
	@IBOutlet var environmentCell: UITableViewCell!
	@IBOutlet var buttonsCell: UITableViewCell!
	@IBOutlet var useCountCell: UITableViewCell!

	override func viewWillAppear(_ animated: Bool) {
		super.viewWillAppear(animated)

		guard let f1sDevice = f1sDevice else {
			fatalError("We really should have an F1sBluetoothDevice at this point.")
		}

		f1sDevice.disableNotifications(for: F1sCharacteristic.keyState)

		f1sDevice.onManufacturerNameUpdated = {
			self.manufacturerNameCell.textLabel?.text = $0
		}

		f1sDevice.onModelNumberUpdated = {
			self.modelNumberCell.textLabel?.text = $0
		}

		f1sDevice.onHardwareRevisionUpdated = {
			self.hardwareRevisionCell.textLabel?.text = $0
		}

		f1sDevice.onFirmwareRevisionUpdated = {
			self.firmwareRevisionCell.textLabel?.text = $0
		}

		f1sDevice.onSoftwareRevisionUpdated = {
			self.softwareRevisionCell.textLabel?.text = $0
		}

		f1sDevice.onMacAddressUpdated = {
			self.macAddressCell.textLabel?.text = $0
		}

		f1sDevice.onSerialNumberUpdated = {
			self.serialNumberCell.textLabel?.text = $0
		}

		f1sDevice.onBatteryLevelUpdated = {
			self.batteryLevelCell.textLabel?.text = String($0)
		}

		f1sDevice.onHallSensorUpdated = {
			self.hallCell.textLabel?.text = String(format: "%d rot/sec", $0)
		}

		f1sDevice.onDepthSensorUpdated = {
			self.depthCell.textLabel?.text = String($0)
		}

		f1sDevice.onAccelerationUpdated = {
			self.accelerationCell.textLabel?.text = String(format: "x: %d; y: %d, z: %d", $0.x, $0.y, $0.z)
		}

		f1sDevice.onEnvironmentUpdated = {
			self.environmentCell.textLabel?.text = String(format: "temp: %0.2f℃; pres: %0.2fmbar", $0.temperature, $0.pressure)
		}

		f1sDevice.onButtonsUpdated = {
			if $0.central {
				self.buttonsCell.textLabel?.text = "Pow"
			} else if $0.plus {
				self.buttonsCell.textLabel?.text = "+"
			} else if $0.minus {
				self.buttonsCell.textLabel?.text = "-"
			} else {
				self.buttonsCell.textLabel?.text = " "
			}
		}

		f1sDevice.onUseLogUpdated = {
			self.useCountCell.textLabel?.text = String($0)
		}
	}

	override func viewDidAppear(_ animated: Bool) {
		super.viewDidAppear(animated)

		guard let f1sDevice = f1sDevice else {
			fatalError("We really should have an F1sBluetoothDevice at this point.")
		}

		[F1sCharacteristic.manufacturerName, .modelNumber, .hardwareRevision, .firmwareRevision, .softwareRevision, .macAddress, .serialNumber, .batteryLevel, .usageLog].forEach { f1sDevice.read(from: $0) }

		[F1sCharacteristic.hall, .depth, .accelerometer, .pressureTemperature, .button].forEach { f1sDevice.enableNotifications(for: $0) }

		// NOTE: Not all characteristics are implemented in this example
		// The ones missing:
		// .motorControl
		// .cruiseControl
		// .vibratorSetting
		// .wakeUp
	}

	override func viewWillDisappear(_ animated: Bool) {
		super.viewWillDisappear(animated)

		guard let f1sDevice = f1sDevice else {
			fatalError("We really should have an F1sBluetoothDevice at this point.")
		}

		f1sDevice.onManufacturerNameUpdated = nil
		f1sDevice.onModelNumberUpdated = nil
		f1sDevice.onHardwareRevisionUpdated = nil
		f1sDevice.onFirmwareRevisionUpdated = nil
		f1sDevice.onSoftwareRevisionUpdated = nil
		f1sDevice.onMacAddressUpdated = nil
		f1sDevice.onSerialNumberUpdated = nil

		f1sDevice.onBatteryLevelUpdated = nil

		f1sDevice.onHallSensorUpdated = nil
		f1sDevice.onDepthSensorUpdated = nil
		f1sDevice.onAccelerationUpdated = nil
		f1sDevice.onEnvironmentUpdated = nil
		f1sDevice.onButtonsUpdated = nil
		f1sDevice.onUseLogUpdated = nil
	}

	@IBAction func actionItemTapped(_ sender: UIBarButtonItem) {
		let alert = UIAlertController(title: "Actions", message: "What action would you like to perform?", preferredStyle: .actionSheet)

		alert.addAction(UIAlertAction(title: "Stop motor", style: .default) { _ in
			self.f1sDevice?.stopMotors()
		})

		alert.addAction(UIAlertAction(title: "Shutdown device", style: .default) { _ in
			self.f1sDevice?.shutdownDevice()
		})

		alert.addAction(UIAlertAction(title: "Verify accelerometer", style: .default) { _ in
			self.f1sDevice?.verifyAccelerometer()
		})

		alert.addAction(UIAlertAction(title: "Clear use log", style: .default) { _ in
			self.f1sDevice?.clearUseLog()
		})

		self.present(alert, animated: true)
	}
}
