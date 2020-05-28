package com.grumpyshoe.module.imagemanager.impl

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.grumpyshoe.module.imagemanager.ImageManager
import com.grumpyshoe.module.imagemanager.impl.model.ImageObject
import com.grumpyshoe.module.imagemanager.impl.model.ImagemanagerConfig
import com.grumpyshoe.module.imagemanager.impl.model.ImagemanagerConfig.Texts.TextKey.GALLERY_PERMISSION_EXPLANATION_MESSAGE
import com.grumpyshoe.module.imagemanager.impl.model.ImagemanagerConfig.Texts.TextKey.GALLERY_PERMISSION_EXPLANATION_RETRY_MESSAGE
import com.grumpyshoe.module.imagemanager.impl.model.ImagemanagerConfig.Texts.TextKey.GALLERY_PERMISSION_EXPLANATION_RETRY_TITLE
import com.grumpyshoe.module.imagemanager.impl.model.ImagemanagerConfig.Texts.TextKey.GALLERY_PERMISSION_EXPLANATION_TITLE
import com.grumpyshoe.module.intentutils.openForResult
import com.grumpyshoe.module.permissionmanager.PermissionManager
import com.grumpyshoe.module.permissionmanager.model.PermissionRequestExplanation

/*
 * ImageObject
 * android-module-imagemanager
 *
 * Created by Thomas Cirksena on 28.05.20.
 * Copyright © 2020 Thomas Cirksena. All rights reserved.
 */
class GalleryManagerImpl(
    private val permissionManager: PermissionManager
) :
    ImageManager.GalleryManager {

    private val LOG_TAG = "ImageManagerImpl"
    private lateinit var mFilePath: String


    /**
     * check permission for handling gallery images
     *
     * @param activity - activity source
     *
     */
    override fun selectImageFromGallery(activity: Activity): Int {

        permissionManager.checkPermissions(
            activity = activity,
            permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            onPermissionResult = { permissionResult ->
                triggerGallery(activity)
            },
            permissionRequestPreExecuteExplanation = PermissionRequestExplanation(
                title = ImagemanagerConfig.texts.getValue(activity, GALLERY_PERMISSION_EXPLANATION_TITLE),
                message = ImagemanagerConfig.texts.getValue(activity, GALLERY_PERMISSION_EXPLANATION_MESSAGE)
            ),
            permissionRequestRetryExplanation = PermissionRequestExplanation(
                title = ImagemanagerConfig.texts.getValue(activity, GALLERY_PERMISSION_EXPLANATION_RETRY_TITLE),
                message = ImagemanagerConfig.texts.getValue(activity, GALLERY_PERMISSION_EXPLANATION_RETRY_MESSAGE)
            ),
            requestCode = ImageManager.ImageSources.GALLERY.permissionRequestCode
        )

        return ImageManager.ImageSources.GALLERY.permissionRequestCode
    }


    /**
     * trigger gallery to choose an image
     *
     * @param activity - base activity to start other actvities
     *
     */
    override fun triggerGallery(activity: Activity) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK

        intent.openForResult(activity, ImageManager.ImageSources.GALLERY.dataRequestCode)
    }


    /**
     * handle intent result
     *
     * @param uri - Uri of image file
     * @param activity - activity source
     * @param onResult - function called on successfully image handling
     *
     * @return flag if the result has been handeld
     */
    override fun onIntentResult(uri: Uri, activity: Activity, onResult: (ImageObject) -> Unit): Boolean {
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val cursor =
                activity.applicationContext.contentResolver.query(
                    uri,
                    proj,
                    null,
                    null,
                    null
                )
            if (cursor != null) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                val s = cursor.getString(columnIndex)
                cursor.close()

                val bmOptions = BitmapFactory.Options()
                val bitmap = BitmapFactory.decodeFile(s, bmOptions)
                onResult(ImageObject(uri, bitmap))

                return true
            }
        } catch (ex: IllegalArgumentException) {
            Log.e(LOG_TAG, ex.message, ex)
        }
        return false
    }


}