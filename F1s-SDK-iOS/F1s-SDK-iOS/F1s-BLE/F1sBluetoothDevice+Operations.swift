//
//  F1sBluetoothDevice+Operations.swift
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

extension F1sBluetoothDevice {

	// Motor control

	@discardableResult func setMotorSpeed(main: UInt8, vibration: UInt8) -> Bool {
		guard main < 0x65, vibration < 0x65 else {
			return false
		}
		write([0x01, main, vibration], to: .motorControl)
		return true
	}

	func stopMotors() {
		write([0x01, 0xff], to: .motorControl)
	}

	func shutdownDevice() {
		write([0x01, 0xfa], to: .motorControl)
	}

	func verifyAccelerometer() {
		write([0xff, 0xff, 0xff], to: .motorControl)
	}

	// Motor control

	func enableCruiseControl(reset: Bool = false) {
		write([reset ? 0x02 : 0x01], to: .cruiseControl)
	}

	func disableCruiseControl() {
		write([0x00], to: .cruiseControl)
	}

	// Vibrator setting

	func setVibrationSpeeds(for capSensor: [UInt8]) -> Bool {
		guard capSensor.count == 8, capSensor.first(where: { $0 > 0x64 }) == nil else {
			return false
		}
		write(capSensor, to: .vibratorSetting)
		return true
	}

	// Wake up

	func enableWakeup(reset: Bool = false) {
		write([0x01], to: .wakeUp)
	}

	func disableWakeup() {
		write([0x00], to: .wakeUp)
	}

	// Usage log

	func clearUseLog() {
		write([0xee], to: .usageLog)
	}
}
