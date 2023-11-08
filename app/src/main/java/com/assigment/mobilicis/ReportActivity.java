package com.assigment.mobilicis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StatusListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);


        recyclerView = findViewById(R.id.recyclerView);

        // Create a list of DeviceInfoData objects
      //  List<DeviceInfoData> deviceInfoList = generateDeviceInfoData();

        // Initialize the adapter



        // Set the layout manager for the RecyclerView (e.g., LinearLayoutManager)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getData();

    }

    private void getData(){

        List<DeviceInfoData> deviceInfoList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Mobilicis");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Iterate through the expected keys

                for (DataSnapshot childsnap : dataSnapshot.getChildren()){
                    String[] expectedKeys = {"Accelerometer", "Bluetooth", "GPS", "GyroScope", "Root","BackCam","FrontCam"};

                    // Create a DeviceInfoData object with initial null values for all keys
                    DeviceInfoData deviceInfo = new DeviceInfoData("2023-11-08", null, null, null, null, null,null,null);

                    deviceInfo.setDate(childsnap.getKey());
                    // Update the values for keys that exist in the database
                    for (String expectedKey : expectedKeys) {
                        if (childsnap.hasChild(expectedKey)) {
                            String childValue = childsnap.child(expectedKey).getValue(String.class);
                            // Update the value for the corresponding key
                            switch (expectedKey) {
                                case "Accelerometer":
                                    deviceInfo.setAccelerometer(childValue);
                                    break;
                                case "Bluetooth":
                                    deviceInfo.setBluetooth(childValue);
                                    break;
                                case "GPS":
                                    deviceInfo.setGps(childValue);
                                    break;
                                case "GyroScope":
                                    deviceInfo.setGyroscope(childValue);
                                    break;
                                case "Root":
                                    deviceInfo.setRoot(childValue);
                                    break;
                                case "BackCam":
                                    deviceInfo.setBackCam(childValue);
                                    break;
                                case "FrontCam":
                                    deviceInfo.setFrontCam(childValue);
                                    break;
                            }
                        }
                    }

                    // Add the DeviceInfoData object to the list
                    deviceInfoList.add(deviceInfo);
                    Log.d("recyclerview",deviceInfoList.toString());
                    adapter = new StatusListAdapter(deviceInfoList);

                    // Set the adapter for the RecyclerView
                    recyclerView.setAdapter(adapter);
                    // Update the RecyclerView with the deviceInfoList
                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors here
            }
        });



//        reference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//            @Override
//            public void onSuccess(DataSnapshot dataSnapshot) {
//
//                String[] expectedKeys = {"Accelerometer", "Bluetooth", "GPS", "Gyroscope", "Root"};
//                DeviceInfoData deviceInfo = new DeviceInfoData("2023-11-08", null, null, null, null, null);
//
////                String name = DataSnapshot.class.
//                deviceInfo.setDate(DataSnapshot.class.getName().toString());
//
//                for (String expectedKey : expectedKeys) {
//                    if (dataSnapshot.hasChild(expectedKey)) {
//                        String childValue = dataSnapshot.child(expectedKey).getValue(String.class);
//
//                        switch (expectedKey) {
//                            case "Accelerometer":
//                                deviceInfo.setAccelerometer(childValue);
//                                break;
//                            case "Bluetooth":
//                                deviceInfo.setBluetooth(childValue);
//                                break;
//                            case "GPS":
//                                deviceInfo.setGps(childValue);
//                                break;
//                            case "Gyroscope":
//                                deviceInfo.setGyroscope(childValue);
//                                break;
//                            case "Root":
//                                deviceInfo.setRoot(childValue);
//                                break;
//                        }
//                    }
//                    else{
//                        Log.d("recyclerview1",deviceInfoList.toString());
//                    }
//                }
//
//                deviceInfoList.add(deviceInfo);
//
//                Log.d("recyclerview",deviceInfoList.toString());
//                adapter = new StatusListAdapter(deviceInfoList);
//
//                // Set the adapter for the RecyclerView
//                recyclerView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//
////
////                    String childKey = childSnapshot.getKey(); // The key (e.g., Accelerometer, Bluetooth, GPS, Gyroscope, Root)
////                    String childValue = childSnapshot.getValue(String.class); // The corresponding value
////
////                    if (childKey == "")
//
//                // Handle the data here based on the childKey and childValue
//                // For example, you can create DeviceInfoData objects and add them to a list.
//
//
//            }
//        });
    }

//    private List<DeviceInfoData> generateDeviceInfoData() {
//        // Create and populate a list of DeviceInfoData objects
//        List<DeviceInfoData> data = new ArrayList<>();
//
//        // Replace with Firebase data retrieval logic
//        // Example:
//        data.add(new DeviceInfoData("2023-11-08", "Accelerometer info", "Bluetooth info", "GPS info", "Gyroscope info", "Root status info"));
//        // Add more data as needed
//
//        return data;
//    }
}