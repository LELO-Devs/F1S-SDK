//
//  ConfirmConnectionViewController.swift
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

fileprivate enum ConnectionState {
	case unknown
	case connecting
	case confirming
	case connected

	var description: String {
		switch self {
		case .connecting:
			return "Connecting to your F1s."
		case .confirming:
			return "Please confirm the connection by pressing the button on your F1s."
		case .connected:
			return "Connection to your F1s is confirmed."
		default:
			return "Unknown state"
		}
	}
}

class ConfirmConnectionViewController: UIViewController, F1sInjectable {

	var f1sDevice: F1sBluetoothDevice?

	@IBOutlet var stateLabel: UILabel!

	private let monitor = F1sMonitor()

	private var connectionState: ConnectionState = .unknown {
		didSet {
			stateLabel.text = connectionState.description
		}
	}

	override func viewDidLoad() {
		super.viewDidLoad()

		monitor.onF1sConnected = { [weak self] item in
			guard let self = self else {
				return
			}
			self.connectionState = .confirming

			// TODO: Maybe add a timer for monitoring...
			self.f1sDevice?.onConnectionConfirmationChanged = { [weak self] confirmation in
				guard let self = self else {
					return
				}
				DispatchQueue.main.async {
					self.performSegue(withIdentifier: "confirmedConnection", sender: self)
				}
			}
		}

		monitor.onF1sConnectFailed = { [weak self] item, error in
			guard let self = self else {
				return
			}
			var message = "There was an error connecting to the selected F1s device."
			if let error = error {
				message += "\n\(error.localizedDescription)"
			}
			let alert = UIAlertController(title: "Error Connecting to F1s", message: message, preferredStyle: .alert)
			alert.addAction(UIAlertAction(title: "Ok", style: .default) { action in
				self.performSegue(withIdentifier: "unwindToDevicesList", sender: self)
			})
			DispatchQueue.main.async {
				self.present(alert, animated: true)
			}
		}
	}

	override func viewDidAppear(_ animated: Bool) {
		super.viewDidAppear(animated)

		// NOTE: We could just as easy start the connection process in viewDidLoad
		guard let f1sDevice = f1sDevice else {
			fatalError("We really should have an F1sBluetoothDevice at this point.")
		}
		F1sBluetoothManager.shared.connect(to: f1sDevice)
	}

	override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
		injectF1sItem(into: segue.destination)
	}
}
