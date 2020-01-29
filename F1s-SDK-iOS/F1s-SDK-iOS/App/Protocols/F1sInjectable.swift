//
//  F1sInjectable.swift
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

protocol F1sInjectable: class {
	var f1sDevice: F1sBluetoothDevice? { get set }
}

extension F1sInjectable where Self: UIViewController {

	@discardableResult func injectF1sItem(into viewController: UIViewController) -> Bool {
		guard let f1sDevice = f1sDevice, let injectable = viewController as? F1sInjectable else {
			return false
		}
		injectable.f1sDevice = f1sDevice
		return true
	}
}
