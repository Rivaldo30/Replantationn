package com.sobi.replantation.ui

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import coil.load
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.sobi.replantation.R
import com.sobi.replantation.databinding.ActivityDetailBuktiBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DetailBuktiActivity : AppCompatActivity() {

private lateinit var binding: ActivityDetailBuktiBinding
private val CAMERA_REQUEST_CODE = 1
private val GALLERY_REQUEST_CODE = 2

    var photoFile: File? = null
    var mCurrentPhotoPath: String? = null
    val CAPTURE_IMAGE_REQUEST = 123


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBuktiBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val imageView = findViewById<ImageView>(R.id.fotoDokumentasi)
//
//        val ambilFoto = findViewById<ImageButton>(R.id.ambil_foto)


        binding.ambilFile.setOnClickListener{
            cameraCheckPermission()
        }

        binding.ambilFileGaleri.setOnClickListener {
            galleryCheckPermission()
        }

        binding.ambilFoto.setOnClickListener {
            cameraCheckPermission()
        }

        binding.ambilFotoGaleri.setOnClickListener{
            galleryCheckPermission()
        }

        //when you click the form image
        binding.formDokumentasi.setOnClickListener {
            val pictureDialog = AlertDialog.Builder(this)
            pictureDialog.setTitle("Select Action")
            val pictureDialogItem = arrayOf("Select photo from Gallery",
                "Capture photo from Camera")
            pictureDialog.setItems(pictureDialogItem){dialog, which ->
                when(which){
                    0 -> gallery()
                    1 -> camera()
                }
            }
            pictureDialog.show()
        }

        //when you click the image
        binding.fotoDokumentasi.setOnClickListener{
            val pictureDialog = AlertDialog.Builder(this)
            pictureDialog.setTitle("Select Action")
            val pictureDialogItem = arrayOf("Select photo from Gallery",
                                            "Capture photo from Camera")
            pictureDialog.setItems(pictureDialogItem){dialog, which ->
                when(which){
                    0 -> gallery()
                    1 -> camera()
                }
            }
            pictureDialog.show()
        }

    }

    private fun galleryCheckPermission(){
        Dexter.withContext(this).withPermission(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : PermissionListener  {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                gallery()
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(this@DetailBuktiActivity,
                                "You have denied the storage permission to select image",
                                Toast.LENGTH_SHORT).show()

                showRotationalDialogForPermission()

            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?,
                p1: PermissionToken?
            ) {
                showRotationalDialogForPermission()
            }


        }).onSameThread().check()
    }

    private fun gallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun cameraCheckPermission(){
        Dexter.withContext(this)
            .withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA).withListener(

                object : MultiplePermissionsListener{
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {

                            if (report.areAllPermissionsGranted()){
                                camera()
                            }

                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        showRotationalDialogForPermission()
                    }
                }

            ).onSameThread().check()
    }

    private fun camera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {

            // Create the File where the photo should go

            try {
                photoFile = createImageFile()
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    val photoURI = FileProvider.getUriForFile(
                        this,
                        "com.sobi.replantation",
                        photoFile!!
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST)
                }
            } catch (ex: Exception) {
                // Error occurred while creating the File
                displayMessage(baseContext, ex.message.toString())
            }

        } else {
            displayMessage(baseContext, "Null")
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    private fun displayMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                CAMERA_REQUEST_CODE->{
                    val bitmap = data?.extras?.get("data") as Bitmap
                    binding.fotoDokumentasi.load(bitmap){
                        crossfade(true)
                        crossfade(1000)
                    }
                }

                GALLERY_REQUEST_CODE->{
                    binding.fotoDokumentasi.load(data?.data){
                        crossfade(true)
                        crossfade(1000)
                    }

                }

            }
        }
    }



    private fun showRotationalDialogForPermission(){
        AlertDialog.Builder(this)
                    .setMessage("It look like you have turned off permissions"
                    +"required for this feature. It can be enable under App settings!!!")

                    .setPositiveButton("GO TO SETTINGS"){_,_->

                        try{

                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivity(intent)

                        }catch (e: ActivityNotFoundException){
                            e.printStackTrace()
                        }
                    }

            .setNegativeButton("CANCEL"){dialog, _->
                dialog.dismiss()
            }.show()

    }

}