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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class EditFamilyMemberActivity: AppCompatActivity() {
    private lateinit var familyMembers: FamilyMembers
    var isEditMode: Boolean = false
    var editFamilyMemberId: Int = 0
    val REQUEST_IMAGE_CAPTURE = 1
    var editFamilyMemberObject: BEFMember? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val errorMessage = "No application found to handle action!"
        if (intent.extras != null) {
            val b = intent.extras!!

            val editId = b.getInt("editFamilyMemberId")
            if (editId != null && editId > 0) {
                isEditMode = true
                editFamilyMemberId = editId
            }
        }

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_detail)


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
}