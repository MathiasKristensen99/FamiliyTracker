package easv.familiytracker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import easv.familiytracker.models.BEFMember
import easv.familiytracker.repository.FamilyMembersDB
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class FMemberDetailActivity : AppCompatActivity() {

    val TAG = "xyz"
    private val PERMISSION_REQUEST_CODE = 1
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE = 101
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP = 102

    var mFile: File? = null
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
        setFMValues(FMNameEditText, FMName, FMPhoneNumberEditText, FMPhone)

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

    private fun showImageFromBitmap(img: ImageView, txt: TextView, bmap: Bitmap) {
        img.setImageBitmap(bmap)
        //img.setLayoutParams(RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        img.setBackgroundColor(Color.RED)
        txt.text = "bitmap - size = " + bmap.byteCount

    }

    // show the image allocated in [f] in imageview [img]. Show meta data in [txt]
    private fun showImageFromFile(img: ImageView, txt: TextView, f: File) {
        img.setImageURI(Uri.fromFile(f))
        img.setBackgroundColor(Color.RED)
        //mImage.setRotation(90);
        txt.text = "File at:" + f.absolutePath + " - size = " + f.length()

    }

    fun onTakeByFile(view: View) {
        mFile = getOutputMediaFile("family_tracker") // create a file to save the image

        if (mFile == null) {
            Toast.makeText(this, "Could not create file...", Toast.LENGTH_LONG).show()
            return
        }

        // create Intent to take a picture
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // Add extra to inform the app where to put the image.
        val applicationId = "easv.family_tracker"
        intent.putExtra(
            MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
            this,
            "${applicationId}.provider",  //use your app signature + ".provider"
            mFile!!))

        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE)

    }

    fun onTakeByBitmap(view: View) {
        if (isGranted(Manifest.permission.CAMERA)) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP)
        } else {

        }
    }

    private fun handleOther(resultCode: Int) {
        if (resultCode == RESULT_CANCELED)
            Toast.makeText(this, "Canceled...", Toast.LENGTH_LONG).show()
        else Toast.makeText(this, "Picture NOT taken - unknown error...", Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val profilePic = findViewById<ImageView>(R.id.imgProfilePic)
        when (requestCode) {

        }
    }
}