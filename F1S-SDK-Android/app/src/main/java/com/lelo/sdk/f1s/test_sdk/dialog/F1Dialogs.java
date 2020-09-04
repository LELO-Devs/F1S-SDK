package com.lelo.sdk.f1s.test_sdk.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.lelo.sdk.f1s.R;
import com.lelo.sdk.lelo_sdk_f1_android_lib.Utils;

public class F1Dialogs {

    private final String TAG = F1Dialogs.class.getSimpleName();

    private F1DialogsListener mListener;

    public void setListener(F1DialogsListener listener) {
        this.mListener = listener;
    }

    public void removeListener() {
        this.mListener = null;
    }

    //vibrator setting
    private int speedTouch1 = 0;
    private int speedTouch2 = 0;
    private int speedTouch3 = 0;
    private int speedTouch4 = 0;
    private int speedTouch5 = 0;
    private int speedTouch6 = 0;
    private int speedTouch7 = 0;
    private int speedTouch8 = 0;

    public void dialogVibratorSetting(Context context, String value) {

        if (value.length() == 10) {
            speedTouch1 = Utils.hex2Decimal(value.substring(0, 2));
            speedTouch2 = Utils.hex2Decimal(value.substring(2, 4));
            speedTouch3 = Utils.hex2Decimal(value.substring(4, 6));
            speedTouch4 = Utils.hex2Decimal(value.substring(6, 8));
            speedTouch5 = Utils.hex2Decimal(value.substring(8, 10));
            speedTouch6 = Utils.hex2Decimal(value.substring(10, 12));
            speedTouch7 = Utils.hex2Decimal(value.substring(12, 14));
            speedTouch8 = Utils.hex2Decimal(value.substring(14, 16));
        }
        Log.d(TAG, "dialogVibratorSetting speed: " + speedTouch1 + " " + speedTouch2 + " " + speedTouch3 + " " + speedTouch4 + " " + speedTouch5 + " " + speedTouch6 + " " + speedTouch7 + " " + speedTouch8);

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_vibrator_settings);
        dialog.setTitle("VIBRATOR SETTING");
        dialog.setCancelable(true);

        final TextView touch1 = (TextView) dialog.findViewById(R.id.touch1);
        touch1.setTag("1");
        final TextView touch2 = (TextView) dialog.findViewById(R.id.touch2);
        touch2.setTag("2");
        final TextView touch3 = (TextView) dialog.findViewById(R.id.touch3);
        touch3.setTag("3");
        final TextView touch4 = (TextView) dialog.findViewById(R.id.touch4);
        touch4.setTag("4");
        final TextView touch5 = (TextView) dialog.findViewById(R.id.touch5);
        touch5.setTag("5");
        final TextView touch6 = (TextView) dialog.findViewById(R.id.touch6);
        touch6.setTag("6");
        final TextView touch7 = (TextView) dialog.findViewById(R.id.touch7);
        touch7.setTag("7");
        final TextView touch8 = (TextView) dialog.findViewById(R.id.touch8);
        touch8.setTag("8");
        showTextVibratorSetting(touch1, speedTouch1);
        showTextVibratorSetting(touch2, speedTouch2);
        showTextVibratorSetting(touch3, speedTouch3);
        showTextVibratorSetting(touch4, speedTouch4);
        showTextVibratorSetting(touch5, speedTouch5);
        showTextVibratorSetting(touch6, speedTouch6);
        showTextVibratorSetting(touch7, speedTouch7);
        showTextVibratorSetting(touch8, speedTouch8);


        final SeekBar seekBarTouch1 = (SeekBar) dialog.findViewById(R.id.dialogSeekBarTouch1);
        seekBarTouch1.setProgress(speedTouch1);
        seekBarTouch1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedTouch1 = progress;
                showTextVibratorSetting(touch1, speedTouch1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        final SeekBar seekBarTouch2 = (SeekBar) dialog.findViewById(R.id.dialogSeekBarTouch2);
        seekBarTouch2.setProgress(speedTouch2);
        seekBarTouch2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedTouch2 = progress;
                showTextVibratorSetting(touch2, speedTouch2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        final SeekBar seekBarTouch3 = (SeekBar) dialog.findViewById(R.id.dialogSeekBarTouch3);
        seekBarTouch3.setProgress(speedTouch3);
        seekBarTouch3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedTouch3 = progress;
                showTextVibratorSetting(touch3, speedTouch3);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        final SeekBar seekBarTouch4 = (SeekBar) dialog.findViewById(R.id.dialogSeekBarTouch4);
        seekBarTouch4.setProgress(speedTouch4);
        seekBarTouch4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedTouch4 = progress;
                showTextVibratorSetting(touch4, speedTouch4);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        final SeekBar seekBarTouch5 = (SeekBar) dialog.findViewById(R.id.dialogSeekBarTouch5);
        seekBarTouch5.setProgress(speedTouch5);
        seekBarTouch5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedTouch5 = progress;
                showTextVibratorSetting(touch5, speedTouch5);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        final SeekBar seekBarTouch6 = (SeekBar) dialog.findViewById(R.id.dialogSeekBarTouch6);
        seekBarTouch6.setProgress(speedTouch6);
        seekBarTouch6.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedTouch6 = progress;
                showTextVibratorSetting(touch6, speedTouch6);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        final SeekBar seekBarTouch7 = (SeekBar) dialog.findViewById(R.id.dialogSeekBarTouch7);
        seekBarTouch7.setProgress(speedTouch7);
        seekBarTouch7.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedTouch7 = progress;
                showTextVibratorSetting(touch7, speedTouch7);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        final SeekBar seekBarTouch8 = (SeekBar) dialog.findViewById(R.id.dialogSeekBarTouch8);
        seekBarTouch8.setProgress(speedTouch8);
        seekBarTouch8.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedTouch8 = progress;
                showTextVibratorSetting(touch8, speedTouch8);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        Button dialogButtonSave = (Button) dialog.findViewById(R.id.dialogButtonSave);
        dialogButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.fromDialogsWriteVibratorSetting((byte) speedTouch1, (byte) speedTouch2, (byte) speedTouch3, (byte) speedTouch4, (byte) speedTouch5, (byte) speedTouch6, (byte) speedTouch7, (byte) speedTouch8);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showTextVibratorSetting(TextView textView, int value) {
        String tag = textView.getTag().toString();
        textView.setText("Speed" + tag + " - " + (value * 5) + "%");
    }


    public void showDialogUsageLog(Context context) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_usage_log);
        dialog.setTitle("USAGE LOG");
        dialog.setCancelable(true);

        Button dialogButtonClear = (Button) dialog.findViewById(R.id.dialogButtonClear);
        dialogButtonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.fromDialogClearUsageLog();
                dialog.dismiss();
            }
        });

        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    public void showDialogPressPowerButtonToActivateKeyState(Context context) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Press power button of F1 to activate key state.");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private byte speedMainMotor = 0;
    private byte speedVibratorMotor = 0;

    public void showMotorControlDialog(Context context, String value) {

        int speed = 0;
        if (value.length() == 6) {
            String speedString = value.substring(4, 6);
            speed = Utils.hex2Decimal(speedString);
        }

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_motor_control);
        dialog.setTitle("MOTOR CONTROL");
        dialog.setCancelable(true);


        final SeekBar dialogSeekBarMotorSpeed = (SeekBar) dialog.findViewById(R.id.dialogSeekBarMotorSpeed);
        dialogSeekBarMotorSpeed.setProgress(0);
        dialogSeekBarMotorSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                speedMainMotor = (byte) (dialogSeekBarMotorSpeed.getProgress() * 5);
                mListener.fromDialogStartMotor(speedMainMotor, speedVibratorMotor);
            }
        });

        final SeekBar dialogSeekBarVibratorSpeed = (SeekBar) dialog.findViewById(R.id.dialogSeekBarVibratorSpeed);
        dialogSeekBarVibratorSpeed.setProgress(0);
        dialogSeekBarVibratorSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                speedVibratorMotor = (byte) (dialogSeekBarVibratorSpeed.getProgress() * 5);
                mListener.fromDialogStartMotor(speedMainMotor, speedVibratorMotor);
            }
        });

        Button dialogButtonMotorStop = (Button) dialog.findViewById(R.id.dialogButtonMotorStop);
        dialogButtonMotorStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.fromDialogStopMotor();
                dialogSeekBarMotorSpeed.setProgress(0);
                dialogSeekBarVibratorSpeed.setProgress(0);
            }
        });
        Button dialogButtonDeviceTurnOff = (Button) dialog.findViewById(R.id.dialogButtonDeviceTurnOff);
        dialogButtonDeviceTurnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.fromDialogTurnOffDevice();
            }
        });
        Button dialogButtonVerifyAccelerometer = (Button) dialog.findViewById(R.id.dialogButtonVerifyAccelerometer);
        dialogButtonVerifyAccelerometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.fromDialogVerifyAccelerometer();
            }
        });
        Button dialogButtonClose = (Button) dialog.findViewById(R.id.dialogButtonClose);
        dialogButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showWakeUpDialog(Context context, String value) {

        String valueWakeUp = value;
        Log.d(TAG, "showWakeUpDialog " + valueWakeUp);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_wake_up);
        dialog.setTitle("WAKE UP");
        dialog.setCancelable(true);

        final Switch dialogSwitchSensor = (Switch) dialog.findViewById(R.id.dialogSwitchSensor);
        if (valueWakeUp.compareTo("01") == 0) {
            dialogSwitchSensor.setChecked(true);
        } else {
            dialogSwitchSensor.setChecked(false);
        }

        Button dialogButtonSave = (Button) dialog.findViewById(R.id.dialogButtonSave);
        dialogButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.fromDialogWakeUp(dialogSwitchSensor.isChecked());
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void showMotorWorkOnTouchDialog(Context context) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_motor_work_on_touch);
        dialog.setTitle("MOTOR WORK ON TOUCH");
        dialog.setCancelable(true);


        Button dialogButtonDisableCapSensor = (Button) dialog.findViewById(R.id.dialogButtonDisableCapSensor);
        dialogButtonDisableCapSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.fromDialogMotorWorkOnTouchEnableCapSensor(false);
                dialog.dismiss();
            }
        });
        Button dialogButtonEnableCapSensor = (Button) dialog.findViewById(R.id.dialogButtonEnableCapSensor);
        dialogButtonEnableCapSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.fromDialogMotorWorkOnTouchEnableCapSensor(true);
                dialog.dismiss();
            }
        });
        Button dialogButtonEnableCapSensorAndResetDefaultSpeed = (Button) dialog.findViewById(R.id.dialogButtonEnableCapSensorAndResetDefaultSpeed);
        dialogButtonEnableCapSensorAndResetDefaultSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.fromDialogMotorWorkOnTouchEnableCapSensorAndResetDefualtSpeed();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
