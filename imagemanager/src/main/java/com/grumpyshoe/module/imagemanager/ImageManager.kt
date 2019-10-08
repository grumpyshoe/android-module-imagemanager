package com.grumpyshoe.module.imagemanager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri

interface ImageManager {

    var cameraManager: CameraManager
    var galleryManager: GalleryManager
    var imageConverter: ImageConverter

    enum class ImageSources(val permissionRequestCode: Int, val dataRequestCode: Int) {
        CAMERA(3155, 1000),
        GALLERY(5513, 2000);
    }

    fun getImage(
        activity: Activity,
        sources: List<ImageSources>,
        onImageReceived: (Bitmap) -> Unit
    )

    fun getMimeType(imagePath: String): String?
    fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, intent: Intent?): Boolean
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray): Boolean
    fun saveImage(
        context: Context,
        bitmap: Bitmap,
        filename: String,
        path: String,
        compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
        compressQuality: Int = 100
    )

    interface CameraManager {
        fun selectImageFromCamera(activity: Activity): Int
        fun triggerCamera(activity: Activity)
        fun onIntentResult(activity: Activity, isSamsung: Boolean, onResult: (Bitmap) -> Unit): Boolean
    }

    interface GalleryManager {
        fun selectImageFromGallery(activity: Activity): Int
        fun triggerGallery(activity: Activity)
        fun onIntentResult(uri: Uri, activity: Activity, onResult: (Bitmap) -> Unit): Boolean
    }

    interface ImageConverter {
        fun toBase64(filePath: String): String
        fun toBase64(bitmap: Bitmap, compressFormat: Bitmap.CompressFormat, quality: Int): String
    }

}
