//
//  F1sCharacteristics.swift
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

import Foundation
import CoreBluetooth

fileprivate struct F1sCharacteristicUUIDs {

	// NOTE: Found in primary service, 0x180a
	static let manufacturerNameUUID = CBUUID(string: "0x2a29")
	static let modelNumberUUID = CBUUID(string: "0x2a24")
	static let hardwareRevisionUUID = CBUUID(string: "0x2a27")
	static let firmwareRevisionUUID = CBUUID(string: "0x2a26")
	static let softwareRevisionUUID = CBUUID(string: "0x2a28")
	static let macAddressUUID = CBUUID(string: "0x0a06")
	static let serialNumberUUID = CBUUID(string: "0x0a05")

	// NOTE: Found in battery service, 0x180f
	static let batteryLevelUUID = CBUUID(string: "0x2a19")

	// NOTE: Found in custom service, 0xfff0
	static let motorControlUUID = CBUUID(string: "0xfff1")
	static let cruiseControlUUID = CBUUID(string: "0x0aa5")
	static let vibratorSettingUUID = CBUUID(string: "0x0a0d")
	static let keyStateUUID = CBUUID(string: "0x0a0f")
	static let wakeUpUUID = CBUUID(string: "0x0aa1")
	static let hallUUID = CBUUID(string: "0x0aa3")
	static let depthUUID = CBUUID(string: "0x0a0b")
	static let accelerometerUUID = CBUUID(string: "0x0a0c")
	static let pressureTemperatureUUID = CBUUID(string: "0x0a0a")
	static let buttonUUID = CBUUID(string: "0x0aa4")
	static let usageLogUUID = CBUUID(string: "0x0a04")
}

enum F1sCharacteristic: CaseIterable {
	
	case manufacturerName
	case modelNumber
	case hardwareRevision
	case firmwareRevision
	case softwareRevision
	case macAddress
	case serialNumber
	case batteryLevel
	case motorControl
	case cruiseControl
	case vibratorSetting
	case keyState
	case wakeUp
	case hall
	case depth
	case accelerometer
	case pressureTemperature
	case button
	case usageLog

	var uuid: CBUUID {
		switch self {
		case .manufacturerName:
			return F1sCharacteristicUUIDs.manufacturerNameUUID
		case .modelNumber:
			return F1sCharacteristicUUIDs.modelNumberUUID
		case .hardwareRevision:
			return F1sCharacteristicUUIDs.hardwareRevisionUUID
		case .firmwareRevision:
			return F1sCharacteristicUUIDs.firmwareRevisionUUID
		case .softwareRevision:
			return F1sCharacteristicUUIDs.softwareRevisionUUID
		case .macAddress:
			return F1sCharacteristicUUIDs.macAddressUUID
		case .serialNumber:
			return F1sCharacteristicUUIDs.serialNumberUUID
		case .batteryLevel:
			return F1sCharacteristicUUIDs.batteryLevelUUID
		case .motorControl:
			return F1sCharacteristicUUIDs.motorControlUUID
		case .cruiseControl:
			return F1sCharacteristicUUIDs.cruiseControlUUID
		case .vibratorSetting:
			return F1sCharacteristicUUIDs.vibratorSettingUUID
		case .keyState:
			return F1sCharacteristicUUIDs.keyStateUUID
		case .wakeUp:
			return F1sCharacteristicUUIDs.wakeUpUUID
		case .hall:
			return F1sCharacteristicUUIDs.hallUUID
		case .depth:
			return F1sCharacteristicUUIDs.depthUUID
		case .accelerometer:
			return F1sCharacteristicUUIDs.accelerometerUUID
		case .pressureTemperature:
			return F1sCharacteristicUUIDs.pressureTemperatureUUID
		case .button:
			return F1sCharacteristicUUIDs.buttonUUID
		case .usageLog:
			return F1sCharacteristicUUIDs.usageLogUUID
		}
	}
}
