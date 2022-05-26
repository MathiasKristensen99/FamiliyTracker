package easv.familiytracker

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.core.graphics.drawable.toBitmap
import android.graphics.Bitmap
import easv.familiytracker.models.BEFMember
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import easv.familiytracker.repository.FamilyMembersDB
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class EditFamilyMemberActivity: AppCompatActivity() {
    private lateinit var familyMembers: FamilyMembers
    var isEditMode: Boolean = false
    var editFamilyMemberId: Int = 0
    val REQUEST_IMAGE_CAPTURE = 1
    var editFamilyMemberObject: BEFMember? = null
    val TAG = "xyz"
    private val PERMISSION_REQUEST_CODE = 1
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE = 101
    var picturePath = ""
    var mFile: File? = null

    val db = FamilyMembersDB()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_detail)
        val errorMessage = "No application found to handle action!"

        val FMName = intent.getStringExtra("Extra_Name").toString()
        val FMPhone = intent.getStringExtra("Extra_Phone").toString()
        val FMId = intent.getStringExtra("Extra_Id").toString()
        val FMLatLong = intent.getStringExtra("Extra_location").toString()
        val FMPicture = intent.getStringExtra("Extra_Picture").toString()

        val currentFM = FamilyMembersDB.FamilyMember(FMId, FMName, FMPhone, FMPicture, "")

        val btnSave = findViewById<Button>(R.id.SaveFamilyMemberButton)
        val btnBack = findViewById<Button>(R.id.GoBackButton)
        val btnCall = findViewById<Button>(R.id.FamilyMemberCallButton)
        val btnSMS = findViewById<Button>(R.id.FamilyMemberSMSButton)
        val btnDelete = findViewById<Button>(R.id.DeleteFamilyMemberButton)
        val btnLocation = findViewById<Button>(R.id.FamilyMemberLocationButton)
        val picture = findViewById<ImageView>(R.id.FamilyMemberImage)

        val uri = Uri.parse(FMPicture)

        if (FMPicture.equals("")) {
            picture.setImageResource(R.drawable.defaultpic)
        } else
            picture.setImageURI(uri)

        val FMNameEditText = findViewById<EditText>(R.id.FamilyMemberName)
        val FMPhoneNumberEditText = findViewById<EditText>(R.id.FamilyMemberPhone)
        setFMValues(FMNameEditText, FMName, FMPhoneNumberEditText, FMPhone)

        checkPermissions();

        btnSMS.setOnClickListener { sendSMS() }
        btnBack.setOnClickListener { finish() }
        btnCall.setOnClickListener { openDialer() }
        btnLocation.setOnClickListener { (seeFMLocation(FMName, FMLatLong)) }
        btnSave.setOnClickListener {
            val task = Thread(
                Runnable {
                    updateMember()
                }
            )
            task.start()
        }

        btnDelete.setOnClickListener{
            val task = Thread(
                Runnable{
                    deleteMemberById(FMId, FMName, FMPhone)
                }
            )
            task.start()
        }

    }
    fun seeFMLocation(fMName : String, latLong : String){
        val FMBundle = Bundle()
        FMBundle.putString("Extra_Name", fMName)
        FMBundle.putString("Extra_latLong", latLong)

        val i = Intent(this,PersonLocationActivity::class.java)
        i.putExtras(FMBundle)
        startActivity(i)
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

    fun deleteMemberById(id : String, name : String, phone : String){
        db.deleteMember(id, name, phone, "", "")
        finish()
    }

    private fun setFMValues(editTextName: EditText, name :String, editTextPhone:EditText, phone :String){
        editTextName.setText(name)
        editTextPhone.setText(phone)
    }

    private fun updateMember() {
        val FMNameEditText = findViewById<EditText>(R.id.FamilyMemberName)
        val FMPhoneNumberEditText = findViewById<EditText>(R.id.FamilyMemberPhone)
        val FMId = intent.getStringExtra("Extra_Id").toString()

        db.updateMember(FMId, FMNameEditText.text.toString(), FMPhoneNumberEditText.text.toString(), picturePath, "55.465,8.44995")
        finish()
    }

    private fun openDialer() {
        val FMPhone = intent.getStringExtra("Extra_Phone").toString()
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$FMPhone")
        startActivity(intent)
    }

    private fun sendSMS() {
        val FMPhone = intent.getStringExtra("Extra_Phone").toString()
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("smsto:$FMPhone")
        startActivity(intent)
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
        val applicationId = "easv.familiytracker"
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
            this,
            "${applicationId}.provider",  //use your app signature + ".provider"
            mFile!!))

        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val mImage = findViewById<ImageView>(R.id.FamilyMemberImage)
        val tvImageInfo = findViewById<TextView>(R.id.tvImageInfo)
        when (requestCode) {

            CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE ->
                if (resultCode == RESULT_OK)
                    showImageFromFile(mImage, tvImageInfo, mFile!!)

                else handleOther(resultCode)


        }
    }

    private fun showImageFromFile(img: ImageView, txt: TextView, f: File) {
        img.setImageURI(Uri.fromFile(f))
        img.setBackgroundColor(Color.RED)
        picturePath = f.absolutePath
        //mImage.setRotation(90);
    }

    private fun handleOther(resultCode: Int) {
        if (resultCode == RESULT_CANCELED)
            Toast.makeText(this, "Canceled...", Toast.LENGTH_LONG).show()
        else Toast.makeText(this, "Picture NOT taken - unknown error...", Toast.LENGTH_LONG).show()
    }

}