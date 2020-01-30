package com.lelo.sdk.lelo_sdk_f1_android_lib.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.util.Arrays;
import java.util.List;


public class BLEDeviceWithRssi {

    private BluetoothDevice device;
    private int rssi;

    public BLEDeviceWithRssi(BluetoothDevice device, int rssi) {
        this.device = device;
        this.rssi = rssi;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }


    public void writeBLEDeviceInLogcat(){
            Log.d("DEVICE INFO", "NAME: " + device.getName());
            Log.d("DEVICE INFO", "ADRESS: " + device.getAddress());
            Log.d("DEVICE INFO", "RSSI: " + rssi);
            Log.d("DEVICE INFO", "TYPE: " + device.getType());
            Log.d("DEVICE INFO", "UUID: " + Arrays.toString(device.getUuids()));
            Log.d("DEVICE INFO", "BOND_SATTE: " + device.getBondState());
            Log.d("DEVICE INFO", "BCLASS: " + device.getBluetoothClass());
            Log.d("DEVICE INFO", "-----------------------------------------");
    }

    public static BLEDeviceWithRssi findClosestBLEDeviceFromList(List<BLEDeviceWithRssi> deviceList){

        if(deviceList==null ) return null;
        if(deviceList.size()==0 ) return null;
        if(deviceList.size()==1) return deviceList.get(0);

        BLEDeviceWithRssi closestDevice=deviceList.get(0);
        for(BLEDeviceWithRssi device:deviceList){
            if(closestDevice.getRssi()< device.getRssi()) closestDevice=device;
        }
        return closestDevice;
    }
}
