package fr.tp.contacts

/** Modèle de données d'un contact; la photo désigne une illustration ou un fichier privé. */
data class Contact(var lastName: String, var firstName: String, var phone: String, var email: String, var photo: String) {
    fun photoResource(): Int? = when (photo) {
        "avatar_blue" -> R.drawable.avatar_blue
        "avatar_rose" -> R.drawable.avatar_rose
        "avatar_green" -> R.drawable.avatar_green
        else -> null
    }
}
