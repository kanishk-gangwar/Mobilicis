package com.assigment.mobilicis;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity {


    private File photoFile;
    private final int CAPTURE_IMAGE_REQUEST = 1;
    private String mCurrentPhotoPath;
    private PreviewView viewFinder;
    private ProcessCameraProvider cameraProvider;
    private ImageView imageView;
    private Button photoButton;
    private final ExecutorService cameraExecutor = Executors.newSingleThreadExecutor();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        viewFinder = findViewById(R.id.previewView);
        photoButton = findViewById(R.id.Capture);
        imageView = findViewById(R.id.imageView);

        startCamera();

    }


    private void startCamera() {
        viewFinder.post(new Runnable() {
            @Override
            public void run() {
                final ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                        ProcessCameraProvider.getInstance(CameraActivity.this);
                cameraProviderFuture.addListener(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            cameraProvider = cameraProviderFuture.get();
                            bindCameraUseCases(cameraProvider);
                        } catch (Exception e) {
                            Log.e("CameraProvider1", "Error initializing camera provider.", e);
                        }
                    }
                }, ContextCompat.getMainExecutor(CameraActivity.this));
            }
        });
    }

    private void bindCameraUseCases(ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(viewFinder.getSurfaceProvider());

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        ImageCapture imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        FaceDetectorOptions realTimeOpts = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .build();
        runOnUiThread(()->{
            checkFun(viewFinder.getBitmap());
        });
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
        preview.setSurfaceProvider(viewFinder.getSurfaceProvider());

        FaceDetector detector = FaceDetection.getClient(realTimeOpts);

        Log.d("Kanishk",viewFinder.getBitmap().toString()+"1");

        runOnUiThread(()->{
            checkFun(viewFinder.getBitmap());
        });
        viewFinder.post(() -> {
            checkFun(viewFinder.getBitmap());
        });




    }

    private void checkFun(Bitmap frameBitmap) {
        if (frameBitmap != null) {
            final FaceDetectorOptions realTimeOpts = new FaceDetectorOptions.Builder()
                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                    .build();

            final FaceDetector detector = FaceDetection.getClient(realTimeOpts);

            final InputImage inputImage = InputImage.fromBitmap(frameBitmap, 0);

            detector.process(inputImage)
                    .addOnSuccessListener(faces -> {
                        runOnUiThread(() -> {
                            if (faces.size() > 0) {
                                Log.d("Kanishk1","Vis");
                                imageView.setVisibility(View.VISIBLE);
                            } else {
                                Log.d("Kanishk1","InVis");
                                imageView.setVisibility(View.GONE);
                            }
                        });
                    })
                    .addOnCompleteListener(task -> {
                    });
        }else{
            Log.d("Kanishk1","Null");
        }
    }


//    private void checkFun(Bitmap frameBitmap) {
//        final FaceDetectorOptions realTimeOpts = new FaceDetectorOptions.Builder()
//                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
//                .build();
//
//        final FaceDetector detector = FaceDetection.getClient(realTimeOpts);
//
//        final InputImage inputImage = InputImage.fromBitmap(frameBitmap, 0);
//
//        detector.process(inputImage)
//                .addOnSuccessListener(faces -> {
//                    runOnUiThread(() -> {
//                        if (faces.size() > 0) {
//                            imageView.setVisibility(View.VISIBLE);
//                        } else {
//                            imageView.setVisibility(View.GONE);
//                        }
//                    });
//                })
//                .addOnCompleteListener(task -> {
//                });
//    }
}