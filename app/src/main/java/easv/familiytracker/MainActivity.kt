package easv.familiytracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import easv.familiytracker.repository.FMembersRepository

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val adapter = FMemberAdapter(this, FMembersRepository().getAll())

        val lvFMembers = this.findViewById<ListView>(R.id.lvFMembers)
        lvFMembers.adapter = adapter
        lvFMembers.setOnItemClickListener { adapterView, view, position, id ->
            val i = Intent(this, FMemberDetailActivity::class.java)
            startActivity(i)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    fun newFMember(item: MenuItem) {
        val intent = Intent(this, FMemberDetailActivity::class.java)
        startActivity(intent)
    }
}