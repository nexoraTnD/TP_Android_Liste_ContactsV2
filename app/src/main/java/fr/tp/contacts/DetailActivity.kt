package fr.tp.contacts

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import java.io.FileOutputStream

class DetailActivity : ComponentActivity() {
    private lateinit var photoView: ImageView
    private var photoValue = "avatar_blue"

    private val galleryPicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri == null) return@registerForActivityResult // annulation : la photo en cours est conservée
        try {
            val file = copyPhotoToAppFiles(uri)
            photoValue = file.absolutePath
            showPhoto(photoValue)
        } catch (_: Exception) {
            Toast.makeText(this, "Impossible de lire cette photo.", Toast.LENGTH_SHORT).show()
        }
    }

    // L'aperçu de caméra évite une permission caméra et une configuration de fichier partagé.
    private val cameraCapture = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
        if (bitmap == null) {
            Toast.makeText(this, "Photo annulée ou caméra indisponible.", Toast.LENGTH_LONG).show()
            return@registerForActivityResult
        }
        try {
            val file = File(filesDir, "contact_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it) }
            photoValue = file.absolutePath
            showPhoto(photoValue)
        } catch (_: Exception) {
            Toast.makeText(this, "Impossible d'enregistrer la photo.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val lastName = findViewById<EditText>(R.id.lastNameInput)
        val firstName = findViewById<EditText>(R.id.firstNameInput)
        val phone = findViewById<EditText>(R.id.phoneInput)
        val email = findViewById<EditText>(R.id.emailInput)
        photoView = findViewById(R.id.detailPhoto)
        val index = intent.getIntExtra(EXTRA_INDEX, -1)
        lastName.setText(intent.getStringExtra(EXTRA_LAST_NAME).orEmpty())
        firstName.setText(intent.getStringExtra(EXTRA_FIRST_NAME).orEmpty())
        phone.setText(intent.getStringExtra(EXTRA_PHONE).orEmpty())
        email.setText(intent.getStringExtra(EXTRA_EMAIL).orEmpty())
        photoValue = intent.getStringExtra(EXTRA_PHOTO) ?: "avatar_blue"
        showPhoto(photoValue)

        findViewById<Button>(R.id.choosePhotoButton).setOnClickListener { galleryPicker.launch("image/*") }
        findViewById<Button>(R.id.takePhotoButton).setOnClickListener {
            try { cameraCapture.launch(null) }
            catch (_: Exception) { Toast.makeText(this, "Aucune application caméra disponible.", Toast.LENGTH_LONG).show() }
        }
        findViewById<Button>(R.id.callButton).setOnClickListener {
            val number = phone.text.toString().trim()
            if (number.isEmpty()) { phone.error = "Saisissez un numéro"; return@setOnClickListener }
            openImplicit(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${Uri.encode(number)}")))
        }
        findViewById<Button>(R.id.emailButton).setOnClickListener {
            val address = email.text.toString().trim()
            if (address.isEmpty()) { email.error = "Saisissez une adresse e-mail"; return@setOnClickListener }
            openImplicit(Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", address, null)))
        }
        findViewById<Button>(R.id.saveButton).setOnClickListener {
            val nom = lastName.text.toString().trim()
            val prenom = firstName.text.toString().trim()
            val telephone = phone.text.toString().trim()
            when {
                nom.isEmpty() -> { lastName.error = "Le nom est obligatoire"; lastName.requestFocus() }
                prenom.isEmpty() -> { firstName.error = "Le prénom est obligatoire"; firstName.requestFocus() }
                telephone.isEmpty() -> { phone.error = "Le téléphone est obligatoire"; phone.requestFocus() }
                else -> {
                    setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(EXTRA_INDEX, index)
                        putExtra(EXTRA_LAST_NAME, nom)
                        putExtra(EXTRA_FIRST_NAME, prenom)
                        putExtra(EXTRA_PHONE, telephone)
                        putExtra(EXTRA_EMAIL, email.text.toString().trim())
                        putExtra(EXTRA_PHOTO, photoValue)
                    })
                    finish()
                }
            }
        }
        findViewById<Button>(R.id.cancelButton).setOnClickListener { setResult(Activity.RESULT_CANCELED); finish() }
    }

    private fun showPhoto(value: String) {
        val resource = when (value) {
            "avatar_rose" -> R.drawable.avatar_rose
            "avatar_green" -> R.drawable.avatar_green
            "avatar_blue" -> R.drawable.avatar_blue
            else -> 0
        }
        if (resource != 0) photoView.setImageResource(resource) else {
            val bitmap = BitmapFactory.decodeFile(value)
            if (bitmap != null) photoView.setImageBitmap(bitmap) else photoView.setImageResource(R.drawable.avatar_blue)
        }
    }

    private fun copyPhotoToAppFiles(uri: Uri): File {
        val destination = File(filesDir, "contact_${System.currentTimeMillis()}.jpg")
        contentResolver.openInputStream(uri).use { input ->
            requireNotNull(input) { "Photo indisponible" }
            destination.outputStream().use { output -> input.copyTo(output) }
        }
        return destination
    }

    private fun openImplicit(intent: Intent) {
        try { startActivity(intent) }
        catch (_: ActivityNotFoundException) {
            Toast.makeText(this, "Aucune application compatible n'a été trouvée.", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        const val EXTRA_INDEX = "contact_index"
        const val EXTRA_LAST_NAME = "last_name"
        const val EXTRA_FIRST_NAME = "first_name"
        const val EXTRA_PHONE = "phone"
        const val EXTRA_EMAIL = "email"
        const val EXTRA_PHOTO = "photo"
    }
}
