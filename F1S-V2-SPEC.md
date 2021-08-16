[![License: CC BY-ND 4.0](https://img.shields.io/badge/License-CC%20BY--ND%204.0-lightgrey.svg)](https://creativecommons.org/licenses/by-nd/4.0/)

# F1S(VER 2) BLE Specification

- [What is this?](#what-is-this)
- [App development considerations](#app-development-considerations)
    - [Security](#security)
    - [Calibration of the accelerometer sensors](#calibration-of-the-accelerometer-sensors)
    - [Powering off the device](#powering-off-the-device)
    - [Reading buttons while motors are running](#reading-buttons-while-motors-are-running)
- [BLE Services](#ble-services)
    - [Device Information Service](#device-information-service)
    - [Battery Service](#battery-service)
    - [LELO Custom Service](#lelo-custom-service)
- [BLE Characteristics details](#ble-characteristics-details)
    - [Motor control](#motor-control)
    - [Advanced Motor control](#advanced-motor-control)
    - [Cruise control](#cruise-control)
    - [Vibrator setting](#vibrator-setting)
    - [Security](#security)
    - [Wake up](#wake-up)
    - [Hall sensor](#hall-sensor)
    - [Depth sensor](#depth-sensor)
    - [Accelerometer](#accelerometer)
    - [Pressure & Temperature](#pressure--temperature)
    - [Buttons](#buttons)
    - [Use log](#use-log)

## What is this?

[LELO F1S SDK](https://www.lelo.com/f1s-developers-kit-red) BLE Specification.

This file defines the BLE interface that your F1S exposes to a controlling central. It holds information on the BLE services you'll need to search for and the characteristics you'll need to achieve desired effects.

## App development considerations

### Security

While designing the F1S(Ver. 2) device we made sure to protect you and your device from unwanted access by anyone but yourself. One of the security features is a characteristic named `Security Access` which will block the device from functioning unless the user physically confirms the connection, which is done by pressing the power button on the F1S device **after** the connection has already been established.

A logic flow diagram would contain the following steps:

* The user starts the app, which starts bluetooth scanning for devices
* The user powers up the F1S device
* The app connects to the F1S device
* The app monitor `Security Access` characteristic, Initially, ` Security Access` is set to `00`.
* After connecting, the app shows a message to the user, prompting them to press the power button again
* After ` Security Access` is set to a certain `String` (that is the password of connecting F1S) rather than the default(`00`) 
* Write the `String`(password) into `Security Access` characteristic. If `password` is correct then the `Security Access` characteristic is set to `01`. the app continues operation.


### Calibration of the accelerometer sensors

When starting the calibration process, the device should be placed upright on a flat surface with the buttons facing upwards, after which a value of `0xFFFFFF` should be written to the `Motor Control` characteristic (`0xFFF1`).

The apps UI should clarify this process to the user.

### Powering off the device

Please use the `Motor control` characteristic for this. Writing a 0x01FA will cause your F1S device to shut down.

### Reading buttons while motors are running

Please note that your F1S device will lock all buttons while the motors are running.

## BLE Services

Out of the BLE services your F1S device exposes the following are the ones you'll be interested in:

* Device Information Service (0x180A)
* Battery Service (0x180F)
* LELO Custom Service (0xFFF0)

In the listed services you'll find a number of characteristics, which allow reading from your devices sensors as well as control its various motors and behaviours.

### Device Information Service

The Device Information Service (0x180A) is a common BLE service in which you'll find the following characteristics (all are read-only):

| Characteristic | UUID | Type | Description | Note |
| -------------- |:----:| ---- | ----------- | ---- |
| Manufacturer name | 0x2A29 | String | Ascii encoded name of the manufacturer | `LELO` |
| Model number | 0x2A24 | String | Ascii encoded model name of your F1S device | `F1SV2X` |
| Hardware revision | 0x2A27 | String | Ascii encoded hardware revision of your F1S | `F1S REV.D` |
| Firmware revision | 0x2A26 | String | Ascii encoded firmware revision installed on your F1S | `F1SV2X SVN_2631` |
| Software revision | 0x2A28 | String | Ascii encoded software revision installed on your F1S | `F1SV2X Rev.A` |
| MAC address | 0x0A06 | String | Ascii encoded BLE MAC address of your F1S device | double colons (`:`) are removed; True value is returned only after connection is confirmed |
| Serial number | 0x0A05 | String | Ascii encoded serial number of your F1S device | True value provided only after connection is confirmed |

### Battery Service

The Battery Service (0x180F) is another common BLE service in which you'll find a single characteristic, which you can either read from or enable notify for:

| Characteristic | UUID | Type | Description | Note |
| -------------- |:----:| ---- | ----------- | ---- |
| Battery level | 0x2A19 | Byte | Single byte representing the battery level of your F1S device | Value 0-100% (0x00-0x64) |

### LELO Custom Service

This service holds the interesting characteristics, the ones you'll use to make the device do what you want it to do, while reading the characteristics to sense how the device is being used.

While some of these characteristics are read only, others can be written to and some support the notify functionality. This will be specified in the Access column with the letters `R`, `W` and `N` representing `Read`, `Write` and `Notify`, respectively.

With the exception of `Security` none of these characteristics may be accessed until the connection to your device is confirmed (`Key state` is `01` or `true`).

| Characteristic | UUID | Access | Description | Note |
| -------------- |:----:|:------:| ----------- | ---- |
| Motor control | 0xFFF1 | R / W | Motor rotation speed | Both main and vibrator motors |
| Advanced Motor control | 0x0A1A | R / W | Motor rotation speed & pattern | Both main and vibrator motors |
| Cruise control | 0x0AA5 | R / W | Enable the Hall sensors in your F1S devices cap | |
| Vibrator setting | 0x0A0D | R / W | Set vibration level for individual depth sensor values | |
| Security Access | 0x0A10 | R / W / N | Security feature to protect you and your device. Used for connection confirmation | |
| Wake up | 0x0AA1 | R / W | Allows quick connection to the device without requiring the user to press the power button | The connection will still need to be confirmed |
| Hall sensor | 0x0AA3 | R / N | Main motor rotation speed | Rotations per second |
| Depth sensor | 0x0A0B | R / N | 8 touch sensors in the chamber combined into a single value | |
| Accelerometer | 0x0A0C | R / N | Accelerometer sensor in your F1S device | Separate values for the x, y, z axis |
| Pressure & Temperature | 0x0A0A | R / N | Air pressure and temperature inside your device | in mbar and ℃ respectively |
| Buttons | 0x0AA4 | R / N | The state of your F1S device buttons | `+`, `-` and `power` |
| Use log | 0x0A04 | R / W | Device use log / counter | Counts number of sessions (or resets the same) |

## BLE Characteristics details

The rest of this document contains a more detailed description of our individual custom characteristics, the data they require or return, and of course, the function they perform and/or support.

### Motor control

| UUID | Operations | Data Length | Description |
|:----:| ---------- |:-----------:| ----------- |
| 0xFFF1 | Read, Write | 3 bytes | This characteristic is used to control your F1S devices main motor, the vibration motor, to turn off your device and to calibrate the accelerometer in your device. |

#### Read

When reading from this characteristic, you'll receive 3 bytes of data, which should be interpreted as a 24 bit unsigned integer: 0x01yyzz

The first 8 bits will always be 0x01, while the second and third 8 bits (`yy` and `zz` in the above hex representation, respectively) represent the main motors speed (0-100%, 0x00-0x64) and the vibrator motors speed (0-100%, 0x00-0x64).

#### Write

When writing to this characteristic, you can trigger multiple functions:

| Value | Function | Description |
| ----- | -------- | ----------- |
| 0x01yyzz | Set motor speeds | Allows your code to set both the main motors speed as well as the speed of the vibration motor. The `yy` and `values` are defined the same as explained above in the `Read` section for this characteristic. Please note that below 30% the motors might not rotate, depending on the battery level of your F1S device. Also note that setting the motor speeds will lock your F1S devices physical buttons. |
| 0x01FF | Stop motors | Stops all motor activity and unlock the physical buttons. The last set motor parameters will be remembered, and if any of the caps sensors are triggered, the device will start vibrating at last set speed again. |
| 0x01FA | Shutdown the device | Stops all the devices motors and shuts the device down. |
| 0xFFFFFF | Verify Accelerometer | This is used to verify and/or calibrate the accelerometer data. The device should be placed upright (with the buttons facing upwards), after which the `x` and `y` values from the accelerometer should approach 0, and the `z` value will approach 1024. |

---

### Advanced Motor control

| UUID | Operations | Data Length | Description |
|:----:| ---------- |:-----------:| ----------- |
| 0x0A1A | Read, Write | 1 byte | This characteristic is used to control your F1S devices main motor, the vibration motor, to turn off your device|

| Value | Meaning | Note |
|:-----:| ------- | ---- |
| 0x0400 | Turn off device | |
| 0x0405XXYY[T1][T2][Pmax][Pmin] | Control your F1S device in the advanced way||

#### Paramemter Descriptions.
[XX](1 byte):01 indicates Main motor, 02 indicates vibrator motor,
[YY](1 byte):02~08 indicates the motor vibrating pattern. The motor vibration patterns are composed of the below parameters
[T1](2 byte):the duration time of period 1.
[T2](2 byte):the duration time of period 2.
[Pmax](1 byte): the motor speed of period 1.
[Pmin](1 byte): the motor speed of period 2.

#### For Example: 
04050108000000003200,  main motor works on pattern 8(continues) with 50% motor speed.
040502030bb80bb8411e,  vibrator motor works on pattern 3, T1:3s, T2:3s, Pmax:65%, Pmin:30%

#### 8 Motor Vibration Patterns are listing here

![image](https://user-images.githubusercontent.com/87749976/129508623-1f73746c-9fbf-4257-aadb-6809b0e7c3a3.png)

![image](https://user-images.githubusercontent.com/87749976/129508744-87d03d59-ce9c-4e35-ae9f-f0803b654d7c.png)

---

### Cruise control

| UUID | Operations | Data Length | Description |
|:----:| ---------- |:-----------:| ----------- |
| 0x0AA4 | Read, Write | 1 byte | Enable the hall sensors in your F1S devices cap. |

Cruise control controls the devices vibration speed depending on the Hall sensors value. (see `Vibrator setting` and `Depth`)

| Value | Meaning | Note |
|:-----:| ------- | ---- |
| 0x00 | Hall cap sensors are disabled | |
| 0x01 | Hall cap sensors are enabled | |
| 0x02 | Enable cap sensors and reset speed settings to default values | Write only |

#### Read

Returns a single byte representing one of the states listed above.

#### Write

Write a single byte with a value from the table above.

---

### Vibrator setting

| UUID | Operations | Data Length | Description |
|:----:| ---------- |:-----------:| ----------- |
| 0x0A0D | Read, Write | 8 bytes | Set the vibrator motor speed setting for the corresponding cap sensor. |

As each cap sensor (8 of them in total, see `Depth` for additional data) is triggered, a certain vibration speed can be trigger automatically.

#### Read

When reading from this characteristic, 8 bytes will be returned, with each byte representing a vibration speed setting (0-100%, 0x00-0x64), on for each cap sensor.

#### Write

Write 8 bytes to this characteristic to set the vibration speed for each triggered cap sensor (see `Read` above for details on the values).

---

### Key state

| UUID | Operations | Data Length | Description |
|:----:| ---------- |:-----------:| ----------- |
| 0x0A0F | Read, Notify | 1 byte | A security feature to protect your F1S device and yourself. To unlock the full functionality of your F1S device you need to confirm a connection by pressing the power button after a connection is established. This characteristic will reflect the state of the confirmation process. |

#### Read

The format of the data is the same when reading or receiving notifications from this characteristic.

| Value | Meaning |
|:-----:| ------- |
| 0x00 | Connection **not** confirmed, limited functionality. |
| 0x01 | Connection confirmed, full functionality enabled. |

---

### Wake up

| UUID | Operations | Data Length | Description |
|:----:| ---------- |:-----------:| ----------- |
| 0x0AA1 | Read, Write | 1 byte | Your F1S device can either be fully powered off or exist in a "sleep mode" state, in which it will still send out BLE advertisements (although sparsely), which means an app can connect to the device without the user pressing the power button. Note that the connection will still need to be confirmed, though. |

#### Read

Reading from this characteristic will return a single byte, with it's value meaning the following:

| Value | Meaning |
|:-----:| ------- |
| 0x00 | Wake up function disabled, device will fully power down. |
| 0x01 | Wake up function enabled, device will send out occasional BLE advertisements and can be connected to. |

#### Write

When writing to this characteristic, write a single byte as described for `Read` above.

---

### Hall sensor

| UUID | Operations | Data Length | Description |
|:----:| ---------- |:-----------:| ----------- |
| 0x0AA3 | Read, Notify | 2 bytes | Represents the current motor speed in rotations per second. |

This characteristic can both be read from as well as set up to notify the app of changes. It's format is a 16 bit unsigned integer number which represents the main motors rotation speed in rotations per second.

---

### Depth sensor

| UUID | Operations | Data Length | Description |
|:----:| ---------- |:-----------:| ----------- |
| 0x0A0B | Read, Notify | 2 bytes | Represents the deepest triggered cap sensor. |

This characteristic can both be read from as well as set up to notify the app of changes. It's format is a 16 bit unsigned integer number which represents the deepest triggered cap sensor in your F1S device. There are 8 cap sensors, so this value ranges from 0x0000-0x0008.

The value this characteristic gives you represents the thrust depth of whats inserted into your F1S device.

---

### Accelerometer

| UUID | Operations | Data Length | Description |
|:----:| ---------- |:-----------:| ----------- |
| 0x0A0C | Read, Notify | 6 bytes | Represents the 3 axis accelerometer and/or the orientation of your F1S device. |

This characteristic can both be read from as well as set up to notify the app of changes. The data delivered will consist of 6 bytes, with individual byte pairs (16 bit) representing the readout for a single axis: 0x `xxxx` `yyyy` `zzzz`.

---

### Pressure & Temperature

| UUID | Operations | Data Length | Description |
|:----:| ---------- |:-----------:| ----------- |
| 0x0A0A | Read, Notify | 8 bytes | Reads the pressure and temperature inside the devices chamber. |

You may either trigger a read from this characteristic or enable notifications for it. Either way, you'll receive 8 bytes of data, which should be interpreted as two 32 bit unsigned integers: 0x `yyy` `FF` `zzzz`, with `yyyy` representing the temperature, `zzzz` representing the pressure and both are separated by a single byte with value 0xFF.

**Pressure**

The pressure is a fixed floating point number, expressed in millibars (mbar), meaning the value should be converted into a floating point number and divided by 100:

0x00018AE0 => 101088 => 1010.88mbar

**Temperature**

Likewise, the temperature is delivered as a fixed floating point value, expressed in degrees Celsius (℃), meaning the value should be converted into a floating point number and divided by 100:

0x00000A86 => 2694 => 26.94℃

---

### Buttons

| UUID | Operations | Data Length | Description |
|:----:| ---------- |:-----------:| ----------- |
| 0x0AA4 | Read, Notify | 1 byte | Indicates what device button is pressed down. |

Reading (or watching for notifications) from this characteristic allows the app to react to button presses and release events. The following values may be delivered to the app:

| Value | Meaning |
|:-----:| ------- |
| 0x00 | The central power button was pressed down. |
| 0x01 | The `+` button was pressed down. |
| 0x02 | The `-` button was pressed down. |
| 0x03 | All buttons were released. |

---

### Use log

| UUID | Operations | Data Length | Description |
|:----:| ---------- |:-----------:| ----------- |
| 0x0A04 | Read, Write | 2 bytes | A value counting the number of times the device was used. |

#### Read

Reading from this characteristic will return a 16 bit unsigned integer value representing the number of times your F1S device has been used.

This counter will increase for each session lasting 30 seconds or more. We define a session as the time between the devices startup and its shutdown. This means that if the devices is powered up, left for at least 30 seconds and then powered down, this will be recorded as a session.

If however the device is powered down less than 30 seconds after powering up, this is not counted as a session and the `Use log` counter will not increase.

Please note that we've simplified the explanation a bit, but in reality it's not 30s from powering up the device, but rather after the device finishes its initialisation.

#### Write

Writing a value of 0xEE to this characteristic will cause your F1S device to reset this counter to 0.
