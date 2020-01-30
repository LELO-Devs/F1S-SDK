//
//  AppDelegate.swift
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

import UIKit
import CoreBluetooth

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

	var window: UIWindow?

	#if DEBUG
	var monitor: F1sMonitor?
	#endif

	func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {

		// Trigger the bluetooth manager to start up bluetooth and commence scanning as soon as possible
		let _ = F1sBluetoothManager.shared

		#if DEBUG
		monitor = F1sMonitor()

		monitor?.onBluetoothStatusChange = { state in
			print("Bluetooth status changed to \(state.description)")
		}

		monitor?.onF1sItemFound = { item, last in
			print("Found f1s item: \(item)\(last ? " - was last connected to this device" : "")")
			return false
		}

		monitor?.onF1sItemRemoved = { item in
			print("Removed f1s item: \(item)")
		}

		monitor?.onAllF1sRemoved = {
			print("All f1s devices were removed")
		}

		monitor?.onF1sConnected = { item in
			print("Connected to f1s device: \(item)")
		}

		monitor?.onF1sDisconnected = { item in
			print("Disconnected from f1s device: \(item)")
		}

		monitor?.onF1sConnectFailed = { item, error in
			guard let error = error else {
				print("Failed to connect to device: \(item)")
				return
			}
			print("Failed to connect to device: \(item)\n\(error.localizedDescription)")
		}
		#endif

		return true
	}
}

#if DEBUG
fileprivate extension CBManagerState {
	var description: String {
		switch self {
		case .unknown:
			return "unknown"
		case .resetting:
			return "resetting"
		case .unsupported:
			return "unsupported"
		case .unauthorized:
			return "unauthorized"
		case .poweredOff:
			return "poweredOff"
		case .poweredOn:
			return "poweredOn"
		@unknown default:
			fatalError("unknown value encountered")
		}
	}
}
#endif
