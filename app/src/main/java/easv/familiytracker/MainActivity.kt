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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val db = FamilyMembersDB()
        //val adapter = FMemberAdapter(this, FMembersRepository().getAll())
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
            val i = Intent(this, FMemberDetailActivity::class.java)
            startActivity(i)
        }
    }
}