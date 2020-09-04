package com.lelo.sdk.lelo_sdk_f1_android_lib;

import android.bluetooth.BluetoothGattCharacteristic;

public class TxQueueItem {

    public enum TxQueueItemType {
        SubscribeCharacteristic,
        ReadCharacteristic,
        WriteCharacteristic
    }

    public BluetoothGattCharacteristic characteristic;
    public byte[] dataToWrite; // Only used for characteristic write
    public boolean enabled; // Only used for characteristic notification subscription
    public TxQueueItemType type;
}
