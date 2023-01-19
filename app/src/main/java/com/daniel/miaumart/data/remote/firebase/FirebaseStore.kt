package com.daniel.miaumart.data.remote.firebase

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

fun FirebaseStorage.insertImage(image: Uri, fileName: String){
    getReference("profile_images").child(fileName).putFile(image)
}

suspend fun FirebaseStorage.getProfileImage(fileName: String): Uri{
    return getReference("profile_images/$fileName").downloadUrl.await()
}