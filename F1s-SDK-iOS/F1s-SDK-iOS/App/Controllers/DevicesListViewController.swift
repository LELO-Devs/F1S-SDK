//
//  DevicesListViewController.swift
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

class DevicesListViewController: UITableViewController {

	private let monitor = F1sMonitor()
	private var items = [F1sBluetoothDevice]()

	override func viewDidLoad() {
		super.viewDidLoad()

		monitor.onF1sItemFound = { item, last in
			if !self.items.contains(item) {
				self.items.append(item)
				DispatchQueue.main.async {
					self.tableView.reloadData()
				}
			}
			return false
		}

		monitor.onF1sItemRemoved = { item in
			if self.items.contains(item) {
				self.items.removeAll(where: { $0 == item })
				DispatchQueue.main.async {
					self.tableView.reloadData()
				}
			}
		}

		monitor.onAllF1sRemoved = {
			self.items.removeAll()
			DispatchQueue.main.async {
				self.tableView.reloadData()
			}
		}

		monitor.silence = false
	}

	override func viewDidAppear(_ animated: Bool) {
		super.viewDidAppear(animated)

		if F1sBluetoothManager.shared.connected {
			F1sBluetoothManager.shared.disconnect(startScan: true)
		} else {
			F1sBluetoothManager.shared.startScanning()
		}
	}

	override func viewWillDisappear(_ animated: Bool) {
		super.viewDidDisappear(animated)

		F1sBluetoothManager.shared.stopScanning()
	}

	// MARK: - Table view data source

	override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
		return items.count
	}

	override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
		let cell = tableView.dequeueReusableCell(withIdentifier: "f1sCell", for: indexPath)
		cell.textLabel?.text = items[indexPath.row].uuid
		return cell
	}

	// MARK: - Navigation

	@IBAction func unwindToList(_ unwindSegue: UIStoryboardSegue) {
		// NOTE: This method is needed for the unwind functionality to function properly
	}

	override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
		guard let cell = sender as? UITableViewCell, let indexPath = tableView.indexPath(for: cell), let destination = segue.destination as? F1sInjectable else {
			return
		}
		destination.f1sDevice = items[indexPath.row]
	}
}
