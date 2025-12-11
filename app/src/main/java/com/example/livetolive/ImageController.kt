package com.example.livetolive

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import java.io.File

object ImageController {
    fun selectPhotoFromGallery(activity: Fragment, code: Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity.startActivityForResult(intent, code)
    }

    fun saveImage(context: Context, uri: Uri) {
        val file = File(context.filesDir, "profile_photo.jpg")

        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputStream = file.outputStream()
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getImageUri(context: Context): Uri? {
        val file = File(context.filesDir, "profile_photo.jpg")
        return if (file.exists()) Uri.fromFile(file) else null
    }
}