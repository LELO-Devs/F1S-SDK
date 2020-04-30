[![License: CC BY-ND 4.0](https://img.shields.io/badge/License-CC%20BY--ND%204.0-lightgrey.svg)](https://creativecommons.org/licenses/by-nd/4.0/)

# LELO F1s SDK

- [What is this?](#what-is-this)
- [In a hurry to get started?](#in-a-hurry-to-get-started)
- [iOS and Android libraries](#ios-and-android-libraries)
- [Sample Projects](#sample-projects)
- [F1s BLE Reference](#f1s-ble-reference)
- [Licence](#licence)
    - [TL;DR](#tldr)
- [Other languages](#other-languages)

## What is this?

The [LELO F1s SDK](https://www.lelo.com/f1s-developers-kit-red) enables you to write your own code and experiment with your F1s device, either for your own indulgence or to release as an app for others to enjoy.

Using the SDK you can control individual aspects of the F1s device and read from its various sensors, enabling you to create your very own take on the F1s experience.

## In a hurry to get started?

Make sure to charge your F1s device before use. We recommend you charge your F1s device for at least 2 hour charge before first use. Turn off your F1s: long press the power button until the lights on the F1s device turn off.

Decide whether you'd like to try out the iOS or Android SDK, open the project from the appropriate directory (iOS or Android, respectively), using the appropriate IDE environment (Xcode for iOS, Android Studio for Android). Build and run the project, after which you should turn on your F1s device and select it in the app.

Press the power button on the F1s to turn on the device. After a connection with the app has been established, the device will require a connection confirmation (you wouldn't want to allow just anyone to connect to your F1s device, now would you?): press the power button on the F1s device again.

At this point your device should be connected to the app, the app should have detected the confirmation, and now you can control the various aspects of the device through the (admittedly spartan) apps interface.

## iOS and Android libraries

We are working on these and will be adding them to [our SDK repository](https://github.com/LELO-Devs/F1s-SDK) in the following days.

## Sample Projects

We are working on these and will be adding them to [our SDK repository](https://github.com/LELO-Devs/F1s-SDK) in the following days.

## Learn more about the F1s

Your first (and last, for that matter) stop for information on your F1s device should be the [main website](https://www.lelo.com/f1s-developers-kit-red). We don't want to duplicate the provided information here, in this SDK.

## F1s BLE Reference

After running our example code, you'll probably want to dive into the inner workings of the BLE communication with your F1s device. We have a [separate document](BLE-Specs.md) for that.

In the [BLE Specs](BLE-Specs.md) file you'll find more information on the services your F1s will provide over BLE, the characteristics you'll find, and what the individual characteristics are used for (and how).

This should be enough to allow you to even write your own BLE app, without the use of this SDK.

## Licence

Our documentation is released under the [Creative Commons Attribution-NoDerivatives 4.0 International (CC BY-ND 4.0)](https://creativecommons.org/licenses/by-nd/4.0/) licence, while the code samples are released under the [Apache Licence 2.0](http://www.apache.org/licenses/LICENSE-2.0) licence.

### TL;DR:

You may use the documentation for the purpose of writing software, but you should mention us. You can't distribute the documentation in an altered form.

The code samples (and associated libraries) you may use as you like, as long as you include the required notices.

* [Creative Commons Attribution-NoDerivatives 4.0 International (CC BY-ND 4.0)](https://tldrlegal.com/license/creative-commons-attribution-noderivatives-4.0-international-(cc-by-nd-4.0))
* [Apache License 2.0 (Apache-2.0)](https://tldrlegal.com/license/apache-license-2.0-(apache-2.0))

## Other languages

As we'd hoped, the community is willing to put time and effort behind this, and we've noticed a few projects popping up, among other, implementations of our BLE specs in other languages, allowing for implementation on other platforms than we envisioned when embarking on open sourcing our specs.

Have a look at the following, if you're interested.  
Please note that we're not directly associated to these projects, and do not take any responsibility for the code you'll find there or any damage that might occur to your device as a result of using them, nor do we offer support on the use of these projects.

### Javascript

* [LELO F1 SDK Web Bluetooth Client](https://github.com/fabiofenoglio/lelo-f1-web-sdk)

### Python

* [LELO F1 SDK Python client](https://github.com/fabiofenoglio/lelo-f1-python-sdk)
