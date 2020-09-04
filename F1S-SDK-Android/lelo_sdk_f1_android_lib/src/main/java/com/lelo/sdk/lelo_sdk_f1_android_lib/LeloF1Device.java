package com.lelo.sdk.lelo_sdk_f1_android_lib;

import android.content.Context;
import android.support.annotation.NonNull;


public interface LeloF1Device {

    /*MUST BE CALLED TO INITIALIZE CLASS*/
    void setDeviceAddress(String deviceAddress);
    void setDeviceLunaResponseListener(LeloF1DeviceResponseListener listener);
    void bindBluetoothService(Context context);

    /* MUST BE CALLED ON onDestroy */
    void onDestroy(@NonNull Context context);
    void unregisterReceiver(@NonNull Context context);

    /* MUST BE CALLED ON onPause */
    void unbindBluetoothService(@NonNull Context context);

    /* MUST BE CALLED ON onPostResume */
    void registerReceiver(@NonNull Context context);

    /*DEVICE INFORMATION */
    void readDeviceName();
    void readManufactureName();
    void readModelNumber();
    void readHardwareRevision();
    void readFirmwareRevision();
    void readSoftwareRevision();
    void readSystemId();
    void readIEEERegulatoryCertificationDataList();
    void readPnpId();

    /*MAC ADDRESS*/
    void readMACAddress();

    /*SERIAL NUMBER*/
    void readSerialNumber();

    /*CHIP ID*/
    void readChipId();

    /*BATTERY PERCENTAGE*/
    void readBatteryPercentage();
    void startNotifyBatteryPercentage();
    void stopNotifyBatteryPercentage();

    /* MOTOR */
    void startMotor(byte speedMainMotor, byte speedVibratorMotor);
    void stopMotor();
    void readMotorState();
    void turnOffDevice();
    void verifyAccelerometer();

    /* CRUISE CONTROL*/
    void readCruiseControl();
    void enableCruiseControl(boolean enable);
    void enableCruiseControlAndResetToDefaultSpeedLevel();

    /*VIBRATOR SETTING*/
    void readVibratorSetting();
    void writeVibratorSetting(byte speedTouch1, byte speedTouch2, byte speedTouch3, byte speedTouch4, byte speedTouch5, byte speedTouch6, byte speedTouch7, byte speedTouch8);

    /*KEY STATE*/
    void readKeyState();
    void startNotifyKeyState();
    void stopNotifyKeyState();

    /*WAKE UP*/
    void enableWakeUp(boolean enable);
    void readWakeUp();

    /* HALL */
    void readHall();
    void startNotifyHall();
    void stopNotifyHall();

    /* DEPTH */
    void readDepth();
    void startNotifyDepth();
    void stopNotifyDepth();

    /*ACCELEROMETER*/
    void readAccelerometer();
    void startNotifyAccelerometer();
    void stopNotifyAccelerometer();

    /*PRESSURE*/
    void startNotifyPressureAndTemperature();
    void stopNotifyPressureAndTemperature();
    void readPressureAndTemperature();

    /*BUTTONS*/
    void readButtons();
    void startNotifyButtons();
    void stopNotifyButtons();

    /*USER LOG*/
    void readUseLog();
    void clearUseLog();

}
