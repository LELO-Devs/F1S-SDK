package com.lelo.sdk.f1s.permissions.permissions;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.lelo.sdk.f1s.R;


public class DeviceBluetooth {

    public static final int REQUEST_CHECK_BLUETOOTH = 2;

    private Activity activity;

    public DeviceBluetooth(Activity activity) {
        this.activity = activity;

    }
    private DeviceBluetoothListener mListener;

    public void setDeviceBluetoothListener(DeviceBluetoothListener listener) {
        this.mListener = listener;
    }

    public void removeListener() {
        this.mListener = null;
    }

    public void checkBluetoothON(BluetoothManager mBluetoothManager){
        if(isBluetoothSupported(mBluetoothManager)){
            enableBluetooth(mBluetoothManager);
        }else{
            msgToBluetoothNotSupported();
        }
    }

    private boolean isBluetoothSupported(BluetoothManager mBluetoothManager){
        if (mBluetoothManager == null) {
            return false;
        }

        if (mBluetoothManager.getAdapter() == null) {
            return false;
        }
        return true;
    }

    private void enableBluetooth(BluetoothManager mBluetoothManager) {
        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.

        BluetoothAdapter mBluetoothAdapter=mBluetoothManager.getAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_CHECK_BLUETOOTH);
            Log.d("DeviceBluetooth","Request enable Bluetooth");
        } else {
            mListener.onBluetoothEnabled();
        }
    }

    public void msgToUserSelectedBluetoothOff(){
        Toast.makeText(activity, R.string.msg_to_user_selected_bluettoth_off,Toast.LENGTH_LONG).show();
    }

    public void msgToBluetoothNotSupported(){
        Toast.makeText(activity, R.string.ble_not_supported,Toast.LENGTH_LONG).show();
    }
}
