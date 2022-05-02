package easv.familiytracker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import easv.familiytracker.repository.FamilyMembersDB
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class FMemberDetailActivity : AppCompatActivity() {

    val TAG = "xyz"
    private val PERMISSION_REQUEST_CODE = 1
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP = 102

    val db = FamilyMembersDB()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)


        val FMNameEditText = findViewById<EditText>(R.id.FamilyMemberName)
        val FMPhoneNumberEditText = findViewById<EditText>(R.id.FamilyMemberPhone)
        val FMName = intent.getStringExtra("Extra_Name").toString()
        val FMPhone = intent.getStringExtra("Extra_Phone").toString()
        val FMId = intent.getStringExtra("Extra_Id").toString()
        val btnSave = findViewById<Button>(R.id.SaveFamilyMemberButton)
        val btnBack = findViewById<Button>(R.id.GoBackButton)


        //Sets Name and Phone Number.
        //setFMValues(FMNameEditText, FMName, FMPhoneNumberEditText, FMPhone)

        btnBack.setOnClickListener(){finish()}



        btnSave.setOnClickListener {
            val task = Thread(
                Runnable {
                    saveMember()
                }
            )
            task.start()
        }
        checkPermissions()


    }


    fun takeByBitmap(view: View) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP)
        bitmapCallback.launch(intent)
    }

    val bitmapCallback = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ activityResult ->
        val mImage = findViewById<ImageView>(R.id.FamilyMemberImage)
        if (activityResult.resultCode == RESULT_OK) {
            val extras = activityResult.data!!.extras
            val imageBitmap = extras!!["data"] as Bitmap
            showImageFromBitmap(mImage, imageBitmap)
        } else handleOther(activityResult.resultCode)
    }


    private fun showImageFromBitmap(img: ImageView, bmap: Bitmap) {
        img.setImageBitmap(bmap)
        //img.setLayoutParams(RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        img.setBackgroundColor(Color.RED)
    }


    //Method that sets Name and Phone Number.
    private fun setFMValues(editTextName: EditText, name :String, editTextPhone:EditText, phone :String){
        editTextName.setText(name)
        editTextPhone.setText(phone)
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        val permissions = mutableListOf<String>()
        if ( ! isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) ) permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if ( ! isGranted(Manifest.permission.CAMERA) ) permissions.add(Manifest.permission.CAMERA)
        if (permissions.size > 0)
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), PERMISSION_REQUEST_CODE)
    }

    private fun isGranted(permission: String): Boolean =
        ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    private fun saveMember() {
        val txtName = findViewById<EditText>(R.id.FamilyMemberName)
        val txtPhone = findViewById<EditText>(R.id.FamilyMemberPhone)

        db.createMember(txtName.text.toString(), txtPhone.text.toString(), "", "")
        finish()
    }

    private fun getOutputMediaFile(folder: String): File? {
        // in an emulated device you can see the external files in /sdcard/Android/data/<your app>.
        val mediaStorageDir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), folder)
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory")
                return null
            }
        }

        // Create a media file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val postfix = "jpg"
        val prefix = "IMG"
        return File(mediaStorageDir.path +
                File.separator + prefix +
                "_" + timeStamp + "." + postfix)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        var cameraGranted = true
        var fileGranted = true
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (i in 0..permissions.size - 1) {
                if (permissions[i] == Manifest.permission.CAMERA && grantResults[i] == PackageManager.PERMISSION_DENIED)
                    cameraGranted = false
                if (permissions[i] == Manifest.permission.WRITE_EXTERNAL_STORAGE && grantResults[i] == PackageManager.PERMISSION_DENIED)
                    fileGranted = false
            }
        }
        if (!cameraGranted) {
            val btnByBitmap = findViewById<Button>(R.id.TakeImageButton)
            btnByBitmap.isEnabled = false;
        }
    }

    private fun handleOther(resultCode: Int) {
        if (resultCode == RESULT_CANCELED)
            Toast.makeText(this, "Canceled...", Toast.LENGTH_LONG).show()
        else Toast.makeText(this, "Picture NOT taken - unknown error...", Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val profilePic = findViewById<ImageView>(R.id.FamilyMemberImage)
        when (requestCode) {
            CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP ->
                if (resultCode == RESULT_OK) {
                    val extras = data!!.extras
                    val imageBitmap = extras!!["data"] as Bitmap
                    showImageFromBitmap(profilePic, imageBitmap)
                } else handleOther(resultCode)
        }
        }

    }
