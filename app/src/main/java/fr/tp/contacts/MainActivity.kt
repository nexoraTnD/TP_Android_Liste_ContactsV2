package fr.tp.contacts

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : ComponentActivity() {
    // Les exemples sont en mémoire : aucune base de données n'est nécessaire pour ce TP.
    private val contacts = mutableListOf(
        Contact("Diallo", "Aminata", "+221 77 123 45 67", "aminata.diallo@example.com", "avatar_rose"),
        Contact("Martin", "Lucas", "+33 6 12 34 56 78", "lucas.martin@example.com", "avatar_blue"),
        Contact("Mensah", "Kofi", "+233 24 123 4567", "kofi.mensah@example.com", "avatar_green")
    )
    private lateinit var adapter: ContactAdapter

    // Activity Result API : reçoit les changements quand la fiche se ferme avec Enregistrer.
    private val detailLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data ?: return@registerForActivityResult
            val index = data.getIntExtra(DetailActivity.EXTRA_INDEX, -1)
            if (index in contacts.indices) {
                contacts[index] = Contact(
                    data.getStringExtra(DetailActivity.EXTRA_LAST_NAME).orEmpty(),
                    data.getStringExtra(DetailActivity.EXTRA_FIRST_NAME).orEmpty(),
                    data.getStringExtra(DetailActivity.EXTRA_PHONE).orEmpty(),
                    data.getStringExtra(DetailActivity.EXTRA_EMAIL).orEmpty(),
                    data.getStringExtra(DetailActivity.EXTRA_PHOTO).orEmpty()
                )
                adapter.notifyDataSetChanged() // rafraîchit également le portrait de la ligne
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = findViewById<ListView>(R.id.contactList)
        adapter = ContactAdapter(this, contacts)
        list.adapter = adapter
        list.setOnItemClickListener { _, _, position, _ ->
            val contact = contacts[position]
            // Intent explicite : la destination est la classe DetailActivity.
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_INDEX, position)
                putExtra(DetailActivity.EXTRA_LAST_NAME, contact.lastName)
                putExtra(DetailActivity.EXTRA_FIRST_NAME, contact.firstName)
                putExtra(DetailActivity.EXTRA_PHONE, contact.phone)
                putExtra(DetailActivity.EXTRA_EMAIL, contact.email)
                putExtra(DetailActivity.EXTRA_PHOTO, contact.photo)
            }
            detailLauncher.launch(intent)
        }
    }
}
