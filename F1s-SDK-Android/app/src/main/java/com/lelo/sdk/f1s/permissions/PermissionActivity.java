package com.lelo.sdk.f1s.permissions;


import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.lelo.sdk.f1s.R;
import com.lelo.sdk.f1s.permissions.model.AllowedPermissions;
import com.lelo.sdk.f1s.permissions.permissions.DeviceBluetooth;
import com.lelo.sdk.f1s.permissions.permissions.DeviceBluetoothListener;
import com.lelo.sdk.f1s.permissions.permissions.DeviceLocation;
import com.lelo.sdk.f1s.permissions.permissions.DeviceLocationListener;
import com.lelo.sdk.f1s.scan_for_ble_devices.ConnectDeviceAPI18Activity;


public class PermissionActivity extends AppCompatActivity implements DeviceBluetoothListener, DeviceLocationListener, View.OnClickListener {

    private String TAG = PermissionActivity.class.getSimpleName();
    private RelativeLayout checkBluetooth;
    private RelativeLayout checkLocation;
    private RelativeLayout checkLocationCourse;
    private Button buttonNextStep;

    private final int REQUEST_CODE_PERMISSION_M_COARSE_LOCATION=1;

    DeviceBluetooth mDeviceBluetooth;
    DeviceLocation mDeviceLocation;
    AllowedPermissions mAllowedPermissions = new AllowedPermissions();
    private boolean next = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        //set up ui
        checkBluetooth = findViewById(R.id.checkBluetooth);
        checkLocation = findViewById(R.id.checkLocation);
        checkLocationCourse = findViewById(R.id.checkLocationCourse);
        buttonNextStep = findViewById(R.id.buttonNextStep);
        buttonNextStep.setOnClickListener(this);
        mDeviceBluetooth = new DeviceBluetooth(this);
        mDeviceBluetooth.setDeviceBluetoothListener(this);
        mDeviceLocation = new DeviceLocation();
        mDeviceLocation.setDeviceLocationChinaListener(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        check();
    }

    private void check() {
        checkBluetoothON();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkPermissionsM();
            }
        }, 200);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkLocationON();
            }
        }, 400);
    }

    @Override
    public void onBluetoothEnabled() {
        mAllowedPermissions.setBLUETOOTH_ON(true);
        checkActivePermissions();
    }

    private void checkBluetoothON() {
        BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (mBluetoothManager != null && mDeviceBluetooth != null)
            mDeviceBluetooth.checkBluetoothON(mBluetoothManager);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mDeviceBluetooth != null) {
            mDeviceBluetooth.removeListener();
            mDeviceBluetooth = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case DeviceBluetooth.REQUEST_CHECK_BLUETOOTH:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.d(TAG, "BLUETOOTH ON");
                        mAllowedPermissions.setBLUETOOTH_ON(true);
                        checkActivePermissions();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.d(TAG, "BLUETOOTH OFF");
                        if (mDeviceBluetooth != null)
                            mDeviceBluetooth.msgToUserSelectedBluetoothOff();
                        mAllowedPermissions.setBLUETOOTH_ON(false);
                        checkActivePermissions();
                        break;
                }
                break;
        }
    }

    private void checkActivePermissions() {
        if (mAllowedPermissions.isBLUETOOTH_ON()) {
            enableBluetooth(true);
        } else {
            enableBluetooth(false);
        }
        if (mAllowedPermissions.isLOCATION_ON()) {
            enableLocation(true);
        } else {
            enableLocation(false);
        }
        if (mAllowedPermissions.isACCESS_COARSE_LOCATION()) {
            enableLocationPermission(true);
        } else {
            enableLocationPermission(false);
        }
        if (mAllowedPermissions.areAllPermissionsAllowed()) {
            enableButtonNext(true);
        } else {
            enableButtonNext(false);
        }
    }

    public void enableLocation(boolean turnedOn) {
        checkTurnOnOrOff(checkLocation, turnedOn);
    }

    public void enableBluetooth(boolean turnedOn) {
        checkTurnOnOrOff(checkBluetooth, turnedOn);
    }

    public void enableLocationPermission(boolean turnedOn) {
        checkTurnOnOrOff(checkLocationCourse, turnedOn);
    }

    private void checkTurnOnOrOff(RelativeLayout check, boolean on_or_off) {
        int image = R.drawable.check_on;

        if (on_or_off) {
            image = R.drawable.check_on;
        } else {
            image = R.drawable.check_off;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            check.setBackground(getResources().getDrawable(image, null));
        } else {
            check.setBackground(getResources().getDrawable(image));
        }
    }

    @Override
    public void onLocationChinaEnabled() {
        mAllowedPermissions.setLOCATION_ON(true);
        checkActivePermissions();
    }

    private void checkLocationON() {
        mDeviceLocation.checkLocation(this);
    }

    public void enableButtonNext(boolean value) {
        buttonNextStep.setEnabled(value);
        buttonNextStep.setText("NEXT");
        next = true;

    }

    public void checkPermissionsM() {
        ActivityCompat.requestPermissions(PermissionActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_CODE_PERMISSION_M_COARSE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_M_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    mAllowedPermissions.setACCESS_COARSE_LOCATION(true);
                    checkActivePermissions();
                } else {
                    // permission denied. Disable the
                    // functionality that depends on this permission.
                    mAllowedPermissions.setACCESS_COARSE_LOCATION(false);
                    checkActivePermissions();
                }
                return;
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == buttonNextStep) {
            if (next) {
                startActivity(new Intent(this, ConnectDeviceAPI18Activity.class));
            } else {
                check();
            }
        }
    }


}
