package com.lelo.sdk.lelo_sdk_f1_android_lib.bluetooth.scanner;

import android.app.Activity;

import java.util.List;


public interface BluetoothScannerAPI {

    public final int REQUEST_ENABLE_BT = 1;

    void setListener(BluetoothScannerListener listener);
    void removeListener();


    void setUp(Activity activity, List<String> mFindDeviceList);

    void onResume();
    void onPause();
    void onDestroy();

    void startScanningForBLEDevice(final boolean enable);
    void stopScanningForBLEDevice();
}
