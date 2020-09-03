//
//  F1sBluetoothState.swift
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

enum F1sBluetoothState {
	case unknown
	case resetting
	case unsupported
	case unauthorized
	case poweredOff
	case poweredOn

	static let notificationName = Notification.Name("bluetoothStateChanged")

	var cbManagerState: CBManagerState {
		switch self {
		case .unknown:
			return .unknown
		case .resetting:
			return .resetting
		case .unsupported:
			return .unsupported
		case .unauthorized:
			return .unauthorized
		case .poweredOff:
			return .poweredOff
		case .poweredOn:
			return .poweredOn
		}
	}

	static func from(cbManagerState: CBManagerState) -> F1sBluetoothState {
		switch cbManagerState {
		case .unknown:
			return .unknown
		case .resetting:
			return .resetting
		case .unsupported:
			return .unsupported
		case .unauthorized:
			return .unauthorized
		case .poweredOff:
			return .poweredOff
		case .poweredOn:
			return .poweredOn
		@unknown default:
			fatalError("Unknown CBManagerState encountered")
		}
	}
}
