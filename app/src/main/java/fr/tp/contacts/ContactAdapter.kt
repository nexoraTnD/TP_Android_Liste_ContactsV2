package fr.tp.contacts

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import java.io.File

/** Construit les lignes personnalisées de la ListView. */
class ContactAdapter(context: Context, private val contacts: MutableList<Contact>) : ArrayAdapter<Contact>(context, 0, contacts) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val row = convertView ?: LayoutInflater.from(context).inflate(R.layout.row_contact, parent, false)
        val contact = contacts[position]
        row.findViewById<TextView>(R.id.rowFullName).text = "${contact.firstName} ${contact.lastName}"
        row.findViewById<TextView>(R.id.rowPhone).text = contact.phone
        val image = row.findViewById<ImageView>(R.id.rowPhoto)
        val resource = contact.photoResource()
        if (resource != null) image.setImageResource(resource) else {
            val file = File(contact.photo)
            val bitmap = if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null
            if (bitmap != null) image.setImageBitmap(bitmap) else image.setImageResource(R.drawable.avatar_blue)
        }
        return row
    }
}
