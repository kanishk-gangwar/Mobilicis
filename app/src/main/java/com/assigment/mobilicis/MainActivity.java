package com.assigment.mobilicis;

import static androidx.core.view.ViewCompat.setScaleX;
import static androidx.core.view.ViewCompat.setScaleY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCaseGroup;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.location.LocationManager;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;


import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.face.Landmark;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;

import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceLandmark;
import com.google.mlkit.vision.face.FaceLandmark.LandmarkType;

import com.scottyab.rootbeer.RootBeer;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private CardView frontCameraStatusTextView;
    private CardView backCameraStatusTextView;
    private CardView rootStatus;
    private CardView bluetoothStatus;
    private CardView accelerometerStatus;
    private CardView gyroscopeStatus;
    private CardView gpsStatus;




    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        gpsStatus = findViewById(R.id.gpsStatus);
        accelerometerStatus = findViewById(R.id.accelerometerStatus);
        gyroscopeStatus = findViewById(R.id.gyroscopeStatus);
        bluetoothStatus = findViewById(R.id.bluetoothStatus);
        frontCameraStatusTextView = findViewById(R.id.frontCameraStatus);
        backCameraStatusTextView = findViewById(R.id.backCameraStatus);
        rootStatus = findViewById(R.id.rootStatus);

        Button ReportBtn = findViewById(R.id.ReportBtn);

        ReportBtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this,ReportActivity.class)));

       gpsStatus.setOnClickListener(view -> alertDialog("GPS", checkGPS()));

       backCameraStatusTextView.setOnClickListener(view -> {
           Intent intent = new Intent(MainActivity.this, MainActivity2.class);
           intent.putExtra("TypeCam","Back");
           startActivity(intent);
       });
       frontCameraStatusTextView.setOnClickListener(view -> {
           Intent intent = new Intent(MainActivity.this, MainActivity2.class);
           intent.putExtra("TypeCam","Front");
           startActivity(intent);
       });

       gyroscopeStatus.setOnClickListener(view -> alertDialog("GyroScope" , checkGyro()));

        accelerometerStatus.setOnClickListener(view -> alertDialog("Accelerometer" , checkccelerometer()));


        rootStatus.setOnClickListener(view -> alertDialog("Root" , checkRootStatus()));

        bluetoothStatus.setOnClickListener(view -> alertDialog("Bluetooth" , checkBluetoothStatus()));

    }



    private void alertDialog(String Type, String Status) {
        Context context = this; // Replace 'this' with your activity or fragment context

        // Create a LinearLayout to hold the TextView
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);

        // Create the TextView
        TextView textView = new TextView(context);
        textView.setText(Status);
        textView.setGravity(Gravity.CENTER);

        // Add the TextView to the LinearLayout
        layout.addView(textView);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(Type + " Status");
        builder.setView(layout);

        // Set up the buttons for the dialog
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = textView.getText().toString();
                SendtoDatabase(Type,Status);
                // Handle the entered text as needed
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



//    private void checkCameras() {
//        CameraManager manager = (CameraManager) getSystemService(CAMERA_SERVICE);
//
//        try {
//            String[] cameraIds = manager.getCameraIdList();
//            for (String cameraId : cameraIds) {
//                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
//
//                int facing = characteristics.get(CameraCharacteristics.LENS_FACING);
//
//                if (facing == CameraCharacteristics.LENS_FACING_FRONT) {
//                    // It's a front camera
//                    frontCameraStatusTextView.setText("Front Camera: Available");
//                } else if (facing == CameraCharacteristics.LENS_FACING_BACK) {
//                    // It's a back camera
//                    backCameraStatusTextView.setText("Back Camera: Available");
//                }
//            }
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//            frontCameraStatusTextView.setText("Front Camera: Error");
//            backCameraStatusTextView.setText("Back Camera: Error");
//        } catch (SecurityException e) {
//            e.printStackTrace();
//            frontCameraStatusTextView.setText("Front Camera: Permission Denied");
//            backCameraStatusTextView.setText("Back Camera: Permission Denied");
//        }
//    }

    public String checkRootStatus() {
        RootBeer rootBeer = new RootBeer(this);
        if (rootBeer.isRooted()) {
            return ("Device is Rooted");
        } else {
            return ("Device is Not Rooted");
        }
    }


    public String checkBluetoothStatus() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
           return ("Bluetooth is not supported on this device");
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return null;
                }
                startActivityForResult(enableBtIntent, 1);
            }
            return ("Bluetooth is enabled and functional");
        }
    }


    private void SendtoDatabase(String Path,String status){

        Date currentDate = new Date();

        // Define a date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Format the current date as a string
        String formattedDate = dateFormat.format(currentDate);

//        Map<String, Object> data = new HashMap<>();
//        data.put("Type", Path);
//        data.put("status", status);


        FirebaseDatabase.getInstance().getReference().child("Mobilicis").child(formattedDate).child(Path).setValue(status).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MainActivity.this,"Successfully Saved",Toast.LENGTH_SHORT).show();
            }
        });
    }


    public String checkccelerometer() {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Check for accelerometer
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerSensor != null) {
            String details = "Accelerometer Details:\n" +
                    "Name: " + accelerometerSensor.getName() + "\n" +
                    "Vendor: " + accelerometerSensor.getVendor() + "\n" +
                    "Version: " + accelerometerSensor.getVersion() + "\n" +
                    "Power (mA): " + accelerometerSensor.getPower() + "\n" +
                    "Resolution: " + accelerometerSensor.getResolution() + "\n" +
                    "Range: " + accelerometerSensor.getMaximumRange();
           return ("Accelerometer: Available\n\n" + details);
        } else {
            return ("Accelerometer: Not Available");
        }

    }
    private String checkGyro(){
        // Check for gyroscope
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (gyroscopeSensor != null) {
            String details = "Gyroscope Details:\n" +
                    "Name: " + gyroscopeSensor.getName() + "\n" +
                    "Vendor: " + gyroscopeSensor.getVendor() + "\n" +
                    "Version: " + gyroscopeSensor.getVersion() + "\n" +
                    "Power (mA): " + gyroscopeSensor.getPower() + "\n" +
                    "Resolution: " + gyroscopeSensor.getResolution() + "\n" +
                    "Range: " + gyroscopeSensor.getMaximumRange();
            return ("Gyroscope: Available\n\n" + details);
        } else {
            return ("Gyroscope: Not Available");
        }
    }




    public String checkGPS() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return "GPS: Available";
        //    gpsStatus.setText("GPS: Available");
        } else {
            return "GPS: Not Available";
          //  gpsStatus.setText("GPS: Not Available");
        }
    }
}