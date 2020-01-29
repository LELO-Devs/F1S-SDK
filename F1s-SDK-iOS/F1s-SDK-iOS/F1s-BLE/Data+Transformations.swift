//
//  Data+Transformations.swift
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

extension Data {

	init?(fromHex string: String) {
		let hexStr = string.dropFirst(string.hasPrefix("0x") ? 2 : 0)
		guard hexStr.count % 2 == 0 else {
			return nil
		}
		var data = Data(capacity: hexStr.count / 2)
		var indexIsEven = true
		for count in hexStr.indices {
			if indexIsEven {
				let hexRange = count...hexStr.index(after: count)
				guard let byte = UInt8(hexStr[hexRange], radix: 16) else {
					return nil
				}
				data.append(byte)
			}
			indexIsEven.toggle()
		}
		self = data
	}

	func asciiString() -> String? {
		return String(data: self, encoding: .ascii)
	}

	func hexEncodedString() -> String {
		return map { String(format: "%02hhx", $0) }.joined()
	}

	func macAddressString() -> String? {
		var addressString = hexEncodedString().uppercased()
		// NOTE: A MAC address must have 12 characters (without the double colons)
		guard addressString.count == 12 else {
			return nil
		}
		for count in 1...5 {
			addressString.insert(":", at: addressString.index(addressString.startIndex, offsetBy: count * 2 + (count - 1)))
		}
		return addressString
	}

	func intValue() -> Int? {
        return Int(self.hexEncodedString(), radix: 16)
    }

	func uintValue() -> UInt? {
        return UInt(self.hexEncodedString(), radix: 16)
    }
}
