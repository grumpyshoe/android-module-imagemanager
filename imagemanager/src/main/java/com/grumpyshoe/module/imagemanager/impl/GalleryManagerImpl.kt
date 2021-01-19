package com.grumpyshoe.module.imagemanager.impl

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.grumpyshoe.module.imagemanager.ImageManager
import com.grumpyshoe.module.imagemanager.impl.model.ImageObject
import com.grumpyshoe.module.imagemanager.impl.model.PermissionExplanation
import com.grumpyshoe.module.intentutils.openForResult
import com.grumpyshoe.module.permissionmanager.PermissionManager
import com.grumpyshoe.module.permissionmanager.model.PermissionRequestExplanation
import java.io.FileNotFoundException
import java.io.IOException


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
    override fun selectImageFromGallery(
        activity: Activity,
        permissionExplanation: PermissionExplanation?,
        permissionRetryExplanation: PermissionExplanation?
    ): Int {

        permissionManager.checkPermissions(
            activity = activity,
            permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            onPermissionResult = { permissionResult ->
                triggerGallery(activity)
            },
            permissionRequestPreExecuteExplanation = permissionExplanation?.let {
                return@let PermissionRequestExplanation(
                    title = activity.getString(it.title),
                    message = activity.getString(it.message)
                )
            },
            permissionRequestRetryExplanation = permissionRetryExplanation?.let {
                return@let PermissionRequestExplanation(
                    title = activity.getString(it.title),
                    message = activity.getString(it.message)
                )
            },
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
            val bitmap = getBitmap(activity, uri)
            onResult(ImageObject(uri, bitmap))
            return true
        } catch (ex: IllegalArgumentException) {
            Log.e(LOG_TAG, ex.message, ex)
        }
        return false
    }

    /**
     * get bitmap from uri
     *
     * @param activity - activity source
     * @param uri - Uri of image file
     *
     * @return bitmap or null
     */
    @Throws(FileNotFoundException::class, IOException::class)
    fun getBitmap(activity: Activity, uri: Uri?): Bitmap? {

        val uri = uri ?: return null

        activity.contentResolver.openInputStream(uri)?.let { input ->

            val onlyBoundsOptions = BitmapFactory.Options()
            onlyBoundsOptions.inJustDecodeBounds = true
            onlyBoundsOptions.inDither = true //optional
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888 //optional
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions)
            input.close()
            if (onlyBoundsOptions.outWidth == -1 || onlyBoundsOptions.outHeight == -1) {
                return null
            }
            val bitmapOptions = BitmapFactory.Options()
            bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888 //
            activity.contentResolver.openInputStream(uri)?.let { input2 ->
                val bitmap = BitmapFactory.decodeStream(input2, null, bitmapOptions)
                input2.close()
                return bitmap
            }
        }
        return null
    }
}