package com.lelo.sdk.lelo_sdk_f1_android_lib;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class BluetoothLeServiceLeloF1 extends Service {

    private final static String TAG = BluetoothLeServiceLeloF1.class.getSimpleName();

    private Queue<TxQueueItem> txQueue = new LinkedList<>();
    private boolean txQueueProcessing = false;

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    public int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED = "com.lelo.sdk.ble.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.lelo.sdk.ble.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.lelo.sdk.ble.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.lelo.sdk.ble.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA_UUID = "com.lelo.sdk.ble.EXTRA_DATA_UUID";
    public final static String EXTRA_DATA = "com.lelo.sdk.ble.EXTRA_DATA";

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.d(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.d(TAG, "Attempting to start service discovery:" + mBluetoothGatt.discoverServices());
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.d(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.d(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.d(TAG,"onCharacteristicRead");
            processTxQueue();

            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            processTxQueue();
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }

        @Override
        public void onCharacteristicWrite (BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status){
            Log.d(TAG,"onCharacteristicWrite");
            processTxQueue();

            if(status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(TAG, "CHARA WRITTEN SUCCESS ");
            } else if(status == BluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH ){
                Log.i(TAG, "CHARA WRITTEN FAILED : A write operation exceeds the maximum length of the attribute");
            }else{
                Log.i(TAG, "CHARA WRITTEN FAILED "+status);
            }
        }
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action, final BluetoothGattCharacteristic characteristic) {
        processTxQueue();
        final Intent intent = new Intent(action);

        switch (characteristic.getUuid().toString()) {
            case LeloF1Constants.MOTOR_CHARACTERISTIC:
                intent.putExtra(EXTRA_DATA, Utils.byte2String(characteristic.getValue()));
                break;
            case LeloF1Constants.BATTERY_PERCENTAGE_CHARACTERISTIC:
                intent.putExtra(EXTRA_DATA, Utils.byte2String(characteristic.getValue()));
                break;
            case LeloF1Constants.MAC_ADDRESS_CHARACTERISTIC:
                intent.putExtra(EXTRA_DATA, Utils.byte2String(characteristic.getValue()));
                break;
            case LeloF1Constants.SERIAL_NUMBER_CHARACTERISTIC:
                intent.putExtra(EXTRA_DATA, Utils.byte2String(characteristic.getValue()));
                break;
            case LeloF1Constants.CHIP_ID_CHARACTERISTIC:
                intent.putExtra(EXTRA_DATA, Utils.byte2String(characteristic.getValue()));
                break;
            case LeloF1Constants.MANUFACTURER_NAME_CHARACTERISTIC:
                intent.putExtra(EXTRA_DATA, Utils.byte2String(characteristic.getValue()));
                break;
            case LeloF1Constants.MODEL_NUMBER_CHARACTERISTIC:
                intent.putExtra(EXTRA_DATA, Utils.byte2String(characteristic.getValue()));
                break;
            case LeloF1Constants.HARDWARE_REVISION_CHARACTERISTIC:
                intent.putExtra(EXTRA_DATA, Utils.byte2String(characteristic.getValue()));
                break;
            case LeloF1Constants.FIRMWARE_REVISION_CHARACTERISTIC:
                intent.putExtra(EXTRA_DATA, Utils.byte2String(characteristic.getValue()));
                break;
            case LeloF1Constants.SOFTWARE_REVISION_CHARACTERISTIC:
                intent.putExtra(EXTRA_DATA, Utils.byte2String(characteristic.getValue()));
                break;
            case LeloF1Constants.SYSTEM_ID_CHARACTERISTIC:
                intent.putExtra(EXTRA_DATA, Utils.byte2String(characteristic.getValue()));
                break;
            case LeloF1Constants.IEEE_RCDL_CHARACTERISTIC:
                intent.putExtra(EXTRA_DATA, Utils.byte2String(characteristic.getValue()));
                break;
            case LeloF1Constants.PNP_ID_CHARACTERISTIC:
                intent.putExtra(EXTRA_DATA, Utils.byte2String(characteristic.getValue()));
                break;
            case LeloF1Constants.USE_LOG_CHARACTERISTIC:
                intent.putExtra(EXTRA_DATA, Utils.byte2String(characteristic.getValue()));
                break;
            case LeloF1Constants.ACCELEROMETER_CHARACTERISTIC:
                intent.putExtra(EXTRA_DATA, Utils.byte2String(characteristic.getValue()));
                break;
            case LeloF1Constants.DEPTH_CHARACTERISTIC:
                intent.putExtra(EXTRA_DATA, Utils.byte2String(characteristic.getValue()));
                break;
            case LeloF1Constants.PRESSURE_AND_TEMPERATURE_CHARACTERISTIC:
                intent.putExtra(EXTRA_DATA, Utils.byte2String(characteristic.getValue()));
                break;
            case LeloF1Constants.DEVICE_NAME_CHARACTERISTIC:
                intent.putExtra(EXTRA_DATA, Utils.byte2String(characteristic.getValue()));
                break;
            case LeloF1Constants.KEY_STATE_CHARACTERISTIC:
                intent.putExtra(EXTRA_DATA, Utils.byte2String(characteristic.getValue()));
                break;
            case LeloF1Constants.VIBRATOR_SETTING_CHARACTERISTIC:
                intent.putExtra(EXTRA_DATA, Utils.byte2String(characteristic.getValue()));
                break;
            case LeloF1Constants.HALL_CHARACTERISTIC:
                intent.putExtra(EXTRA_DATA, Utils.byte2String(characteristic.getValue()));
                break;
            case LeloF1Constants.WAKE_UP_CHARACTERISTIC:
                intent.putExtra(EXTRA_DATA, Utils.byte2String(characteristic.getValue()));
                break;
            case LeloF1Constants.CRUISE_CONTROL_CHARACTERISTIC:
                intent.putExtra(EXTRA_DATA, Utils.byte2String(characteristic.getValue()));
                break;
            case LeloF1Constants.BUTTONS_CHARACTERISTIC:
                intent.putExtra(EXTRA_DATA, Utils.byte2String(characteristic.getValue()));
                break;
            default:
                Log.i(TAG, "Unknown characteristic "+characteristic.getUuid().toString());
                break;
        }
        intent.putExtra(EXTRA_DATA_UUID, characteristic.getUuid().toString());

        sendBroadcast(intent);
    }

    private Object convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInput in = new ObjectInputStream(bis)) {
            return in.readObject();
        }
    }

    public class LocalBinder extends Binder {
        public BluetoothLeServiceLeloF1 getService() {
            return BluetoothLeServiceLeloF1.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new BluetoothLeServiceLeloF1.LocalBinder();

    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress) && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.d(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }

        mBluetoothGatt.disconnect();
        //mBluetoothGatt.close();
    }

    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }else{
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
    }

    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null)
            return null;
        return mBluetoothGatt.getServices();
    }

    private void writeCharacteristic(final BluetoothGattCharacteristic characteristic, final byte[] dataToWrite) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null || characteristic == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }

        characteristic.setValue(dataToWrite);
        mBluetoothGatt.writeCharacteristic(characteristic);
    }

    private void setCharacteristicNotification(final BluetoothGattCharacteristic characteristic, final boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        Log.d(TAG, "setCharacteristicNotification(device=" + mBluetoothDeviceAddress + ", UUID="
                + characteristic.getUuid() + ", enable=" + enabled + " )");

        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(LeloF1Constants.CLIENT_CHARACTERISTIC_CONFIG));
                if(descriptor != null) {
                    if(descriptor==null){
                        Log.d(TAG,"descriptor==null");
                    }else{
                        Log.d(TAG,"descriptor!=null");
                    }
                    if(enabled){
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

                    }else{
                        descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                    }

                    if(mBluetoothGatt!=null){
                        boolean valueOfWriteDescriptor=mBluetoothGatt.writeDescriptor(descriptor);
                        Log.d(TAG,"valueOfWriteDescriptor "+valueOfWriteDescriptor);
                    }
                } else {
                    Log.d(TAG,"notification processTxQueue ");
                    processTxQueue();
                }
            }
        }, 2000);

    }

    private void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    private void processTxQueue() {
        if (txQueue.size() <= 0)  {
            txQueueProcessing = false;
            return;
        }

        txQueueProcessing = true;
        TxQueueItem txQueueItem = txQueue.remove();
        if (txQueueItem.type == TxQueueItem.TxQueueItemType.SubscribeCharacteristic) {
            Log.d(TAG,"TxQueueItem SubscribeCharacteristic "+txQueueItem.characteristic.getUuid());
            setCharacteristicNotification(txQueueItem.characteristic, txQueueItem.enabled);
        }
        if (txQueueItem.type == TxQueueItem.TxQueueItemType.WriteCharacteristic) {
            Log.d(TAG,"TxQueueItem WriteCharacteristic "+txQueueItem.characteristic.getUuid());
            writeCharacteristic(txQueueItem.characteristic, txQueueItem.dataToWrite);
        }
        if (txQueueItem.type == TxQueueItem.TxQueueItemType.ReadCharacteristic) {
            Log.d(TAG,"TxQueueItem ReadCharacteristic "+txQueueItem.characteristic.getUuid());
            readCharacteristic(txQueueItem.characteristic);
        }
    }

    private void addToTxQueue(TxQueueItem txQueueItem) {
        txQueue.add(txQueueItem);
        if (!txQueueProcessing) {
            processTxQueue();
        }
    }

    public void queueWriteDataToCharacteristic(final BluetoothGattCharacteristic ch, final byte[] dataToWrite) {
        TxQueueItem txQueueItem = new TxQueueItem();
        txQueueItem.characteristic = ch;
        txQueueItem.dataToWrite = dataToWrite;
        txQueueItem.type = TxQueueItem.TxQueueItemType.WriteCharacteristic;
        addToTxQueue(txQueueItem);
    }

    public void queueSetNotificationForCharacteristic(BluetoothGattCharacteristic ch, boolean enabled) {
        TxQueueItem txQueueItem = new TxQueueItem();
        txQueueItem.characteristic = ch;
        txQueueItem.enabled = enabled;
        txQueueItem.type = TxQueueItem.TxQueueItemType.SubscribeCharacteristic;
        addToTxQueue(txQueueItem);
    }

    public void queueReadCharacteristicValue(BluetoothGattCharacteristic ch) {
        TxQueueItem txQueueItem = new TxQueueItem();
        txQueueItem.characteristic = ch;
        txQueueItem.type = TxQueueItem.TxQueueItemType.ReadCharacteristic;
        addToTxQueue(txQueueItem);
    }
}
