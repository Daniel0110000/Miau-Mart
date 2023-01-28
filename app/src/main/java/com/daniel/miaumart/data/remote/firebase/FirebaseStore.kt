package com.daniel.miaumart.data.remote.firebase

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun FirebaseStorage.getProfileImage(fileName: String): Uri {
    return getReference("profile_images/$fileName").downloadUrl.await()
}

fun FirebaseStorage.insertImage(image: Uri, fileName: String) {
    getReference("profile_images").child(fileName).putFile(image)
}

suspend fun FirebaseStorage.updateProfileImage(image: Uri, fileName: String): Int {
    return suspendCoroutine { count ->
        var codeResult = 0
        getReference("profile_images/$fileName").putFile(image)
            .addOnSuccessListener {
                codeResult = 1
                count.resume(codeResult)
            }
            .addOnFailureListener {
                codeResult = 0
                count.resume(codeResult)
            }
    }
}