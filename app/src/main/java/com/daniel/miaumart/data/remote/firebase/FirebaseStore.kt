package com.daniel.miaumart.data.remote.firebase

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun FirebaseStorage.getProfileImage(fileName: String): Uri {
    return getReference("profile_images/$fileName").downloadUrl.await()
}

suspend fun FirebaseStorage.updateOrInsertProfileImage(image: Uri, fileName: String): Boolean {
    return suspendCoroutine { count ->
        getReference("profile_images").child(fileName).putFile(image)
            .addOnSuccessListener { count.resume(true) }
            .addOnFailureListener { count.resume(false) }
    }
}