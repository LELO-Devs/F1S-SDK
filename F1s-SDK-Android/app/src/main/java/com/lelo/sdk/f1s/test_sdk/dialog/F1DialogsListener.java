package com.lelo.sdk.f1s.test_sdk.dialog;

public interface F1DialogsListener {

    void fromDialogsWriteVibratorSetting(byte speedTouch1, byte speedTouch2, byte speedTouch3, byte speedTouch4, byte speedTouch5, byte speedTouch6, byte speedTouch7, byte speedTouch8);

    void fromDialogWakeUp(boolean enable);

    void fromDialogClearUsageLog();

    void fromDialogStartMotor(byte speedMainMotor, byte speedVibratorMotor);

    void fromDialogStopMotor();

    void fromDialogTurnOffDevice();

    void fromDialogVerifyAccelerometer();

    /* MOTOR WORK ON TOUCH*/
    void fromDialogMotorWorkOnTouchEnableCapSensor(boolean enable);
    void fromDialogMotorWorkOnTouchEnableCapSensorAndResetDefualtSpeed();
}

