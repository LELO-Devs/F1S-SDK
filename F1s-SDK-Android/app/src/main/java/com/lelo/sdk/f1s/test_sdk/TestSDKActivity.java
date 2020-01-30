package com.lelo.sdk.f1s.test_sdk;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.lelo.sdk.f1s.R;
import com.lelo.sdk.f1s.test_sdk.adapter.BleCharAdapter;
import com.lelo.sdk.f1s.model.BleCharacteristic;
import com.lelo.sdk.f1s.test_sdk.dialog.F1Dialogs;
import com.lelo.sdk.f1s.test_sdk.dialog.F1DialogsListener;
import com.lelo.sdk.f1s.test_sdk.model.F1UUIDShort;
import com.lelo.sdk.lelo_sdk_f1_android_lib.LeloF1Constants;
import com.lelo.sdk.lelo_sdk_f1_android_lib.LeloF1Device;
import com.lelo.sdk.lelo_sdk_f1_android_lib.LeloF1DeviceImpl;
import com.lelo.sdk.lelo_sdk_f1_android_lib.LeloF1DeviceResponseListener;
import com.lelo.sdk.lelo_sdk_f1_android_lib.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class TestSDKActivity extends AppCompatActivity implements LeloF1DeviceResponseListener, F1DialogsListener {

    private LeloF1Device mDevice = new LeloF1DeviceImpl();

    public final String TAG = TestSDKActivity.class.getSimpleName();
    private String mDeviceName;
    private String mDeviceAddress;

    private RecyclerView recyclerView;
    private BleCharAdapter mAdapter;
    private List<BleCharacteristic> mList = new ArrayList<>();
    private boolean readAllCharacteristics = true;

    private boolean dialogMotorControl = false;
    private boolean dialogVibratorSensor = false;
    private boolean dialogWakeUp = false;

    private F1Dialogs mDialogs = new F1Dialogs();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mDeviceName = getIntent().getStringExtra("DEVICE_NAME");
        mDeviceAddress = getIntent().getStringExtra("DEVICE_ADDRESS");
        Log.d(TAG, "device name: " + mDeviceName + " mac address: " + mDeviceAddress);

        mDevice.setDeviceLunaResponseListener(this);
        mDevice.setDeviceAddress(mDeviceAddress);
        mDevice.bindBluetoothService(getApplicationContext());

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new BleCharAdapter(this, mList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        mDialogs.setListener(this);
    }


    @Override
    public void returnDeviceValue(String valueType, String valueData) {
        updateList(valueType, valueData);
        readAllCharacteristicsOnStart(valueType, valueData);
        openDialogOnWriteFromList(valueType, valueData);
    }

    private void openDialogOnWriteFromList(String valueType, String valueData) {
        if (dialogMotorControl && valueType.compareTo(LeloF1DeviceResponseListener.TYPE_MOTOR) == 0) {
            dialogMotorControl = false;
            mDialogs.showMotorControlDialog(this, valueData);
        }
        if (dialogVibratorSensor && valueType.compareTo(LeloF1DeviceResponseListener.TYPE_VIBRATOR_SETTING) == 0) {
            dialogVibratorSensor = false;
            mDialogs.dialogVibratorSetting(this, valueData);
        }
        if (dialogWakeUp && valueType.compareTo(LeloF1DeviceResponseListener.TYPE_WAKE_UP) == 0) {
            dialogWakeUp = false;
            mDialogs.showWakeUpDialog(this, valueData);
        }
    }

    private void readAllCharacteristicsOnStart(String valueType, String valueData) {
        if (readAllCharacteristics) {
            if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_DEVICE_NAME) == 0) {
                mDevice.readSystemId();
            } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_SYSTEM_ID) == 0) {
                mDevice.readModelNumber();
            } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_MODEL_NUMBER) == 0) {
                mDevice.readFirmwareRevision();
            } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_FIRMWARE_REVISION) == 0) {
                mDevice.readHardwareRevision();
            } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_HARDWARE_REVISION) == 0) {
                mDevice.readSoftwareRevision();
            } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_SOFTWARE_REVISION) == 0) {
                mDevice.readManufactureName();
            } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_MANUFACTURER_NAME) == 0) {
                mDevice.readIEEERegulatoryCertificationDataList();
            } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_IEEE_RCDL) == 0) {
                mDevice.readPnpId();
            } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_PNP_ID) == 0) {
                mDevice.readMACAddress();
            } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_MAC_ADDRESS) == 0) {
                mDevice.readChipId();
            } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_CHIP_ID) == 0) {
                mDevice.readMotorState();
            } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_MOTOR) == 0) {
                mDevice.readSerialNumber();
            } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_SERIAL_NUMBER) == 0) {
                mDevice.readUseLog();
            } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_USE_LOG) == 0) {
                mDevice.readVibratorSetting();
            } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_VIBRATOR_SETTING) == 0) {
                mDevice.readWakeUp();
            } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_WAKE_UP) == 0) {
                mDevice.readCruiseControl();
            } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_CRUISE_CONTROL) == 0) {
                mDevice.readPressureAndTemperature();
            } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_PRESSURE_AND_TEMPERATURE) == 0) {
                mDevice.readDepth();
            } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_DEPTH) == 0) {
                mDevice.readAccelerometer();
            } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_ACCELEROMETER) == 0) {
                mDevice.readKeyState();
            } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_KEY_STATE) == 0) {
                mDevice.readHall();
            } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_HALL) == 0) {
                mDevice.readButtons();
            } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_BUTTONS) == 0) {
                mDevice.readBatteryPercentage();
            } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_BATTERY_PERCENTAGE) == 0) {
                readAllCharacteristics = false;
            }
        }
    }

    private void updateList(String valueType, String valueData) {

        if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_MOTOR) == 0) {
            updateListValueForUuid(LeloF1Constants.MOTOR_CHARACTERISTIC, valueData);
        } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_MAC_ADDRESS) == 0) {
            updateListValueForUuid(LeloF1Constants.MAC_ADDRESS_CHARACTERISTIC, valueData);
        } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_SERIAL_NUMBER) == 0) {
            updateListValueForUuid(LeloF1Constants.SERIAL_NUMBER_CHARACTERISTIC, Utils.hexToAscii(valueData));
        } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_CHIP_ID) == 0) {
            updateListValueForUuid(LeloF1Constants.CHIP_ID_CHARACTERISTIC, valueData);
        } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_MANUFACTURER_NAME) == 0) {
            updateListValueForUuid(LeloF1Constants.MANUFACTURER_NAME_CHARACTERISTIC, Utils.hexToAscii(valueData));
        } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_MODEL_NUMBER) == 0) {
            updateListValueForUuid(LeloF1Constants.MODEL_NUMBER_CHARACTERISTIC, Utils.hexToAscii(valueData));
        } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_HARDWARE_REVISION) == 0) {
            updateListValueForUuid(LeloF1Constants.HARDWARE_REVISION_CHARACTERISTIC, Utils.hexToAscii(valueData));
        } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_FIRMWARE_REVISION) == 0) {
            updateListValueForUuid(LeloF1Constants.FIRMWARE_REVISION_CHARACTERISTIC, Utils.hexToAscii(valueData));
        } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_SOFTWARE_REVISION) == 0) {
            updateListValueForUuid(LeloF1Constants.SOFTWARE_REVISION_CHARACTERISTIC, Utils.hexToAscii(valueData));
        } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_SYSTEM_ID) == 0) {
            updateListValueForUuid(LeloF1Constants.SYSTEM_ID_CHARACTERISTIC, valueData);
        } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_IEEE_RCDL) == 0) {
            updateListValueForUuid(LeloF1Constants.IEEE_RCDL_CHARACTERISTIC, valueData);
        } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_PNP_ID) == 0) {
            updateListValueForUuid(LeloF1Constants.PNP_ID_CHARACTERISTIC, valueData);
        } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_USE_LOG) == 0) {
            updateListValueForUuid(LeloF1Constants.USE_LOG_CHARACTERISTIC, valueData);
        } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_BATTERY_PERCENTAGE) == 0) {
            int batteryPercentage = Utils.hex2Decimal(valueData);
            updateListValueForUuid(LeloF1Constants.BATTERY_PERCENTAGE_CHARACTERISTIC, batteryPercentage + " %");
        } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_PRESSURE_AND_TEMPERATURE) == 0) {
            String temperatureHex = valueData.substring(0, 6);
            String airPressureHex = valueData.substring(6);
            float temperature = ((float) Utils.hex2Decimal(temperatureHex) / 100);
            float airPressure = ((float) Utils.hex2Decimal(airPressureHex) / 100);
            updateListValueForUuid(LeloF1Constants.PRESSURE_AND_TEMPERATURE_CHARACTERISTIC, temperature + " C - " + airPressure + " mbar");
        } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_DEVICE_NAME) == 0) {
            updateListValueForUuid(LeloF1Constants.DEVICE_NAME_CHARACTERISTIC, "" + Utils.hexToAscii(valueData));
        } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_KEY_STATE) == 0) {
            updateListValueForUuid(LeloF1Constants.KEY_STATE_CHARACTERISTIC, valueData);
        } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_VIBRATOR_SETTING) == 0) {
            updateListValueForUuid(LeloF1Constants.VIBRATOR_SETTING_CHARACTERISTIC, valueData);
        } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_ACCELEROMETER) == 0) {
            String x = valueData.substring(0, 4);
            String y = valueData.substring(4, 8);
            String z = valueData.substring(8);
            updateListValueForUuid(LeloF1Constants.ACCELEROMETER_CHARACTERISTIC, "x : " + Utils.hex2Decimal(x) + "   y : " + Utils.hex2Decimal(y) + "   z : " + Utils.hex2Decimal(z));
        } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_DEPTH) == 0) {
            updateListValueForUuid(LeloF1Constants.DEPTH_CHARACTERISTIC, valueData);
        } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_HALL) == 0) {
            updateListValueForUuid(LeloF1Constants.HALL_CHARACTERISTIC, valueData);
        } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_WAKE_UP) == 0) {
            updateListValueForUuid(LeloF1Constants.WAKE_UP_CHARACTERISTIC, valueData);
        } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_CRUISE_CONTROL) == 0) {
            updateListValueForUuid(LeloF1Constants.CRUISE_CONTROL_CHARACTERISTIC, valueData);
        } else if (valueType.compareTo(LeloF1DeviceResponseListener.TYPE_BUTTONS) == 0) {
            updateListValueForUuid(LeloF1Constants.BUTTONS_CHARACTERISTIC, valueData);
        }
    }

    private void updateListValueForUuid(String uuid, String value) {
        for (int i = 0; i < mList.size(); i++) {
            BleCharacteristic mBleCharacteristic = mList.get(i);
            if (mBleCharacteristic.getUuid().compareTo(uuid.toString().substring(4, 8)) == 0) {
                Log.d(TAG, "update value " + mBleCharacteristic.getUuid() + " in " + uuid + " " + value);
                mBleCharacteristic.setValue(value);
                mAdapter.notifyItemChanged(i);
            }
        }
    }

    @Override
    public void onDeviceConnected() {
        Log.d(TAG, "device connected");
        mDialogs.showDialogPressPowerButtonToActivateKeyState(this);
    }

    @Override
    public void onDeviceDisconnected() {

    }

    @Override
    public void onBluetoothServiceNotInitialized() {

    }

    public void readCharacteristic(String charachteristic) {
        switch (charachteristic) {
            case F1UUIDShort.DEVICE_NAME_UUID_SHORT:
                mDevice.readDeviceName();
                break;
            case F1UUIDShort.MOTOR_CONTROL_UUID_SHORT:
                mDevice.readMotorState();
                break;
            case F1UUIDShort.MAC_ADDRESS_UUID_SHORT:
                mDevice.readMACAddress();
                break;
            case F1UUIDShort.SERIAL_NUMBER_UUID_SHORT:
                mDevice.readSerialNumber();
                break;
            case F1UUIDShort.CHIP_ID_UUID_SHORT:
                mDevice.readChipId();
                break;
            case F1UUIDShort.USAGE_LOG_UUID_SHORT:
                mDevice.readUseLog();
                break;
            case F1UUIDShort.SYSTEM_ID_UUID_SHORT:
                mDevice.readSystemId();
                break;
            case F1UUIDShort.MODEL_NUMBER_UUID_SHORT:
                mDevice.readModelNumber();
                break;
            case F1UUIDShort.FIRMWARE_UUID_SHORT:
                mDevice.readFirmwareRevision();
                break;
            case F1UUIDShort.HARDWARE_UUID_SHORT:
                mDevice.readHardwareRevision();
                break;
            case F1UUIDShort.SOFTWARE_UUID_SHORT:
                mDevice.readSoftwareRevision();
                break;
            case F1UUIDShort.MANUFACTURER_NAME_UUID_SHORT:
                mDevice.readManufactureName();
                break;
            case F1UUIDShort.IEEE_UUID_SHORT:
                mDevice.readIEEERegulatoryCertificationDataList();
                break;
            case F1UUIDShort.PNP_ID_UUID_SHORT:
                mDevice.readPnpId();
            case F1UUIDShort.KEY_STATE_UUID_SHORT:
                mDevice.readKeyState();
                break;
            case F1UUIDShort.VIBRATOR_SETTINGS_UUID_SHORT:
                mDevice.readVibratorSetting();
                break;
            case F1UUIDShort.BATTERY_PERCENTAGE_UUID_SHORT:
                mDevice.readBatteryPercentage();
                break;
            case F1UUIDShort.PRESSURE_UUID_SHORT:
                mDevice.readPressureAndTemperature();
                break;
            case F1UUIDShort.WAKE_UP_UUID_SHORT:
                mDevice.readWakeUp();
                break;
            case F1UUIDShort.MOTOR_WORK_ON_TOUCH_UUID_SHORT:
                mDevice.readCruiseControl();
                break;
            case F1UUIDShort.BUTTON_UUID_SHORT:
                mDevice.readButtons();
                break;
            case F1UUIDShort.LENGTH_UUID_SHORT:
                mDevice.readDepth();
                break;
            case F1UUIDShort.ACCELETROMETER_UUID_SHORT:
                mDevice.readAccelerometer();
                break;
            case F1UUIDShort.HALL_UUID_SHORT:
                mDevice.readHall();
                break;
        }
    }

    public void writeCharacteristic(String characteristicUuidShort) {
        switch (characteristicUuidShort) {
            case F1UUIDShort.MOTOR_CONTROL_UUID_SHORT:
                dialogMotorControl = true;
                mDevice.readMotorState();
                break;
            case F1UUIDShort.USAGE_LOG_UUID_SHORT:
                mDialogs.showDialogUsageLog(this);
                break;
            case F1UUIDShort.VIBRATOR_SETTINGS_UUID_SHORT:
                dialogVibratorSensor = true;
                mDevice.readVibratorSetting();
                break;
            case F1UUIDShort.WAKE_UP_UUID_SHORT:
                dialogWakeUp = true;
                mDevice.readWakeUp();
                break;
            case F1UUIDShort.MOTOR_WORK_ON_TOUCH_UUID_SHORT:
                mDialogs.showMotorWorkOnTouchDialog(this);
                break;
        }

    }

    public void notifyCharacteristic(String characteristicUuidShort) {
        switch (characteristicUuidShort) {
            case F1UUIDShort.BATTERY_PERCENTAGE_UUID_SHORT:
                mDevice.startNotifyBatteryPercentage();
                break;
            case F1UUIDShort.PRESSURE_UUID_SHORT:
                mDevice.startNotifyPressureAndTemperature();
                break;
            case F1UUIDShort.LENGTH_UUID_SHORT:
                mDevice.startNotifyDepth();
                break;
            case F1UUIDShort.ACCELETROMETER_UUID_SHORT:
                mDevice.startNotifyAccelerometer();
                break;
            case F1UUIDShort.KEY_STATE_UUID_SHORT:
                mDevice.startNotifyKeyState();
                break;
            case F1UUIDShort.HALL_UUID_SHORT:
                mDevice.startNotifyHall();
                break;
            case F1UUIDShort.BUTTON_UUID_SHORT:
                mDevice.startNotifyButtons();
                break;
        }
    }




    @Override
    public void onCharacteristicDiscovered(List<BluetoothGattCharacteristic> gattCharacteristics) {
        addCharacteristics(gattCharacteristics);
        mAdapter.notifyDataSetChanged();
        mDevice.readDeviceName();
    }

    private void getCharacteristicNameFromBluetoothStack(List<BluetoothGattCharacteristic> gattCharacteristics) {
        for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
            UUID uuid = gattCharacteristic.getUuid();
            Log.d(TAG, "NameFromBluetoothStack " + uuid + " ");
            BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(UUID.fromString(LeloF1Constants.USER_DESCRIPTION_CHARACTERISTIC));
            if (descriptor != null) {
                if (descriptor == null) {
                    Log.d(TAG, "NameFromBluetoothStack descriptor==null");
                } else {
                    Log.d(TAG, "NameFromBluetoothStack descriptor!=null " + descriptor.getValue());

                }
            }
        }
    }

    private void addCharacteristics(List<BluetoothGattCharacteristic> gattCharacteristics) {
        addReadCharacteristics(gattCharacteristics);
        addWriteCharacteristics(gattCharacteristics);
        addNotifyCharacteristics(gattCharacteristics);
    }

    private void addReadCharacteristics(List<BluetoothGattCharacteristic> gattCharacteristics) {
        for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
            String uuid = gattCharacteristic.getUuid().toString().substring(4, 8);
            String name = F1UUIDShort.getNameFromUuid(uuid);

            if (name.compareTo("") != 0) {
                BleCharacteristic mBleCharacteristic = new BleCharacteristic(name, uuid);
                if (BleCharacteristic.isCharacteristicReadable(gattCharacteristic)) {
                    mBleCharacteristic.setRead(true);
                }
                if (BleCharacteristic.isCharacteristicWritable(gattCharacteristic)) {
                    mBleCharacteristic.setWrite(true);

                }
                if (BleCharacteristic.isCharacteristicNotifiable(gattCharacteristic)) {
                    mBleCharacteristic.setNotify(true);
                }

                if (BleCharacteristic.isCharacteristicReadable(gattCharacteristic) && !BleCharacteristic.isCharacteristicWritable(gattCharacteristic) && !BleCharacteristic.isCharacteristicNotifiable(gattCharacteristic)) {
                    mList.add(mBleCharacteristic);
                }
            }
        }
    }

    private void addWriteCharacteristics(List<BluetoothGattCharacteristic> gattCharacteristics) {
        for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
            String uuid = gattCharacteristic.getUuid().toString().substring(4, 8);
            String name = F1UUIDShort.getNameFromUuid(uuid);

            if (name.compareTo("") != 0) {
                BleCharacteristic mBleCharacteristic = new BleCharacteristic(name, uuid);
                if (BleCharacteristic.isCharacteristicReadable(gattCharacteristic)) {
                    mBleCharacteristic.setRead(true);
                }
                if (BleCharacteristic.isCharacteristicWritable(gattCharacteristic)) {
                    mBleCharacteristic.setWrite(true);

                }
                if (BleCharacteristic.isCharacteristicNotifiable(gattCharacteristic)) {
                    mBleCharacteristic.setNotify(true);
                }
                if (BleCharacteristic.isCharacteristicWritable(gattCharacteristic)) {
                    mList.add(mBleCharacteristic);
                }
            }
        }
    }

    private void addNotifyCharacteristics(List<BluetoothGattCharacteristic> gattCharacteristics) {
        for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
            String uuid = gattCharacteristic.getUuid().toString().substring(4, 8);
            String name = F1UUIDShort.getNameFromUuid(uuid);

            if (name.compareTo("") != 0) {
                BleCharacteristic mBleCharacteristic = new BleCharacteristic(name, uuid);
                if (BleCharacteristic.isCharacteristicReadable(gattCharacteristic)) {
                    mBleCharacteristic.setRead(true);
                }
                if (BleCharacteristic.isCharacteristicWritable(gattCharacteristic)) {
                    mBleCharacteristic.setWrite(true);

                }
                if (BleCharacteristic.isCharacteristicNotifiable(gattCharacteristic)) {
                    mBleCharacteristic.setNotify(true);
                }
                if (BleCharacteristic.isCharacteristicNotifiable(gattCharacteristic)) {
                    mList.add(mBleCharacteristic);
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDevice.onDestroy(getApplicationContext());
        mDialogs.removeListener();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mDevice.registerReceiver(getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDevice.unregisterReceiver(getApplicationContext());
    }


    @Override
    public void fromDialogsWriteVibratorSetting(byte speedTouch1, byte speedTouch2, byte speedTouch3, byte speedTouch4, byte speedTouch5, byte speedTouch6, byte speedTouch7, byte speedTouch8) {
        mDevice.writeVibratorSetting(speedTouch1, speedTouch2, speedTouch3, speedTouch4, speedTouch5, speedTouch6, speedTouch7, speedTouch8);
    }


    @Override
    public void fromDialogWakeUp(boolean enable) {
        mDevice.enableWakeUp(enable);
    }

    @Override
    public void fromDialogClearUsageLog() {
        mDevice.clearUseLog();
    }


    @Override
    public void fromDialogStartMotor(byte speedMainMotor, byte speedVibratorMotor) {
        mDevice.startMotor(speedMainMotor, speedVibratorMotor);
    }

    @Override
    public void fromDialogStopMotor() {
        mDevice.stopMotor();
    }

    @Override
    public void fromDialogTurnOffDevice() {
        mDevice.turnOffDevice();
    }

    @Override
    public void fromDialogVerifyAccelerometer() {
        mDevice.verifyAccelerometer();
    }

    @Override
    public void fromDialogMotorWorkOnTouchEnableCapSensor(boolean enable) {
        mDevice.enableCruiseControl(enable);
    }

    @Override
    public void fromDialogMotorWorkOnTouchEnableCapSensorAndResetDefualtSpeed() {
        mDevice.enableCruiseControlAndResetToDefaultSpeedLevel();
    }

}
