package com.lelo.sdk.f1s.scan_for_ble_devices;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lelo.sdk.f1s.R;
import com.lelo.sdk.f1s.test_sdk.TestSDKActivity;
import com.lelo.sdk.lelo_sdk_f1_android_lib.LeloF1Constants;
import com.lelo.sdk.lelo_sdk_f1_android_lib.bluetooth.BLEDeviceWithRssi;
import com.lelo.sdk.lelo_sdk_f1_android_lib.bluetooth.scanner.BluetoothScannerAPI;
import com.lelo.sdk.lelo_sdk_f1_android_lib.bluetooth.scanner.BluetoothScannerAPI18;
import com.lelo.sdk.lelo_sdk_f1_android_lib.bluetooth.scanner.BluetoothScannerListener;

import java.util.ArrayList;
import java.util.List;


public class ConnectDeviceAPI18Activity extends AppCompatActivity implements BluetoothScannerListener {

    BluetoothScannerAPI mBluetoothScanner =new BluetoothScannerAPI18();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_device);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<String> findDeviceList=new ArrayList<>();
        findDeviceList.add(LeloF1Constants.NAME_ADVERTISING);
        findDeviceList.add(LeloF1Constants.NAME_ADVERTISING_V2A);
        findDeviceList.add(LeloF1Constants.NAME_ADVERTISING_V2X);
        mBluetoothScanner.setUp(this,findDeviceList);
        mBluetoothScanner.setListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBluetoothScanner.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBluetoothScanner.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mBluetoothScanner.onPause();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == mBluetoothScanner.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                //Bluetooth not enabled.
                finish();
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onBluetoothNotSupported() {

    }

    @Override
    public void onBluetoothNotEnabled() {

    }

    @Override
    public void onBLEDevicesScanningDone(List<BLEDeviceWithRssi> deviceList) {
        if(deviceList==null || deviceList.size()==0) {

        }else{
            for(BLEDeviceWithRssi device : deviceList){
                device.writeBLEDeviceInLogcat();
            }
            BLEDeviceWithRssi mDevice=BLEDeviceWithRssi.findClosestBLEDeviceFromList(deviceList);
            if(mDevice!=null){
                Log.d("CONNECT","FOUND "+mDevice.getDevice().getName()+" "+mDevice.getDevice().getAddress()+" "+mDevice.getRssi());

                String deviceName=mDevice.getDevice().getName();
                String deviceAddress=mDevice.getDevice().getAddress();

                Intent intent = new Intent(getApplicationContext(), TestSDKActivity.class);
                intent.putExtra("DEVICE_NAME",deviceName);
                intent.putExtra("DEVICE_ADDRESS",deviceAddress);
                startActivity(intent);
                finish();
            }
        }
    }
}

