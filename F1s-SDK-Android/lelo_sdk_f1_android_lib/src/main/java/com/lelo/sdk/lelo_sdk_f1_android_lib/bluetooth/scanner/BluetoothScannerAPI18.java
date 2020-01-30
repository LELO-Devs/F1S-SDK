package com.lelo.sdk.lelo_sdk_f1_android_lib.bluetooth.scanner;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.lelo.sdk.lelo_sdk_f1_android_lib.bluetooth.BLEDeviceWithRssi;

import java.util.ArrayList;
import java.util.List;


public class BluetoothScannerAPI18 implements BluetoothScannerAPI {


    // Device scan callback for API level lower than 21.
    private final BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, final byte[] scanRecord) {
            checkDevice(device, rssi);
        }
    };

    private ScanSettings mScanSettings;
    private List<ScanFilter> mScanFilterList;
    private boolean mIsScanning;
    private Activity activity;
    private BluetoothLeScanner mBluetoothLeScanner;
    private BluetoothAdapter mBluetoothAdapter;
    private List<BLEDeviceWithRssi> mDeviceList=new ArrayList<>();
    private List<String> findDeviceList =new ArrayList<>();
    private ScanCallback mScanCallback;
    private BluetoothScannerListener mListener;

    @Override
    public void setListener(BluetoothScannerListener listener) {
        this.mListener = listener;
    }

    @Override
    public void removeListener() {
        this.mListener = null;
    }

    @Override
    public void setUp(Activity activity, List<String> mFindDeviceList) {
        this.activity=activity;
        if (!activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(activity, "BLE Not Supported", Toast.LENGTH_SHORT).show();
        }
        final BluetoothManager bluetoothManager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        this.findDeviceList =mFindDeviceList;
    }

    private void checkDevice(BluetoothDevice device, int rssi) {
        if (device == null) return;
        Log.d("BluetoothScanner","found device "+device.getName()+" "+device.getAddress()+" "+rssi);
        final String deviceName = device.getName();
        if (deviceName != null) {
            for(String findDevice : findDeviceList){
                if(findDevice.equalsIgnoreCase(deviceName)){
                    Log.d("BluetoothScanner","unfiltered found device "+device.getName()+" "+device.getAddress()+" "+rssi);
                    boolean deviceInList=false;
                    String deviceAddress = device.getAddress();
                    for(BLEDeviceWithRssi item : mDeviceList){
                        if(item.getDevice().getAddress().compareTo(deviceAddress)==0){
                            deviceInList=true;
                            item.setRssi(rssi);
                        }
                    }
                    if(deviceInList==false){
                        BLEDeviceWithRssi mBLEDeviceWithRssi = new BLEDeviceWithRssi(device, rssi);
                        mDeviceList.add(mBLEDeviceWithRssi);
                    }
                    deviceFound();
                }
            }
        }
    }
    private boolean deviceFoundActivated=false;
    private void deviceFound() {
        if (deviceFoundActivated == false) {
            deviceFoundActivated = true;
            stopDiscoveringDevices();
            mListener.onBLEDevicesScanningDone(mDeviceList);
        }
    }

    private boolean stopDiscoveringDevices=false;
    private void stopDiscoveringDevices() {
        if (mIsScanning) {
            if(stopDiscoveringDevices==false){
                stopDiscoveringDevices=true;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (mBluetoothLeScanner != null) mBluetoothLeScanner.stopScan(mScanCallback);
                } else {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }

                mIsScanning = false;
            }
        }
    }


    @Override
    public void onResume() {
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mScanCallback = new ScanCallback() {
                    @Override
                    public void onScanResult(int callbackType, ScanResult result) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            checkDevice(result.getDevice(), result.getRssi());
                        }
                    }

                    @Override
                    public void onBatchScanResults(List<ScanResult> results) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            for (ScanResult sr : results) {
                                checkDevice(sr.getDevice(), sr.getRssi());
                            }
                        }
                    }

                    @Override
                    public void onScanFailed(int errorCode) {

                    }
                };

                if (mBluetoothAdapter != null)
                    mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();

                mScanSettings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) //For foreground. ScanSettings.SCAN_MODE_LOW_POWER for background.
                        .build();

                mScanFilterList = new ArrayList<>();

                /*final ScanFilter uuidFilter = new ScanFilter.Builder()
                    .setServiceUuid(ParcelUuid.fromString(DeviceConstants.MOTOR_SERVICE_UUID))
                    .build();*/

                //mScanFilterList.add(uuidFilter);
            }
            startScanningForBLEDevice(true);
        }
    }

    @Override
    public void onPause() {
        Log.d("BluetoothScanner","unregisterReceiver");
        if (mBluetoothAdapter == null) {
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            return;
        }
        stopDiscoveringDevices();
    }

    @Override
    public void onDestroy() {
        removeListener();
    }

    @Override
    public void startScanningForBLEDevice(boolean enable) {
        stopDiscoveringDevices=false;
        if (mBluetoothAdapter == null) {
            if (mListener != null) mListener.onBluetoothNotSupported();
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            if (mListener != null) mListener.onBluetoothNotEnabled();
            return;
        }
        startDiscoveringDevices();
    }

    private void startDiscoveringDevices() {
        if (!mIsScanning) {
            Log.d("BluetoothScanner","startDiscoveringDevices");
            mDeviceList.clear();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (mBluetoothLeScanner != null)
                    mBluetoothLeScanner.startScan(mScanFilterList, mScanSettings, mScanCallback);
            } else {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            }

            mIsScanning = true;
        }
    }

    @Override
    public void stopScanningForBLEDevice() {
        stopDiscoveringDevices();
    }
}
