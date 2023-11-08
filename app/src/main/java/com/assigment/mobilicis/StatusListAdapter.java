package com.assigment.mobilicis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StatusListAdapter extends RecyclerView.Adapter<StatusListAdapter.DeviceInfoViewHolder> {

    private List<DeviceInfoData> deviceInfoList;

    public StatusListAdapter(List<DeviceInfoData> deviceInfoList) {
        this.deviceInfoList = deviceInfoList;
    }

    @NonNull
    @Override
    public DeviceInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device_info, parent, false);
        return new DeviceInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceInfoViewHolder holder, int position) {
        DeviceInfoData deviceInfo = deviceInfoList.get(position);

        // Bind data to the views
        holder.textDate.setText("Date: " + deviceInfo.getDate()+ "\n");
        holder.textAccelerometer.setText("Accelerometer Status : "+deviceInfo.getAccelerometer()+ "\n\n");
        holder.textBluetooth.setText("Bluetooth Status : "+deviceInfo.getBluetooth() + "\n\n" );
        holder.textGPS.setText("GPS Status : "+deviceInfo.getGps()+ "\n\n");
        holder.textGyroscope.setText("GyroScope Status : "+deviceInfo.getGyroscope()+ "\n");
        holder.textRoot.setText("Root Status "+deviceInfo.getRoot()+ "\n\n");
        holder.FrontCam.setText("Front Camera Status "+ deviceInfo.getFrontCam()+ "\n\n");
        holder.BackCam.setText("Back Camera Status " + deviceInfo.getBackCam());
    }

    @Override
    public int getItemCount() {
        return deviceInfoList.size();
    }

    static class DeviceInfoViewHolder extends RecyclerView.ViewHolder {
        TextView textDate;
        TextView textAccelerometer;
        TextView textBluetooth;
        TextView textGPS;
        TextView textGyroscope;
        TextView textRoot;
        TextView BackCam;
        TextView FrontCam;

        DeviceInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            textDate = itemView.findViewById(R.id.textDate);
            textAccelerometer = itemView.findViewById(R.id.textAccelerometer);
            textBluetooth = itemView.findViewById(R.id.textBluetooth);
            textGPS = itemView.findViewById(R.id.textGPS);
            textGyroscope = itemView.findViewById(R.id.textGyroscope);
            textRoot = itemView.findViewById(R.id.textRoot);
            BackCam = itemView.findViewById(R.id.BackCam);
            FrontCam = itemView.findViewById(R.id.FrontCam);
        }
    }
}
