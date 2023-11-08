package com.assigment.mobilicis

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.UseCaseGroup
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.FirebaseDatabase
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects
import java.util.concurrent.Executors


class MainActivity2 : AppCompatActivity() {


    var photoFile: File? = null
    private val CAMERA_PERMISSION_REQUEST_CODE = 101
    var mCurrentPhotoPath: String? = null
    private lateinit var viewFinder: PreviewView
    lateinit var cameraProvider: ProcessCameraProvider
    lateinit var imageView : ImageView
   // lateinit var photoButton : Button
    private val cameraExecutor = Executors.newSingleThreadExecutor()
    private val REQUIRED_PERMISSIONS = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE, // Add any other permissions your app may need
        // Add more permissions if needed
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        viewFinder =findViewById<PreviewView>(R.id.previewView)

        //photoButton = findViewById(R.id.Capture)

        val intent = intent

        if (intent.hasExtra("TypeCam")) {
            // Get the extra data using the key
            val receivedData = intent.getStringExtra("TypeCam")
            if (receivedData == "Front"){
                StartFrontCam()
            }else{
                startCamera()
            }

            // Use the received data as needed (e.g., set it to a TextView)
            // textView.setText(receivedData);
        }

      imageView=findViewById<ImageView>(R.id.imageView)
        if (isCameraPermissionGranted()) {
            // Camera permission is already granted, you can proceed with your camera-related operations.
        } else {
            // Request camera permission
            requestCameraPermission()
        }

       // startCamera()




    }

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission is granted .
                startCamera()
            } else {
                // Camera permission is denied. You may want to handle this case.
            }
        }
    }

    private fun SendtoDatabase(Path: String, status: String) {
        val currentDate = Date()

        // Define a date format
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        // Format the current date as a string
        val formattedDate = dateFormat.format(currentDate)

//        Map<String, Object> data = new HashMap<>();
//        data.put("Type", Path);
//        data.put("status", status);
        FirebaseDatabase.getInstance().reference.child("Mobilicis").child(formattedDate).child(Path)
            .setValue(status).addOnSuccessListener {
                Toast.makeText(
                    this@MainActivity2,
                    "Successfully Saved",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }



    private fun startCamera() {
        viewFinder.post(Runnable {
            val cameraProviderFuture =
                ProcessCameraProvider.getInstance(this)
            cameraProviderFuture.addListener({
                try {
                    cameraProvider = cameraProviderFuture.get()
                    Log.d("Camera", cameraProvider.toString())
                    bindCameraUseCases(cameraProvider)
                } catch (e: java.lang.Exception) {
                    Log.e("CameraProvider", "Error initializing camera provider.", e)
                }
            }, ContextCompat.getMainExecutor(this))
        })
    }


    private fun bindCameraUseCases(cameraProvider: ProcessCameraProvider) {
        Log.d("Kanishk", "dfags")
        val preview = Preview.Builder().build()
        preview.setSurfaceProvider(viewFinder.getSurfaceProvider())
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        val useCaseGroup = UseCaseGroup.Builder()
            .addUseCase(preview)
            .build()
        CoroutineScope(Dispatchers.IO).launch {
            while (true){
                delay(200L)
                Log.d("Kanishk", "asda")
                runOnUiThread{
                    viewFinder.bitmap?.let {
                        CoroutineScope(Dispatchers.IO).launch{
                            checkFun(it,"BackCam")
                        }
                    }
                }


            }
        }
        Log.d("Kanishk", "gdesf")

        val faceDetector = FaceDetection.getClient()
        cameraProvider.bindToLifecycle(this, cameraSelector, useCaseGroup)
        preview.setSurfaceProvider(viewFinder.getSurfaceProvider())
        Log.d("Kanishk", getInputImage(viewFinder).toString())


    }

    private fun StartFrontCam() {
        viewFinder.post(Runnable {
            val cameraProviderFuture =
                ProcessCameraProvider.getInstance(this)
            cameraProviderFuture.addListener({
                try {
                    cameraProvider = cameraProviderFuture.get()
                    Log.d("Camera", cameraProvider.toString())
                    bindFrontCamera(cameraProvider)
                } catch (e: java.lang.Exception) {
                    Log.e("CameraProvider", "Error initializing camera provider.", e)
                }
            }, ContextCompat.getMainExecutor(this))
        })
    }

    private fun bindFrontCamera(cameraProvider: ProcessCameraProvider) {
        Log.d("Kanishk", "dfags")
        val preview = Preview.Builder().build()
        preview.setSurfaceProvider(viewFinder.getSurfaceProvider())
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()
        val useCaseGroup = UseCaseGroup.Builder()
            .addUseCase(preview)
            .build()
        CoroutineScope(Dispatchers.IO).launch {
            while (true){
                delay(200L)
                Log.d("Kanishk", "asda")
                runOnUiThread{
                    viewFinder.bitmap?.let {
                        CoroutineScope(Dispatchers.IO).launch{
                            checkFun(it,"FrontCam")
                        }
                    }
                }


            }
        }
        Log.d("Kanishk", "gdesf")

        val faceDetector = FaceDetection.getClient()
        cameraProvider.bindToLifecycle(this, cameraSelector, useCaseGroup)
        preview.setSurfaceProvider(viewFinder.getSurfaceProvider())
        Log.d("Kanishk", getInputImage(viewFinder).toString())


    }

    private fun getInputImage(previewView: PreviewView): InputImage? {
        val rotationDegrees = previewView.display.rotation
        return Objects.requireNonNull(previewView.bitmap)
            ?.let { InputImage.fromBitmap(it, rotationDegrees) }
    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }



    private fun checkFun(frameBitmap :Bitmap,Path: String) {
        val Facedetect = findViewById<TextView>(R.id.faceDetectionResult)
        val realTimeOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode( FaceDetectorOptions.PERFORMANCE_MODE_FAST )
            .build()
        val detector = FaceDetection.getClient(realTimeOpts)
        val inputImage = InputImage.fromBitmap( frameBitmap , 0 )
        detector.process(inputImage)
            .addOnSuccessListener { faces ->
                runOnUiThread{
                    if(faces.isNotEmpty()) {
                        SendtoDatabase(Path,"Face Detected")
                        Facedetect.text = "Face detected"
                        imageView.visibility = View.VISIBLE
                    }
                    else {
                        SendtoDatabase(Path,"No Face Available")
                        Facedetect.text = "No Face Available"
                        imageView.visibility = View.GONE
                    }
                }

            }
            .addOnCompleteListener {

            }
    }


}