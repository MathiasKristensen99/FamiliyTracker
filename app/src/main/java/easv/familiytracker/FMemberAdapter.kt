package easv.familiytracker

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import easv.familiytracker.models.BEFMember

class FMemberAdapter (context: Context, private val fmembers: Array<BEFMember>) : ArrayAdapter<BEFMember>(context, 0, fmembers) {
    override fun getView(position: Int, v: View?, parent: ViewGroup): View {
        var v1: View? = v
        if (v1 == null) {
            val mInflater = LayoutInflater.from(context)
            v1 = mInflater.inflate(R.layout.cell, null)

        }
        val resView: View = v1!!
        //resView.setBackgroundColor(colours[position % colours.size])
        val f = fmembers[position]
        val nameView = resView.findViewById<TextView>(R.id.txtName)
        val phoneView = resView.findViewById<TextView>(R.id.txtPhone)
        val profilePic = resView.findViewById<ImageView>(R.id.imgProfilePic)

        nameView.text = f.name
        phoneView.text = f.phone

        val uri = Uri.parse(f.picture)

        if (f.picture.equals("")) {
            profilePic.setImageResource(R.drawable.defaultpic)
        } else
            profilePic.setImageURI(uri)

        return resView

    }


}