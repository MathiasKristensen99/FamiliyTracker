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

    val db = FamilyMembersDB()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_detail)
        val errorMessage = "No application found to handle action!"

        val FMName = intent.getStringExtra("Extra_Name").toString()
        val FMPhone = intent.getStringExtra("Extra_Phone").toString()
        val FMId = intent.getStringExtra("Extra_Id").toString()

        val currentFM = FamilyMembersDB.FamilyMember(FMId, FMName, FMPhone, "", "")

        val btnSave = findViewById<Button>(R.id.SaveFamilyMemberButton)
        val btnBack = findViewById<Button>(R.id.GoBackButton)
        val btnCall = findViewById<Button>(R.id.FamilyMemberCallButton)
        val btnSMS = findViewById<Button>(R.id.FamilyMemberSMSButton)
        val btnDelete = findViewById<Button>(R.id.DeleteFamilyMemberButton)

        val FMNameEditText = findViewById<EditText>(R.id.FamilyMemberName)
        val FMPhoneNumberEditText = findViewById<EditText>(R.id.FamilyMemberPhone)
        setFMValues(FMNameEditText, FMName, FMPhoneNumberEditText, FMPhone)

        btnSMS.setOnClickListener { sendSMS() }
        btnBack.setOnClickListener { finish() }
        btnCall.setOnClickListener { openDialer() }
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

        db.updateMember(FMId, FMNameEditText.text.toString(), FMPhoneNumberEditText.text.toString(), "", "")
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
}