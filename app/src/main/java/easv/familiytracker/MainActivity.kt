package easv.familiytracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import easv.familiytracker.models.BEFMember
import easv.familiytracker.repository.FamilyMembersDB
import easv.familiytracker.repository.ICallback

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = "xyz"
    }

    val db = FamilyMembersDB()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        //val adapter = FMemberAdapter(this, FMembersRepository().getAll())
        db.getAll(object:ICallback{
            override fun familyMembers(members: List<BEFMember>) {
                setupListView(members)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        db.getAll(object:ICallback{
            override fun familyMembers(members: List<BEFMember>) {
                setupListView(members)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    fun newFMember(item: MenuItem) {
        val intent = Intent(this, FMemberDetailActivity::class.java)
        startActivity(intent)
    }

    fun setupListView(members: List<BEFMember>) {
        val adapter = FMemberAdapter(this, members.toTypedArray())
        val lvFMembers = this.findViewById<ListView>(R.id.lvFMembers)
        lvFMembers.adapter = adapter
        lvFMembers.setOnItemClickListener { adapterView, view, position, id ->


            val familyMemberId = adapter.getItem(id.toInt())?.id
            val familyMemberName = adapter.getItem(id.toInt())?.name
            val familyMemberPhone = adapter.getItem(id.toInt())?.phone
            val familyMemberLocation = adapter.getItem(id.toInt())?.location
            val FMIBundle = Bundle()
            FMIBundle.putString("Extra_Name", familyMemberName.toString())
            FMIBundle.putString("Extra_Phone", familyMemberPhone.toString())
            FMIBundle.putString("Extra_Id", familyMemberId.toString())
            FMIBundle.putString("Extra_location", familyMemberLocation.toString())

            val i = Intent(this, EditFamilyMemberActivity::class.java)
            i.putExtras(FMIBundle)
            startActivity(i)
        }
    }
}