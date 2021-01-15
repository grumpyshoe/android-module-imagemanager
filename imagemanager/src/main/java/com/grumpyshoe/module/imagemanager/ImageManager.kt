package com.grumpyshoe.module.imagemanager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import com.grumpyshoe.module.imagemanager.impl.model.ImageObject
import com.grumpyshoe.module.imagemanager.impl.model.PermissionExplanation
import com.grumpyshoe.module.imagemanager.impl.model.SourceChooserDialogValues
import com.grumpyshoe.module.permissionmanager.model.PermissionRequestExplanation

/*
 * ImageObject
 * android-module-imagemanager
 *
 * Created by Thomas Cirksena on 28.05.20.
 * Copyright © 2020 Thomas Cirksena. All rights reserved.
 */
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
        srcChooserDialog: SourceChooserDialogValues = SourceChooserDialogValues(),
        cameraPermissionExplanation: PermissionExplanation? = null,
        cameraPermissionRetryExplanation: PermissionExplanation? = null,
        galleryPermissionExplanation: PermissionExplanation? = null,
        galleryPermissionRetryExplanation: PermissionExplanation? = null,
        onImageReceived: (ImageObject) -> Unit,
        uriOnly: Boolean = false
    )

    fun getMimeType(imagePath: String): String?

    fun onActivityResult(
        activity: Activity,
        requestCode: Int,
        resultCode: Int,
        intent: Intent?
    ): Boolean

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ): Boolean

    fun saveImage(
        context: Context,
        bitmap: Bitmap,
        filename: String,
        path: String,
        compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
        compressQuality: Int = 100
    ): String?

    fun loadImagefromDisk(
        context: Context,
        filename: String,
        path: String
    ): Bitmap?

    fun deleteImageFromDisk(
        context: Context,
        filename: String,
        path: String
    ): Boolean

    interface CameraManager {
        fun selectImageFromCamera(
            activity: Activity,
            permissionExplanation: PermissionExplanation?,
            permissionRetryExplanation: PermissionExplanation?
        ): Int

        fun triggerCamera(activity: Activity)
        fun onIntentResult(
            activity: Activity,
            uriOnly: Boolean,
            onResult: (ImageObject) -> Unit
        ): Boolean
    }

    interface GalleryManager {
        fun selectImageFromGallery(
            activity: Activity,
            permissionExplanation: PermissionExplanation?,
            permissionRetryExplanation: PermissionExplanation?
        ): Int

        fun triggerGallery(activity: Activity)
        fun onIntentResult(uri: Uri, activity: Activity, onResult: (ImageObject) -> Unit): Boolean
    }

    interface ImageConverter {
        fun toBase64(filePath: String): String
        fun toBase64(bitmap: Bitmap, compressFormat: Bitmap.CompressFormat, quality: Int): String
    }

}
