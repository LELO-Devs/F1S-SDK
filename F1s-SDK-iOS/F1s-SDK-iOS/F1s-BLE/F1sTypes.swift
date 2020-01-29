//
//  F1sTypes.swift
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

struct F1sAcceleration {

	let x: UInt16
	let y: UInt16
	let z: UInt16

	init?(from data: Data?) {
		guard let data = data, data.count == 6, let x = data[0...1].uintValue(), let y = data[2...3].uintValue(), let z = data[4...5].uintValue() else {
			return nil
		}
		self.x = UInt16(x)
		self.y = UInt16(y)
		self.z = UInt16(z)
	}
}

struct F1sButtons {

	let central: Bool
	let plus: Bool
	let minus: Bool

	init?(from data: Data?) {
		guard let data = data, data.count == 1, let value = data.uintValue() else {
			return nil
		}
		central = value == 0x00
		plus = value == 0x01
		minus = value == 0x02
	}
}

struct F1sMotorSpeed {

	let main: UInt8
	let vibration: UInt8

	init?(from data: Data?) {
		guard let data = data, data.count == 3, data[1] < 0x65, data[2] < 0x65 else {
			return nil
		}
		self.main = data[1]
		self.vibration = data[2]
	}
}

struct F1sEnvironment {

	let temperature: Double
	let pressure: Double

	init?(from data: Data?) {
		guard let data = data, data.count == 8, data[3] == 0xff, let temperature = data[0...2].uintValue(), let pressure = data[4...7].uintValue() else {
			return nil
		}
		self.temperature = Double(temperature) / 100
		self.pressure = Double(pressure) / 100
	}
}
