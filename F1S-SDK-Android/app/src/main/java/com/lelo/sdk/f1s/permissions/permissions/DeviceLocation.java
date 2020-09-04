package com.lelo.sdk.f1s.permissions.permissions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;

public class DeviceLocation {

    String TAG = DeviceLocation.class.getSimpleName();

    boolean dialogShow = true;
    private DeviceLocationListener mListener;

    public void setDeviceLocationChinaListener(DeviceLocationListener listener) {
        this.mListener = listener;
    }

    public void checkLocation(Context context) {
        if (canGetLocation(context) == true) {
            Log.d(TAG, "checkLocation true");
            mListener.onLocationChinaEnabled();
        } else {
            Log.d(TAG, "checkLocation false");
            showSettingsAlert(context);
        }
    }

    public void showSettingsAlert(final Context context) {
        if (dialogShow) {
            dialogShow = false;
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Location");
            alertDialog.setMessage("Please turn on location in settings");
            alertDialog.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(intent);
                            dialog.dismiss();
                        }
                    });

            alertDialog.show();
        }
    }

    public boolean canGetLocation(Context context) {
        boolean result = true;
        LocationManager locationManager = null;
        boolean gpsEnabled = false;
        boolean networkEnabled = false;

        if (locationManager == null)
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        try {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            networkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gpsEnabled && !networkEnabled) {
            result = false;
        } else {
            result = true;
        }

        return result;
    }


}
