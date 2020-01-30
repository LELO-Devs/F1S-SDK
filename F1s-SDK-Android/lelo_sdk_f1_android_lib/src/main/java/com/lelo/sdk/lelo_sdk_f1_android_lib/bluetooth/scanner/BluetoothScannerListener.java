package com.lelo.sdk.lelo_sdk_f1_android_lib.bluetooth.scanner;


import com.lelo.sdk.lelo_sdk_f1_android_lib.bluetooth.BLEDeviceWithRssi;

import java.util.List;

public interface BluetoothScannerListener {

    void onBluetoothNotSupported();
    void onBluetoothNotEnabled();
    void onBLEDevicesScanningDone(List<BLEDeviceWithRssi> deviceList);

}
