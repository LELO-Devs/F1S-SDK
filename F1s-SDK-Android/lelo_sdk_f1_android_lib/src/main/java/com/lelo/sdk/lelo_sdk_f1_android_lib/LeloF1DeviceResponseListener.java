package com.lelo.sdk.lelo_sdk_f1_android_lib;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.List;

public interface LeloF1DeviceResponseListener {

    public static final String TYPE_DEVICE_NAME="device_name";
    public static final String TYPE_MANUFACTURER_NAME ="manufacturer_name";
    public static final String TYPE_MODEL_NUMBER ="model_number";
    public static final String TYPE_HARDWARE_REVISION ="hardware_revision";
    public static final String TYPE_FIRMWARE_REVISION ="firmware_revision";
    public static final String TYPE_SOFTWARE_REVISION ="softwear_revision";
    public static final String TYPE_SYSTEM_ID ="system_id";
    public static final String TYPE_IEEE_RCDL ="ieee_rcdl";
    public static final String TYPE_PNP_ID ="pnp_id";
    public static final String TYPE_MAC_ADDRESS="mac_address";
    public static final String TYPE_CHIP_ID ="chip_id";
    public static final String TYPE_SERIAL_NUMBER ="serial_number";
    public static final String TYPE_USE_LOG ="use_log";
    public static final String TYPE_BATTERY_PERCENTAGE ="battery_percentage";
    public static final String TYPE_MOTOR="motor";
    public static final String TYPE_HALL="hall";
    public static final String TYPE_PRESSURE_AND_TEMPERATURE ="pressure_and_temperature";
    public static final String TYPE_DEPTH ="depth";
    public static final String TYPE_ACCELEROMETER ="accelerometer";
    public static final String TYPE_VIBRATOR_SETTING="vibrator_setting";
    public static final String TYPE_KEY_STATE="key_state";
    public static final String TYPE_WAKE_UP="wake_up";
    public static final String TYPE_BUTTONS ="buttons";
    public static final String TYPE_CRUISE_CONTROL ="cruise_control";


    /**
     * DEVICE RETURN VALUE
     */
    void returnDeviceValue(String valueType, String valueData);

    /**
     * DEVICE CONNECTION
     */
    void onDeviceConnected();
    void onDeviceDisconnected();
    void onBluetoothServiceNotInitialized();

    /**
     * SERVICES DISCOVERED
     */
    void onCharacteristicDiscovered(List<BluetoothGattCharacteristic> gattCharacteristics);
}
