package com.lelo.sdk.lelo_sdk_f1_android_lib;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import static com.lelo.sdk.lelo_sdk_f1_android_lib.LeloF1Constants.buildEnableCapSensorControlSpeedAndResetToDefaultSpeedLevelCommand;
import static com.lelo.sdk.lelo_sdk_f1_android_lib.LeloF1Constants.buildEnableCapSensorControlSpeedCommand;


public class LeloF1DeviceImpl implements LeloF1Device{

    private final String TAG = LeloF1DeviceImpl.class.getSimpleName();

    private LeloF1DeviceResponseListener mResponse;
    private BluetoothLeServiceLeloF1 mBluetoothLeService;
    private String mDeviceAddress;
    private BluetoothGattCharacteristic mMotorCharacteristics;
    private BluetoothGattCharacteristic mBatteryPercentageCharacteristics;
    private BluetoothGattCharacteristic mMacAddressCharacteristics;
    private BluetoothGattCharacteristic mSerialNumberCharacteristics;
    private BluetoothGattCharacteristic mUseLogCharacteristics;
    private BluetoothGattCharacteristic mChipIdCharacteristics;
    private BluetoothGattCharacteristic mManufacturerNameCharacteristics;
    private BluetoothGattCharacteristic mModelNumberCharacteristics;
    private BluetoothGattCharacteristic mHardwareRevisionCharacteristics;
    private BluetoothGattCharacteristic mFirmwareRevisionCharacteristics;
    private BluetoothGattCharacteristic mSoftwareRevisionCharacteristics;
    private BluetoothGattCharacteristic mSystemIdCharacteristics;
    private BluetoothGattCharacteristic mIEEERCDLCharacteristics;
    private BluetoothGattCharacteristic mPnpIdCharacteristics;
    private BluetoothGattCharacteristic mAccelerometerCharacteristics;
    private BluetoothGattCharacteristic mDepthCharacteristics;
    private BluetoothGattCharacteristic mHallCharacteristics;
    private BluetoothGattCharacteristic mPressureAndTemperatureCharacteristics;
    private BluetoothGattCharacteristic mDeviceNameCharacteristics;
    private BluetoothGattCharacteristic mKeyStateCharacteristics;
    private BluetoothGattCharacteristic mVibratorSettingCharacteristics;
    private BluetoothGattCharacteristic mWakeUpCharacteristics;
    private BluetoothGattCharacteristic mCruiseControlCharacteristics;
    private BluetoothGattCharacteristic mButtonsCharacteristics;


    private boolean mIsConnected=false;

    public void setDeviceLunaResponseListener(LeloF1DeviceResponseListener listener) {
        this.mResponse = listener;
    }

    public void onDestroy(@NonNull Context context) {
        if(mResponse!=null) mResponse=null;
        if (mBluetoothLeService != null) {
            mBluetoothLeService.disconnect();
        }
       
    }

    public void registerReceiver(@NonNull Context context) {
        context.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);

        }
    }

    public void unregisterReceiver(@NonNull Context context) {
        context.unregisterReceiver(mGattUpdateReceiver);
    }

    public void bindBluetoothService(@NonNull Context context) {
        Intent gattServiceIntent = new Intent(context, BluetoothLeServiceLeloF1.class);
        context.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbindBluetoothService(@NonNull Context context) {
        if (mServiceConnection != null) {
            context.unbindService(mServiceConnection);
        }
    }

    @Override
    public void readDeviceName() {
        Log.d(TAG, "readDeviceName ");
        if (mBluetoothLeService != null && mDeviceNameCharacteristics != null) {
            mBluetoothLeService.queueReadCharacteristicValue(mDeviceNameCharacteristics);
        }
    }

    public void setDeviceAddress(String deviceAddress) {
        this.mDeviceAddress = deviceAddress;
    }


    @Override
    public void startMotor(byte speedMainMotor, byte speedVibratorMotor) {
        Log.d(TAG, "startMotor speedMainMotor "+speedMainMotor+" speedVibratorMotor "+speedVibratorMotor);
        if (mBluetoothLeService != null && mMotorCharacteristics != null) {
            mBluetoothLeService.queueWriteDataToCharacteristic(mMotorCharacteristics, LeloF1Constants.buildMotorStartCommand(speedMainMotor, speedVibratorMotor));
        }
    }


    @Override
    public void stopMotor() {
       Log.d(TAG, "stopMotor");
        if (mBluetoothLeService != null && mMotorCharacteristics != null) {
            mBluetoothLeService.queueWriteDataToCharacteristic(mMotorCharacteristics, LeloF1Constants.buildMotorStopCommand());
        }
    }

    @Override
    public void turnOffDevice() {
       Log.d(TAG, "turnOffDevice");
        if (mBluetoothLeService != null && mMotorCharacteristics != null) {
            mBluetoothLeService.queueWriteDataToCharacteristic(mMotorCharacteristics, LeloF1Constants.buildMotorDeviceTurnOffCommand());
        }
    }

    @Override
    public void verifyAccelerometer() {
        Log.d(TAG, "verifyAccelerometer");
        if (mBluetoothLeService != null && mMotorCharacteristics != null) {
            mBluetoothLeService.queueWriteDataToCharacteristic(mMotorCharacteristics, LeloF1Constants.buildVerifyAccelerometerCommand());
        }
    }

    @Override
    public void readMotorState() {
       Log.d(TAG, "readMotorState ");
        if (mBluetoothLeService != null && mMotorCharacteristics != null) {
            mBluetoothLeService.queueReadCharacteristicValue(mMotorCharacteristics);
        }
    }


    @Override
    public void readBatteryPercentage() {
        Log.d(TAG, "readBatteryPercentage ");
        if (mBluetoothLeService != null && mBatteryPercentageCharacteristics != null) {
            mBluetoothLeService.queueReadCharacteristicValue(mBatteryPercentageCharacteristics);
        }
    }

    @Override
    public void startNotifyBatteryPercentage() {
        Log.d(TAG, "startNotifyBatteryPercentage ");
        if (mBluetoothLeService != null && mBatteryPercentageCharacteristics != null) {
            mBluetoothLeService.queueSetNotificationForCharacteristic(mBatteryPercentageCharacteristics, true);
        }
    }

    @Override
    public void stopNotifyBatteryPercentage() {
        Log.d(TAG, "stopNotifyBatteryPercentage ");
        if (mBluetoothLeService != null && mBatteryPercentageCharacteristics != null) {
            mBluetoothLeService.queueSetNotificationForCharacteristic(mBatteryPercentageCharacteristics, false);
        }
    }



    @Override
    public void readMACAddress() {
        Log.d(TAG, "readMACAddress");
        if (mBluetoothLeService != null && mMacAddressCharacteristics != null) {
            mBluetoothLeService.queueReadCharacteristicValue(mMacAddressCharacteristics);
        }
    }

    @Override
    public void readSerialNumber() {
        Log.d(TAG, "readSerialNumber");
        if (mBluetoothLeService != null && mSerialNumberCharacteristics != null) {
            mBluetoothLeService.queueReadCharacteristicValue(mSerialNumberCharacteristics);
        }
    }

    @Override
    public void readChipId() {
        Log.d(TAG, "readChipId ");
        if (mBluetoothLeService != null && mChipIdCharacteristics != null) {
            mBluetoothLeService.queueReadCharacteristicValue(mChipIdCharacteristics);
        }
    }
    

    @Override
    public void readManufactureName() {
       Log.d(TAG, "readManufactureName ");
        if (mBluetoothLeService != null && mManufacturerNameCharacteristics != null) {
            mBluetoothLeService.queueReadCharacteristicValue(mManufacturerNameCharacteristics);
        }
    }

    @Override
    public void readModelNumber() {
       Log.d(TAG, "readModelNumber ");
        if (mBluetoothLeService != null && mModelNumberCharacteristics != null) {
            mBluetoothLeService.queueReadCharacteristicValue(mModelNumberCharacteristics);
        }
    }

    @Override
    public void readHardwareRevision() {
       Log.d(TAG, "readHardwareRevision ");
        if (mBluetoothLeService != null && mHardwareRevisionCharacteristics != null) {
            mBluetoothLeService.queueReadCharacteristicValue(mHardwareRevisionCharacteristics);
        }
    }

    @Override
    public void readFirmwareRevision() {
       Log.d(TAG, "readFirmwareRevision ");
        if (mBluetoothLeService != null && mFirmwareRevisionCharacteristics != null) {
            mBluetoothLeService.queueReadCharacteristicValue(mFirmwareRevisionCharacteristics);
        }
    }

    @Override
    public void readSoftwareRevision() {
       Log.d(TAG, "readSoftwareRevision ");
        if (mBluetoothLeService != null && mSoftwareRevisionCharacteristics != null) {
            mBluetoothLeService.queueReadCharacteristicValue(mSoftwareRevisionCharacteristics);
        }
    }

    @Override
    public void readSystemId() {
       Log.d(TAG, "readSystemId ");
        if (mBluetoothLeService != null && mSystemIdCharacteristics != null) {
           Log.d("readSystemId", "" + mSystemIdCharacteristics.getUuid());
            mBluetoothLeService.queueReadCharacteristicValue(mSystemIdCharacteristics);
        }
    }

    @Override
    public void readIEEERegulatoryCertificationDataList() {
       Log.d(TAG, "readIEEERegulatoryCertificationDataList ");
        if (mBluetoothLeService != null && mIEEERCDLCharacteristics != null) {
            mBluetoothLeService.queueReadCharacteristicValue(mIEEERCDLCharacteristics);
        }
    }

    @Override
    public void readPnpId() {
       Log.d(TAG, "readPnpId ");
        if (mBluetoothLeService != null && mPnpIdCharacteristics != null) {
            mBluetoothLeService.queueReadCharacteristicValue(mPnpIdCharacteristics);
        }
    }

    @Override
    public void readUseLog() {
        Log.d(TAG, "readUseLog  ");
        if (mBluetoothLeService != null && mUseLogCharacteristics != null) {
            mBluetoothLeService.queueReadCharacteristicValue(mUseLogCharacteristics);
        }
    }

    @Override
    public void clearUseLog() {
        Log.d(TAG, "clearUseLog  ");
        if (mBluetoothLeService != null && mUseLogCharacteristics != null) {
            mBluetoothLeService.queueWriteDataToCharacteristic(mUseLogCharacteristics, LeloF1Constants.buildUserRecordClearCommand());
        }
    }

    @Override
    public void startNotifyAccelerometer() {
        Log.d(TAG, "startNotifyAccelerometer ");
        if (mBluetoothLeService != null && mAccelerometerCharacteristics != null) {
            mBluetoothLeService.queueSetNotificationForCharacteristic(mAccelerometerCharacteristics, true);
        }
    }

    @Override
    public void stopNotifyAccelerometer() {
        Log.d(TAG, "stopNotifyAccelerometer");
        if (mBluetoothLeService != null && mAccelerometerCharacteristics != null) {
            mBluetoothLeService.queueSetNotificationForCharacteristic(mAccelerometerCharacteristics, false);
        }
    }

    @Override
    public void readAccelerometer() {
        Log.d(TAG, "readAccelerometer ");
        if (mBluetoothLeService != null && mAccelerometerCharacteristics != null) {
            mBluetoothLeService.queueReadCharacteristicValue(mAccelerometerCharacteristics);
        }
    }

    @Override
    public void startNotifyDepth() {
        Log.d(TAG, "startNotifyDepth");
        if (mBluetoothLeService != null && mDepthCharacteristics != null) {
            mBluetoothLeService.queueSetNotificationForCharacteristic(mDepthCharacteristics, true);
        }
    }

    @Override
    public void stopNotifyDepth() {
        Log.d(TAG, "stopNotifyDepth");
        if (mBluetoothLeService != null && mDepthCharacteristics != null) {
            mBluetoothLeService.queueSetNotificationForCharacteristic(mDepthCharacteristics, false);
        }
    }

    @Override
    public void readDepth() {
        Log.d(TAG, "readDepth");
        if (mBluetoothLeService != null && mDepthCharacteristics != null) {
            mBluetoothLeService.queueReadCharacteristicValue(mDepthCharacteristics);
        }
    }

    @Override
    public void startNotifyHall() {
        Log.d(TAG, "startNotifyHall");
        if (mBluetoothLeService != null && mHallCharacteristics != null) {
            mBluetoothLeService.queueSetNotificationForCharacteristic(mHallCharacteristics, true);
        }
    }

    @Override
    public void stopNotifyHall() {
        Log.d(TAG, "stopNotifyHall");
        if (mBluetoothLeService != null && mHallCharacteristics != null) {
            mBluetoothLeService.queueSetNotificationForCharacteristic(mHallCharacteristics, false);
        }
    }

    @Override
    public void readHall() {
        Log.d(TAG, "readHall");
        if (mBluetoothLeService != null && mHallCharacteristics != null) {
            mBluetoothLeService.queueReadCharacteristicValue(mHallCharacteristics);
        }
    }
    @Override
    public void startNotifyPressureAndTemperature() {
        Log.d(TAG, "startNotifyPressureAndTemperature");
        if (mBluetoothLeService != null && mPressureAndTemperatureCharacteristics != null) {
            mBluetoothLeService.queueSetNotificationForCharacteristic(mPressureAndTemperatureCharacteristics, true);
        }
    }

    @Override
    public void stopNotifyPressureAndTemperature() {
        Log.d(TAG, "stopNotifyPressureAndTemperature");
        if (mBluetoothLeService != null && mPressureAndTemperatureCharacteristics != null) {
            mBluetoothLeService.queueSetNotificationForCharacteristic(mPressureAndTemperatureCharacteristics, false);
        }
    }

    @Override
    public void readPressureAndTemperature() {
        Log.d(TAG, "readPressureAndTemperature");
        if (mBluetoothLeService != null && mPressureAndTemperatureCharacteristics != null) {
            mBluetoothLeService.queueReadCharacteristicValue(mPressureAndTemperatureCharacteristics);
        }
    }

    @Override
    public void readKeyState() {
        Log.d(TAG, "readKeyState");
        if (mBluetoothLeService != null && mKeyStateCharacteristics != null) {
            mBluetoothLeService.queueReadCharacteristicValue(mKeyStateCharacteristics);
        }
    }

    @Override
    public void startNotifyKeyState() {
        Log.d(TAG, "startNotifyKeyState");
        if (mBluetoothLeService != null && mKeyStateCharacteristics != null) {
            mBluetoothLeService.queueSetNotificationForCharacteristic(mKeyStateCharacteristics, true);
        }
    }

    @Override
    public void stopNotifyKeyState() {
        Log.d(TAG, "stopNotifyKeyState");
        if (mBluetoothLeService != null && mKeyStateCharacteristics != null) {
            mBluetoothLeService.queueSetNotificationForCharacteristic(mKeyStateCharacteristics, false);
        }
    }

    @Override
    public void readVibratorSetting() {
        Log.d(TAG, "readVibratorSetting");
        if (mBluetoothLeService != null && mVibratorSettingCharacteristics != null) {
            mBluetoothLeService.queueReadCharacteristicValue(mVibratorSettingCharacteristics);
        }
    }

    @Override
    public void writeVibratorSetting(byte speedTouch1, byte speedTouch2, byte speedTouch3, byte speedTouch4, byte speedTouch5, byte speedTouch6, byte speedTouch7, byte speedTouch8) {
        Log.d(TAG, "writeVibratorSetting");
        if (mBluetoothLeService != null && mVibratorSettingCharacteristics != null) {
            mBluetoothLeService.queueWriteDataToCharacteristic(mVibratorSettingCharacteristics,LeloF1Constants.buildVibratorSettingCommand(speedTouch1, speedTouch2, speedTouch3, speedTouch4, speedTouch5, speedTouch6, speedTouch7, speedTouch8));
        }
    }



    @Override
    public void enableWakeUp(boolean enable) {
        Log.d(TAG, "enableWakeUp enable "+enable);
        if (mBluetoothLeService != null && mWakeUpCharacteristics!= null) {
            mBluetoothLeService.queueWriteDataToCharacteristic( mWakeUpCharacteristics,LeloF1Constants.buildEnableWakeUpCommand(enable));
        }
    }

    @Override
    public void readWakeUp() {
        Log.d(TAG, "readWakeUp");
        if (mBluetoothLeService != null && mWakeUpCharacteristics!= null) {
            mBluetoothLeService.queueReadCharacteristicValue(mWakeUpCharacteristics);
        }
    }

    @Override
    public void readCruiseControl() {
        Log.d(TAG, "readCruiseControl");
        if (mBluetoothLeService != null && mCruiseControlCharacteristics != null) {
            mBluetoothLeService.queueReadCharacteristicValue(mCruiseControlCharacteristics);
        }
    }

    @Override
    public void enableCruiseControl(boolean enable) {
        Log.d(TAG, "enableCruiseControl " + enable);
        if (mBluetoothLeService != null && mCruiseControlCharacteristics != null) {
            mBluetoothLeService.queueWriteDataToCharacteristic(mCruiseControlCharacteristics, buildEnableCapSensorControlSpeedCommand(enable));
        }
    }

    @Override
    public void enableCruiseControlAndResetToDefaultSpeedLevel() {
        Log.d(TAG, "enableCruiseControlAndResetToDefaultSpeedLevel ");
        if (mBluetoothLeService != null && mCruiseControlCharacteristics != null) {
            mBluetoothLeService.queueWriteDataToCharacteristic(mCruiseControlCharacteristics, buildEnableCapSensorControlSpeedAndResetToDefaultSpeedLevelCommand());
        }
    }

    @Override
    public void readButtons() {
        Log.d(TAG, "readButtons");
        if (mBluetoothLeService != null && mButtonsCharacteristics != null) {
            mBluetoothLeService.queueReadCharacteristicValue(mButtonsCharacteristics);
        }
    }

    @Override
    public void startNotifyButtons() {
        Log.d(TAG, "startNotifyButtons");
        if (mBluetoothLeService != null && mButtonsCharacteristics != null) {
            mBluetoothLeService.queueSetNotificationForCharacteristic(mButtonsCharacteristics, true);
        }
    }

    @Override
    public void stopNotifyButtons() {
        Log.d(TAG, "stopNotifyButtons");
        if (mBluetoothLeService != null && mButtonsCharacteristics != null) {
            mBluetoothLeService.queueSetNotificationForCharacteristic(mButtonsCharacteristics, false);
        }
    }

    public void manuallyConnectDevice() {
        if (mBluetoothLeService == null || !mBluetoothLeService.initialize()) {
            mResponse.onBluetoothServiceNotInitialized();
            return;
        }

        if (!TextUtils.isEmpty(mDeviceAddress)) {
            mBluetoothLeService.connect(mDeviceAddress);
        }
    }

  
    public void manuallyDisconnectDevice() {
        if (mBluetoothLeService == null || !mBluetoothLeService.initialize()) {
            mResponse.onBluetoothServiceNotInitialized();
            return;
        }

        mBluetoothLeService.disconnect();
    }
  
    public boolean isBluetoothServiceConnected() {
        return mIsConnected;
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

      
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeServiceLeloF1.LocalBinder) service).getService();
            if (mBluetoothLeService == null || !mBluetoothLeService.initialize()) {
                mResponse.onBluetoothServiceNotInitialized();
                return;
            }

            if (!TextUtils.isEmpty(mDeviceAddress)) {
                mBluetoothLeService.connect(mDeviceAddress);
            }


        }

      
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
      
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeServiceLeloF1.ACTION_GATT_CONNECTED.equals(action)) {
                mIsConnected = true;
                mResponse.onDeviceConnected();
               Log.d(TAG, "ACTION_GATT_CONNECTED");
            } else if (BluetoothLeServiceLeloF1.ACTION_GATT_DISCONNECTED.equals(action)) {
                mIsConnected = false;
                mResponse.onDeviceDisconnected();
               Log.d(TAG, "ACTION_GATT_DISCONNECTED");
            } else if (BluetoothLeServiceLeloF1.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
               Log.d(TAG, "ACTION_GATT_SERVICES_DISCOVERED");
                //a little delay to give time to the bluetooth stack
                queryGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeServiceLeloF1.ACTION_DATA_AVAILABLE.equals(action)) {
                String data = intent.getStringExtra(BluetoothLeServiceLeloF1.EXTRA_DATA);
                String uuid = intent.getStringExtra(BluetoothLeServiceLeloF1.EXTRA_DATA_UUID);
                switch (uuid) {
                    case LeloF1Constants.MOTOR_CHARACTERISTIC:
                        mResponse.returnDeviceValue(LeloF1DeviceResponseListener.TYPE_MOTOR, data);
                        break;
                    case LeloF1Constants.BATTERY_PERCENTAGE_CHARACTERISTIC:
                        mResponse.returnDeviceValue(LeloF1DeviceResponseListener.TYPE_BATTERY_PERCENTAGE, data);
                        break;
                    case LeloF1Constants.MAC_ADDRESS_CHARACTERISTIC:
                        mResponse.returnDeviceValue(LeloF1DeviceResponseListener.TYPE_MAC_ADDRESS, data);
                        break;
                    case LeloF1Constants.SERIAL_NUMBER_CHARACTERISTIC:
                        mResponse.returnDeviceValue(LeloF1DeviceResponseListener.TYPE_SERIAL_NUMBER, data);
                        break;
                    case LeloF1Constants.USE_LOG_CHARACTERISTIC:
                        mResponse.returnDeviceValue(LeloF1DeviceResponseListener.TYPE_USE_LOG, data);
                        break;
                    case LeloF1Constants.CHIP_ID_CHARACTERISTIC:
                        mResponse.returnDeviceValue(LeloF1DeviceResponseListener.TYPE_CHIP_ID, data);
                        break;
                    case LeloF1Constants.MANUFACTURER_NAME_CHARACTERISTIC:
                        mResponse.returnDeviceValue(LeloF1DeviceResponseListener.TYPE_MANUFACTURER_NAME, data);
                        break;
                    case LeloF1Constants.MODEL_NUMBER_CHARACTERISTIC:
                        mResponse.returnDeviceValue(LeloF1DeviceResponseListener.TYPE_MODEL_NUMBER, data);
                        break;
                    case LeloF1Constants.HARDWARE_REVISION_CHARACTERISTIC:
                        mResponse.returnDeviceValue(LeloF1DeviceResponseListener.TYPE_HARDWARE_REVISION, data);
                        break;
                    case LeloF1Constants.FIRMWARE_REVISION_CHARACTERISTIC:
                        mResponse.returnDeviceValue(LeloF1DeviceResponseListener.TYPE_FIRMWARE_REVISION, data);
                        break;
                    case LeloF1Constants.SOFTWARE_REVISION_CHARACTERISTIC:
                        mResponse.returnDeviceValue(LeloF1DeviceResponseListener.TYPE_SOFTWARE_REVISION, data);
                        break;
                    case LeloF1Constants.SYSTEM_ID_CHARACTERISTIC:
                        mResponse.returnDeviceValue(LeloF1DeviceResponseListener.TYPE_SYSTEM_ID, data);
                        break;
                    case LeloF1Constants.IEEE_RCDL_CHARACTERISTIC:
                        mResponse.returnDeviceValue(LeloF1DeviceResponseListener.TYPE_IEEE_RCDL, data);
                        break;
                    case LeloF1Constants.PNP_ID_CHARACTERISTIC:
                        mResponse.returnDeviceValue(LeloF1DeviceResponseListener.TYPE_PNP_ID, data);
                        break;
                    case LeloF1Constants.ACCELEROMETER_CHARACTERISTIC:
                        mResponse.returnDeviceValue(LeloF1DeviceResponseListener.TYPE_ACCELEROMETER, data);
                        break;
                    case LeloF1Constants.DEPTH_CHARACTERISTIC:
                        mResponse.returnDeviceValue(LeloF1DeviceResponseListener.TYPE_DEPTH, data);
                        break;
                    case LeloF1Constants.PRESSURE_AND_TEMPERATURE_CHARACTERISTIC:
                        mResponse.returnDeviceValue(LeloF1DeviceResponseListener.TYPE_PRESSURE_AND_TEMPERATURE, data);
                        break;
                    case LeloF1Constants.DEVICE_NAME_CHARACTERISTIC:
                        mResponse.returnDeviceValue(LeloF1DeviceResponseListener.TYPE_DEVICE_NAME,data);
                        break;
                    case LeloF1Constants.KEY_STATE_CHARACTERISTIC:
                        mResponse.returnDeviceValue(LeloF1DeviceResponseListener.TYPE_KEY_STATE,data);
                        break;
                    case LeloF1Constants.VIBRATOR_SETTING_CHARACTERISTIC:
                        mResponse.returnDeviceValue(LeloF1DeviceResponseListener.TYPE_VIBRATOR_SETTING,data);
                        break;
                    case LeloF1Constants.HALL_CHARACTERISTIC:
                        mResponse.returnDeviceValue(LeloF1DeviceResponseListener.TYPE_HALL,data);
                        break;
                    case LeloF1Constants.WAKE_UP_CHARACTERISTIC:
                        mResponse.returnDeviceValue(LeloF1DeviceResponseListener.TYPE_WAKE_UP,data);
                        break;
                    case LeloF1Constants.CRUISE_CONTROL_CHARACTERISTIC:
                        mResponse.returnDeviceValue(LeloF1DeviceResponseListener.TYPE_CRUISE_CONTROL,data);
                        break;
                    case LeloF1Constants.BUTTONS_CHARACTERISTIC:
                        mResponse.returnDeviceValue(LeloF1DeviceResponseListener.TYPE_BUTTONS,data);
                        break;
                    default:
                        Log.i(TAG, "Unknown characteristic " + uuid);
                        break;
                }
            }

        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeServiceLeloF1.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeServiceLeloF1.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeServiceLeloF1.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeServiceLeloF1.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    private void queryGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices != null && !gattServices.isEmpty()) {
            for (BluetoothGattService gattService : gattServices) {
                queryGattCharacteristics(gattService.getCharacteristics());
            }
        }
    }

    private void queryGattCharacteristics(List<BluetoothGattCharacteristic> gattCharacteristics) {
        if (gattCharacteristics != null && !gattCharacteristics.isEmpty()) {
            mResponse.onCharacteristicDiscovered(gattCharacteristics);
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                if (isReadable(gattCharacteristic)) {
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(LeloF1Constants.MAC_ADDRESS_CHARACTERISTIC)) {
                        mMacAddressCharacteristics = gattCharacteristic;
                        Log.d(TAG, "mMacAddressCharacteristics " + mMacAddressCharacteristics.getUuid());
                    }
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(LeloF1Constants.SERIAL_NUMBER_CHARACTERISTIC)) {
                        mSerialNumberCharacteristics = gattCharacteristic;
                        Log.d(TAG, "mSerialNumberCharacteristics " + mSerialNumberCharacteristics.getUuid());
                    }
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(LeloF1Constants.CHIP_ID_CHARACTERISTIC)) {
                        mChipIdCharacteristics = gattCharacteristic;
                        Log.d(TAG, "mChipIdCharacteristics " + mChipIdCharacteristics.getUuid());
                    }
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(LeloF1Constants.MANUFACTURER_NAME_CHARACTERISTIC)) {
                        mManufacturerNameCharacteristics = gattCharacteristic;
                        Log.d(TAG, "mManufacturerNameCharacteristics " + mManufacturerNameCharacteristics.getUuid());
                    }
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(LeloF1Constants.MODEL_NUMBER_CHARACTERISTIC)) {
                        mModelNumberCharacteristics = gattCharacteristic;
                        Log.d(TAG, "mModelNumberCharacteristics " + mModelNumberCharacteristics.getUuid());
                    }
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(LeloF1Constants.HARDWARE_REVISION_CHARACTERISTIC)) {
                        mHardwareRevisionCharacteristics = gattCharacteristic;
                        Log.d(TAG, "mHardwareRevisionCharacteristics " + mHardwareRevisionCharacteristics.getUuid());
                    }
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(LeloF1Constants.FIRMWARE_REVISION_CHARACTERISTIC)) {
                        mFirmwareRevisionCharacteristics = gattCharacteristic;
                        Log.d(TAG, "mFirmwareRevisionCharacteristics " + mFirmwareRevisionCharacteristics.getUuid());
                    }
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(LeloF1Constants.SOFTWARE_REVISION_CHARACTERISTIC)) {
                        mSoftwareRevisionCharacteristics = gattCharacteristic;
                        Log.d(TAG, "mSoftwareRevisionCharacteristics " + mSoftwareRevisionCharacteristics.getUuid());
                    }
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(LeloF1Constants.SYSTEM_ID_CHARACTERISTIC)) {
                        mSystemIdCharacteristics = gattCharacteristic;
                        Log.d(TAG, "mSystemIdCharacteristics " + mSystemIdCharacteristics.getUuid());
                    }
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(LeloF1Constants.IEEE_RCDL_CHARACTERISTIC)) {
                        mIEEERCDLCharacteristics = gattCharacteristic;
                        Log.d(TAG, "mIEEERCDLCharacteristics " + mIEEERCDLCharacteristics.getUuid());
                    }
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(LeloF1Constants.PNP_ID_CHARACTERISTIC)) {
                        mPnpIdCharacteristics = gattCharacteristic;
                        Log.d(TAG, "mPnpIdCharacteristics " + mPnpIdCharacteristics.getUuid());
                    }
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(LeloF1Constants.DEVICE_NAME_CHARACTERISTIC)) {
                        mDeviceNameCharacteristics = gattCharacteristic;
                        Log.d(TAG, "mDeviceNameCharacteristics " + mDeviceNameCharacteristics.getUuid());
                    }
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(LeloF1Constants.KEY_STATE_CHARACTERISTIC)) {
                        mKeyStateCharacteristics = gattCharacteristic;
                        Log.d(TAG, "mKeyStateCharacteristics " + mKeyStateCharacteristics.getUuid());
                    }
                }
                if (isCharacteristicWriteable(gattCharacteristic)) {
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(LeloF1Constants.MOTOR_CHARACTERISTIC)) {
                        mMotorCharacteristics = gattCharacteristic;
                       Log.d(TAG, "mMotorCharacteristics " + mMotorCharacteristics.getUuid());
                    }
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(LeloF1Constants.USE_LOG_CHARACTERISTIC)) {
                        mUseLogCharacteristics = gattCharacteristic;
                        Log.d(TAG, "mUseLogCharacteristics " + mUseLogCharacteristics.getUuid());
                    }
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(LeloF1Constants.VIBRATOR_SETTING_CHARACTERISTIC)) {
                        mVibratorSettingCharacteristics = gattCharacteristic;
                        Log.d(TAG, "mVibratorSettingCharacteristics " + mVibratorSettingCharacteristics.getUuid());
                    }
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(LeloF1Constants.WAKE_UP_CHARACTERISTIC)) {
                        mWakeUpCharacteristics = gattCharacteristic;
                        Log.d(TAG, "mWakeUpCharacteristics " + mWakeUpCharacteristics.getUuid());
                    }
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(LeloF1Constants.CRUISE_CONTROL_CHARACTERISTIC)) {
                        mCruiseControlCharacteristics = gattCharacteristic;
                        Log.d(TAG, "mCruiseControlCharacteristics " + mCruiseControlCharacteristics.getUuid());
                    }
                }
                if (isCharacterisiticNotifiable(gattCharacteristic)) {
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(LeloF1Constants.BATTERY_PERCENTAGE_CHARACTERISTIC)) {
                        mBatteryPercentageCharacteristics = gattCharacteristic;
                        Log.d(TAG, "mBatteryPercentageCharacteristics " + mBatteryPercentageCharacteristics.getUuid());
                    }
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(LeloF1Constants.ACCELEROMETER_CHARACTERISTIC)) {
                        mAccelerometerCharacteristics = gattCharacteristic;
                        Log.d(TAG, "mAccelerometerCharacteristics " + mAccelerometerCharacteristics.getUuid());
                    }
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(LeloF1Constants.DEPTH_CHARACTERISTIC)) {
                        mDepthCharacteristics = gattCharacteristic;
                        Log.d(TAG, "mDepthCharacteristics " + mDepthCharacteristics.getUuid());
                    }
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(LeloF1Constants.PRESSURE_AND_TEMPERATURE_CHARACTERISTIC)) {
                        mPressureAndTemperatureCharacteristics = gattCharacteristic;
                        Log.d(TAG, "mPressureAndTemperatureCharacteristics " + mPressureAndTemperatureCharacteristics.getUuid());
                    }
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(LeloF1Constants.HALL_CHARACTERISTIC)) {
                        mHallCharacteristics = gattCharacteristic;
                        Log.d(TAG, "mHallCharacteristics " + mHallCharacteristics.getUuid());
                    }
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(LeloF1Constants.BUTTONS_CHARACTERISTIC)) {
                        mButtonsCharacteristics = gattCharacteristic;
                        Log.d(TAG, "mButtonsCharacteristics " + mButtonsCharacteristics.getUuid());
                    }
                }
            }

        }
    }

    private boolean isReadable(BluetoothGattCharacteristic characteristic) {
        return ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_READ) != 0);
    }

    private boolean isCharacteristicWriteable(BluetoothGattCharacteristic characteristic) {
        return (characteristic.getProperties() & (BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) != 0;
    }

    private boolean isCharacterisiticNotifiable(BluetoothGattCharacteristic characteristic) {
        return (characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0;
    }
}
