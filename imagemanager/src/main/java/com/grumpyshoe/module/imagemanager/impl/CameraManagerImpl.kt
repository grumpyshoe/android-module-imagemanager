package com.grumpyshoe.module.imagemanager.impl

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import androidx.core.content.FileProvider
import com.grumpyshoe.module.imagemanager.ImageManager
import com.grumpyshoe.module.imagemanager.ImageManagerFileProvider
import com.grumpyshoe.module.imagemanager.impl.model.ImageObject
import com.grumpyshoe.module.imagemanager.impl.model.PermissionExplanation
import com.grumpyshoe.module.intentutils.openForResult
import com.grumpyshoe.module.permissionmanager.PermissionManager
import com.grumpyshoe.module.permissionmanager.model.PermissionRequestExplanation
import java.io.File
import java.io.IOException


/*
 * ImageObject
 * android-module-imagemanager
 *
 * Created by Thomas Cirksena on 28.05.20.
 * Copyright © 2020 Thomas Cirksena. All rights reserved.
 */
class CameraManagerImpl(private val permissionManager: PermissionManager) :
    ImageManager.CameraManager {

    private var cameraImageUri: Uri? = null
    private lateinit var filePath: String


    /**
     * check permission for handling camera
     *
     * @param activity - activity source
     *
     */
    override fun selectImageFromCamera(
        activity: Activity,
        permissionExplanation: PermissionExplanation?,
        permissionRetryExplanation: PermissionExplanation?
    ): Int {

        permissionManager.checkPermissions(
            activity = activity,
            permissions = arrayOf(Manifest.permission.CAMERA),
            onPermissionResult = { permissionResult ->
                triggerCamera(activity)
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
            requestCode = ImageManager.ImageSources.CAMERA.permissionRequestCode
        )

        return ImageManager.ImageSources.CAMERA.permissionRequestCode
    }


    /**
     * trigger camera to create image
     *
     * @param activity - base activity to start other actvities
     */
    @Throws(IOException::class)
    override fun triggerCamera(activity: Activity) {

        val context = activity.applicationContext

        val previousFile = File(activity.externalCacheDir.toString() + CAMERA_IMAGE_NAME + ".png")
        if (previousFile.exists()) {
            previousFile.delete()
        }

        val photoFile = File.createTempFile(CAMERA_IMAGE_NAME, ".png", activity.externalCacheDir)
        filePath = photoFile.absolutePath
        cameraImageUri = ImageManagerFileProvider.getUri(activity, activity.packageName + ".imagemanager_fileprovider", photoFile)


        val intent = Intent(ACTION_IMAGE_CAPTURE)

        val requestCode = ImageManager.ImageSources.CAMERA.dataRequestCode

        val resolvedIntentActivities = context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)

        for (resolvedIntentInfo in resolvedIntentActivities) {
            val packageName = resolvedIntentInfo.activityInfo.packageName

            context.grantUriPermission(
                packageName,
                cameraImageUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri)
        intent.openForResult(activity, requestCode)
    }


    /**
     * handle intent result
     *
     * @param context - Context source
     * @param onResult - function called on successfully image handling
     *
     * @return flag if the result has been handeld
     */
    override fun onIntentResult(
        activity: Activity,
        uriOnly: Boolean,
        onResult: (ImageObject) -> Unit
    ): Boolean {

        try {
            // if only the URI is requested return it
            if (uriOnly) {
                onResult(ImageObject(cameraImageUri))
            } else {

                val bitmap = MediaStore.Images.Media.getBitmap(
                    activity.contentResolver,
                    cameraImageUri
                )

                // check reotiation
                val input = activity.contentResolver.openInputStream(cameraImageUri);
                val ei = if (Build.VERSION.SDK_INT > 23)
                    ExifInterface(input);
                else
                    ExifInterface(cameraImageUri?.path);

                val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                val bmp = when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap!!, 90f);
                    ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap!!, 180f);
                    ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap!!, 270f);
                    else -> bitmap
                }

                onResult(
                    ImageObject(
                        cameraImageUri,
                        bmp
                    )
                )
            }

            return true
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return false
    }

    private fun rotateImage(img: Bitmap, degree: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree)
        val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }

    companion object {
        private const val CAMERA_IMAGE_NAME = "photo"
    }
}