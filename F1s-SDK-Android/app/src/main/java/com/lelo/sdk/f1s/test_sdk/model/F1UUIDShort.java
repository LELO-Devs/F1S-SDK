package com.lelo.sdk.f1s.test_sdk.model;

public class F1UUIDShort {
    public static final String DEVICE_NAME_UUID_SHORT="2a00";
    public static final String MOTOR_CONTROL_UUID_SHORT="fff1";
    public static final String BATTERY_PERCENTAGE_UUID_SHORT ="2a19";
    public static final String MAC_ADDRESS_UUID_SHORT="0a06";
    public static final String SERIAL_NUMBER_UUID_SHORT="0a05";
    public static final String CHIP_ID_UUID_SHORT="0a07";
    public static final String USAGE_LOG_UUID_SHORT="0a04";
    public static final String SYSTEM_ID_UUID_SHORT="2a23";
    public static final String MODEL_NUMBER_UUID_SHORT="2a24";
    public static final String FIRMWARE_UUID_SHORT="2a26";
    public static final String HARDWARE_UUID_SHORT="2a27";
    public static final String SOFTWARE_UUID_SHORT="2a28";
    public static final String MANUFACTURER_NAME_UUID_SHORT="2a29";
    public static final String IEEE_UUID_SHORT="2a2a";
    public static final String PNP_ID_UUID_SHORT="2a50";
    public static final String KEY_STATE_UUID_SHORT="0a0f";
    public static final String HALL_UUID_SHORT ="0aa3";
    public static final String PRESSURE_UUID_SHORT ="0a0a";
    public static final String LENGTH_UUID_SHORT="0a0b";
    public static final String ACCELETROMETER_UUID_SHORT="0a0c";
    public static final String VIBRATOR_SETTINGS_UUID_SHORT="0a0d";
    public static final String WAKE_UP_UUID_SHORT="0aa1";
    public static final String MOTOR_WORK_ON_TOUCH_UUID_SHORT="0aa5";
    public static final String BUTTON_UUID_SHORT="0aa4";

    public static String getNameFromUuid(String uuidShort){
        String name="";
        switch (uuidShort) {
            case DEVICE_NAME_UUID_SHORT:
                name = "Device Name";
                break;
            case MOTOR_CONTROL_UUID_SHORT:
                name = "Motor Control";
                break;
            case BATTERY_PERCENTAGE_UUID_SHORT:
                name = "Battery Percentage";
                break;
            case MAC_ADDRESS_UUID_SHORT:
                name = "MAC Address";
                break;
            case SERIAL_NUMBER_UUID_SHORT:
                name = "Serial Number";
                break;
            case CHIP_ID_UUID_SHORT:
                name = "Chip Id";
                break;
            case USAGE_LOG_UUID_SHORT:
                name = "Use Log";
                break;
            case PRESSURE_UUID_SHORT:
                name = "Pressure/Temperature";
                break;
            case LENGTH_UUID_SHORT:
                name = "Depth";
                break;
            case ACCELETROMETER_UUID_SHORT:
                name = "Accelerometer";
                break;
            case SYSTEM_ID_UUID_SHORT:
                name = "System Id";
                break;
            case MODEL_NUMBER_UUID_SHORT:
                name = "Model Number";
                break;
            case FIRMWARE_UUID_SHORT:
                name = "Firmware Revision";
                break;
            case HARDWARE_UUID_SHORT:
                name = "Hardware Revision";
                break;
            case SOFTWARE_UUID_SHORT:
                name = "Software Revision";
                break;
            case MANUFACTURER_NAME_UUID_SHORT:
                name = "Manufacturer Name";
                break;
            case IEEE_UUID_SHORT:
                name = "IEEE RCDL";
                break;
            case PNP_ID_UUID_SHORT:
                name = "Pnp Id";
                break;
            case VIBRATOR_SETTINGS_UUID_SHORT:
                name = "Vibrator Setting";
                break;
            case KEY_STATE_UUID_SHORT:
                name = "Key State";
                break;
            case HALL_UUID_SHORT:
                name = "Hall";
                break;
            case WAKE_UP_UUID_SHORT:
                name = "Wake Up";
                break;
            case MOTOR_WORK_ON_TOUCH_UUID_SHORT:
                name = "Cruise Control";
                break;
            case BUTTON_UUID_SHORT:
                name = "Buttons";
                break;
        }
        return name;
    }
}
