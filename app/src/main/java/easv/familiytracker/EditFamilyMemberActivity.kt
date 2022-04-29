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

        val btnSave = findViewById<Button>(R.id.SaveFamilyMemberButton)
        val btnBack = findViewById<Button>(R.id.GoBackButton)

        val FMNameEditText = findViewById<EditText>(R.id.FamilyMemberName)
        val FMPhoneNumberEditText = findViewById<EditText>(R.id.FamilyMemberPhone)
        setFMValues(FMNameEditText, FMName, FMPhoneNumberEditText, FMPhone)

        btnSave.setOnClickListener {
            val task = Thread(
                Runnable {
                    updateMember()
                }
            )
            task.start()
        }


/*        if(isEditMode) {
            val getOneObserver = Observer<BEFMember>{ familyMember ->
                if(isEditMode) {
                    editFamilyMemberObject = familyMember
                    val img = familyMember.picture
                    if(img.isNotEmpty())
                        FamilyMemberImage.setImageBitmap(StringToBitMap(img))

                    FamilyMemberName.setText(familyMember.name)
                    FamilyMemberPhone.setText(familyMember.phone)
                }
            }

            familyMembers.getFamilyMemberById(editFamilyMemberId).observe(this, getOneObserver)
        }
*/
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
}