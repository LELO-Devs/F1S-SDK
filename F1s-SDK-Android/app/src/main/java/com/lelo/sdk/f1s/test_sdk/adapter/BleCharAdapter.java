package com.lelo.sdk.f1s.test_sdk.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.lelo.sdk.f1s.R;
import com.lelo.sdk.f1s.test_sdk.TestSDKActivity;
import com.lelo.sdk.f1s.model.BleCharacteristic;

import java.util.List;


public class BleCharAdapter extends RecyclerView.Adapter<BleCharAdapter.MyViewHolder> {

    private String TAG=BleCharAdapter.class.getSimpleName();
    private List<BleCharacteristic> mList;
    private TestSDKActivity activity;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_ble_charachteristic, parent, false);

        return new MyViewHolder(itemView);
    }


    public BleCharAdapter(TestSDKActivity activity, List<BleCharacteristic> list) {
        this.activity=activity;
        this.mList = list;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        BleCharacteristic mBleCharacteristic = mList.get(position);
        Log.d(TAG,position+" "+ mBleCharacteristic.getName()+" "+ mBleCharacteristic.getValue());

        holder.name.setText(mBleCharacteristic.getName());
        holder.uuid.setText(mBleCharacteristic.getUuid());
        holder.value.setText(mBleCharacteristic.getValue());

        if(mBleCharacteristic.isRead()){
            holder.buttonRead.setVisibility(View.VISIBLE);
            holder.buttonRead.setTag(mBleCharacteristic.getUuid());
            holder.buttonRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String shortUuid=view.getTag().toString();
                    Log.d(TAG,"read uuid "+shortUuid);
                    activity.readCharacteristic(shortUuid);
                }
            });
        }else{
            holder.buttonRead.setVisibility(View.GONE);
        }

        if(mBleCharacteristic.isWrite()){
            holder.buttonWrite.setVisibility(View.VISIBLE);
            holder.buttonWrite.setTag(mBleCharacteristic.getUuid());
            holder.buttonWrite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String shortUuid=view.getTag().toString();
                    Log.d(TAG,"write uuid "+shortUuid);
                    activity.writeCharacteristic(shortUuid);
                }
            });
        }else{
            holder.buttonWrite.setVisibility(View.GONE);
        }

        if(mBleCharacteristic.isNotify()){
            holder.buttonNotify.setVisibility(View.VISIBLE);
            holder.buttonNotify.setTag(mBleCharacteristic.getUuid());
            holder.buttonNotify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String shortUuid=view.getTag().toString();
                    Log.d(TAG,"notify uuid "+shortUuid);
                    activity.notifyCharacteristic(shortUuid);
                }
            });
        }else{
            holder.buttonNotify.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name, uuid, value;
        public Button buttonRead, buttonWrite, buttonNotify;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            uuid = (TextView) view.findViewById(R.id.uuid);
            value = (TextView) view.findViewById(R.id.value);
            buttonRead= (Button) view.findViewById(R.id.buttonRead);
            buttonWrite= (Button) view.findViewById(R.id.buttonWrite);
            buttonNotify= (Button) view.findViewById(R.id.buttonNotify);
        }
    }
}
