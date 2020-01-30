package com.lelo.sdk.lelo_sdk_f1_android_lib;

public class LeloF1Constants {

    public static final String NAME_ADVERTISING = "F1s";
    public static final String DEVICE_NAME_CHARACTERISTIC = "00002a00-0000-1000-8000-00805f9b34fb"; //WRITE,READ
    public static final String MOTOR_CHARACTERISTIC = "0000fff1-0000-1000-8000-00805f9b34fb"; //WRITE,READ
    public static final String BATTERY_PERCENTAGE_CHARACTERISTIC = "00002a19-0000-1000-8000-00805f9b34fb";//NOTIFY
    public static final String MAC_ADDRESS_CHARACTERISTIC = "00000a06-0000-1000-8000-00805f9b34fb";//READ
    public static final String SERIAL_NUMBER_CHARACTERISTIC = "00000a05-0000-1000-8000-00805f9b34fb";//WRITE,READ
    public static final String CHIP_ID_CHARACTERISTIC = "00000a07-0000-1000-8000-00805f9b34fb";//READ
    public static final String USE_LOG_CHARACTERISTIC = "00000a04-0000-1000-8000-00805f9b34fb"; //WRITE,READ
    public static final String PRESSURE_AND_TEMPERATURE_CHARACTERISTIC = "00000a0a-0000-1000-8000-00805f9b34fb"; //READ
    public static final String DEPTH_CHARACTERISTIC = "00000a0b-0000-1000-8000-00805f9b34fb"; //READ
    public static final String ACCELEROMETER_CHARACTERISTIC = "00000a0c-0000-1000-8000-00805f9b34fb"; //READ
    public static final String KEY_STATE_CHARACTERISTIC = "00000a0f-0000-1000-8000-00805f9b34fb"; //READ
    public static final String VIBRATOR_SETTING_CHARACTERISTIC = "00000a0d-0000-1000-8000-00805f9b34fb"; //READ
    public static final String HALL_CHARACTERISTIC = "00000aa3-0000-1000-8000-00805f9b34fb"; //READ
    public static final String WAKE_UP_CHARACTERISTIC = "00000aa1-0000-1000-8000-00805f9b34fb"; //READ
    public static final String CRUISE_CONTROL_CHARACTERISTIC = "00000aa5-0000-1000-8000-00805f9b34fb"; //READ
    public static final String BUTTONS_CHARACTERISTIC = "00000aa4-0000-1000-8000-00805f9b34fb"; //READ

    //DEVICE INFORMATION 0x180A PRIMARY SERVICE
    public static final String SYSTEM_ID_CHARACTERISTIC = "00002a23-0000-1000-8000-00805f9b34fb"; //READ
    public static final String MODEL_NUMBER_CHARACTERISTIC = "00002a24-0000-1000-8000-00805f9b34fb"; //READ
    public static final String FIRMWARE_REVISION_CHARACTERISTIC = "00002a26-0000-1000-8000-00805f9b34fb"; //READ
    public static final String HARDWARE_REVISION_CHARACTERISTIC = "00002a27-0000-1000-8000-00805f9b34fb"; //READ
    public static final String SOFTWARE_REVISION_CHARACTERISTIC = "00002a28-0000-1000-8000-00805f9b34fb"; //READ
    public static final String MANUFACTURER_NAME_CHARACTERISTIC = "00002a29-0000-1000-8000-00805f9b34fb"; //READ
    public static final String IEEE_RCDL_CHARACTERISTIC = "00002a2a-0000-1000-8000-00805f9b34fb"; //READ
    public static final String PNP_ID_CHARACTERISTIC = "00002a50-0000-1000-8000-00805f9b34fb"; //READ

    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";//READ
    public static String USER_DESCRIPTION_CHARACTERISTIC = "00002901-0000-1000-8000-00805f9b34fb";//READ


    /**
     * MOTOR COMMAND
     **/

    private static final int COMMAND_HEADER_MOTOR_1 = 0x01;
    private static final int MOTOR_STOP = 0xff;
    private static final int MOTOR_DEVICE_SHUT_DOWN = 0xfa;
    private static final int VERIFY_ACCELEROMETER_BYTE1 = 0xff;
    private static final int VERIFY_ACCELEROMETER_BYTE2 = 0xff;
    private static final int VERIFY_ACCELEROMETER_BYTE3 = 0xff;

    public static byte[] buildMotorStartCommand(byte speedMainMotor, byte speedVibratorMotor) {
        byte[] buildBytes = new byte[3];
        buildBytes[0] = COMMAND_HEADER_MOTOR_1;
        buildBytes[1] = speedMainMotor;
        buildBytes[2] = speedVibratorMotor;
        return buildBytes;
    }

    public static byte[] buildMotorStopCommand() {
        byte[] buildBytes = new byte[3];
        buildBytes[0] = COMMAND_HEADER_MOTOR_1;
        buildBytes[1] = (byte) MOTOR_STOP;
        return buildBytes;
    }

    public static byte[] buildMotorDeviceTurnOffCommand() {
        byte[] buildBytes = new byte[3];
        buildBytes[0] = COMMAND_HEADER_MOTOR_1;
        buildBytes[1] = (byte) MOTOR_DEVICE_SHUT_DOWN;
        return buildBytes;
    }

    public static byte[] buildVerifyAccelerometerCommand() {
        byte[] buildBytes = new byte[3];
        buildBytes[0] = (byte) VERIFY_ACCELEROMETER_BYTE1;
        buildBytes[1] = (byte) VERIFY_ACCELEROMETER_BYTE2;
        buildBytes[2] = (byte) VERIFY_ACCELEROMETER_BYTE3;
        return buildBytes;
    }


    /* USAGE LOG */

    private static final int USER_RECORED_CLEAR_COMMAND = 0xee;

    public static byte[] buildUserRecordClearCommand() {
        byte[] buildBytes = new byte[1];
        buildBytes[0] = (byte) USER_RECORED_CLEAR_COMMAND;
        return buildBytes;
    }

    /*
    * VIBRATOR SETTING
     */

    public static byte[] buildVibratorSettingCommand(byte speedTouch1, byte speedTouch2, byte speedTouch3, byte speedTouch4, byte speedTouch5, byte speedTouch6, byte speedTouch7, byte speedTouch8){
        byte[] buildBytes = new byte[8];
        buildBytes[0] = speedTouch1;
        buildBytes[1] = speedTouch2;
        buildBytes[2] = speedTouch3;
        buildBytes[3] = speedTouch4;
        buildBytes[4] = speedTouch5;
        buildBytes[5] = speedTouch6;
        buildBytes[6] = speedTouch7;
        buildBytes[7] = speedTouch8;
        return buildBytes;
    }

    /* WAKE UP */
    private static final int COMMAND_WAKE_UP_DISABLE = 0x00;
    private static final int COMMAND_WAKE_UP_ENABLE = 0x01;

    public static byte[] buildEnableWakeUpCommand(boolean enable){
        byte[] buildBytes = new byte[1];
        if(enable){
            buildBytes[0] = COMMAND_WAKE_UP_ENABLE;
        }else{
            buildBytes[0] = COMMAND_WAKE_UP_DISABLE;
        }
        return buildBytes;
    }


    /* MOTOR WORK ON TOUCH */

    private static final int COMMAND_MOTOR_WORK_ON_TOUCH_DISABLE_CAP_SENSOR = 0x00;
    private static final int COMMAND_MOTOR_WORK_ON_TOUCH_ENABLE_CAP_SENSOR = 0x01;
    private static final int COMMAND_MOTOR_WORK_ON_TOUCH_ENABLE_CAP_SENSOR_AND_RESET_TO_DEFAULT_SPEED = 0x02;

    public static byte[] buildEnableCapSensorControlSpeedCommand(boolean enable){
        byte[] buildBytes = new byte[1];
        if(enable){
            buildBytes[0] = COMMAND_MOTOR_WORK_ON_TOUCH_ENABLE_CAP_SENSOR;
        }else{
            buildBytes[0] = COMMAND_MOTOR_WORK_ON_TOUCH_DISABLE_CAP_SENSOR;
        }
        return buildBytes;
    }

    public static byte[] buildEnableCapSensorControlSpeedAndResetToDefaultSpeedLevelCommand(){
        byte[] buildBytes = new byte[1];
        buildBytes[0] = COMMAND_MOTOR_WORK_ON_TOUCH_ENABLE_CAP_SENSOR_AND_RESET_TO_DEFAULT_SPEED;
        return buildBytes;
    }


}
