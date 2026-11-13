# TP Android — Liste de contacts (Version 2)

Projet Android Studio en Kotlin avec interfaces XML. Il contient deux activités, une ListView personnalisée, trois contacts de démonstration et trois portraits vectoriels temporaires.

## Ouvrir et lancer le projet

1. Téléchargez puis décompressez `TP_Android_Liste_Contacts.zip` dans un dossier accessible.
2. Lancez Android Studio. Sur l’écran d’accueil, cliquez sur **Open** (ou **File > Open** si un projet est déjà ouvert).
3. Sélectionnez le dossier décompressé **TP_Android_Liste_Contacts**, puis cliquez sur **OK**.
4. Si Android Studio demande si vous faites confiance au projet, choisissez **Trust Project**. Attendez la fin de la synchronisation Gradle.
5. Si Android SDK Platform 35 manque, ouvrez **Tools > SDK Manager**, installez Android SDK Platform 35 et cliquez sur **Apply**.
6. Choisissez un appareil en haut de la fenêtre. Pour créer un émulateur : **Tools > Device Manager > Add a new device**, choisissez un modèle, téléchargez une image système, puis démarrez-le avec le bouton de lecture.
7. Cliquez sur **Run ▶** et sélectionnez la configuration **app** si Android Studio le demande.

## Parcours de démonstration

1. Sur l’écran **Mes contacts**, touchez une ligne. La fiche correspondante s’ouvre.
2. Modifiez le nom, le prénom ou le téléphone, puis touchez **Enregistrer**. La ligne est actualisée. Essayez de vider un champ obligatoire pour voir la validation.
3. Modifiez une fiche puis touchez **Annuler** : les changements ne sont pas transmis.
4. Touchez **Appeler** : Android ouvre le composeur avec le numéro. L’application ne lance pas d’appel directement.
5. Saisissez une adresse puis touchez **Envoyer un e-mail** : Android ouvre une application compatible avec le destinataire prérempli.
6. Touchez **Choisir une photo**, sélectionnez une image puis enregistrez. Le nouveau portrait apparaît dans la fiche et la liste. Fermer le sélecteur sans choisir conserve le portrait précédent.
7. Touchez **Prendre une photo**. L’application caméra renvoie un aperçu qui s’affiche dans la fiche. L’annulation ou l’absence de caméra produit un message.

Les exemples et les modifications de coordonnées sont conservés en mémoire pendant l’exécution seulement. Un redémarrage réinitialise la liste d’exemple. Les photos choisies sont copiées dans les fichiers privés de l’application.

## Organisation du projet

- `MainActivity.kt` : données d’exemple, ListView, Intent explicite et retour Activity Result.
- `DetailActivity.kt` : édition, validation, intents d’appel et d’e-mail, galerie et caméra.
- `ContactAdapter.kt` : lignes personnalisées avec photo et coordonnées.
- `res/layout/` : interfaces XML.
- `res/drawable/` : portraits vectoriels de démonstration.

La capture utilise l’aperçu caméra Android (`TakePicturePreview`), dont la résolution dépend de l’appareil. Elle ne nécessite pas de permission caméra déclarée pour l’application.
