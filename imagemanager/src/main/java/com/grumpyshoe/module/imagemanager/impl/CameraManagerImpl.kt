package com.grumpyshoe.module.imagemanager.impl

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import androidx.core.content.FileProvider
import com.grumpyshoe.module.imagemanager.ImageManager
import com.grumpyshoe.module.imagemanager.impl.model.ImagemanagerConfig
import com.grumpyshoe.module.imagemanager.impl.model.ImagemanagerConfig.Texts.TextKey.*
import com.grumpyshoe.module.intentutils.openForResult
import com.grumpyshoe.module.permissionmanager.PermissionManager
import com.grumpyshoe.module.permissionmanager.model.PermissionRequestExplanation
import java.io.File
import java.io.IOException


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
    override fun selectImageFromCamera(activity: Activity): Int {

        permissionManager.checkPermissions(
            activity = activity,
            permissions = arrayOf(Manifest.permission.CAMERA),
            onPermissionResult = { permissionResult ->
                triggerCamera(activity)
            },
            permissionRequestPreExecuteExplanation = PermissionRequestExplanation(
                title = ImagemanagerConfig.texts.getValue(activity, CAMERA_PERMISSION_EXPLANATION_TITLE),
                message = ImagemanagerConfig.texts.getValue(activity, CAMERA_PERMISSION_EXPLANATION_MESSAGE)
            ),
            permissionRequestRetryExplanation = PermissionRequestExplanation(
                title = ImagemanagerConfig.texts.getValue(activity, CAMERA_PERMISSION_EXPLANATION_RETRY_TITLE),
                message = ImagemanagerConfig.texts.getValue(activity,CAMERA_PERMISSION_EXPLANATION_RETRY_MESSAGE)
            ),
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

        val previousFile = File(activity.externalCacheDir.toString() + CAMERA_IMAGE_NAME + ".jpg")
        if (previousFile.exists()) {
            previousFile.delete()
        }

        val photoFile = File.createTempFile(CAMERA_IMAGE_NAME, ".jpg", activity.externalCacheDir)
        filePath = photoFile.absolutePath
        cameraImageUri =
            FileProvider.getUriForFile(activity, activity.packageName + ".fileprovider", photoFile)

        val manufacturer = android.os.Build.MANUFACTURER

        val intent = Intent(ACTION_IMAGE_CAPTURE)
        val requestCode: Int

        requestCode = if (manufacturer.equals("samsung", ignoreCase = true) || manufacturer.equals(
                "huawei",
                ignoreCase = true
            )
        ) {
            ImageManager.ImageSources.CAMERA.dataRequestCode + 1
        } else {
            ImageManager.ImageSources.CAMERA.dataRequestCode
        }

        val resolvedIntentActivities =
            context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
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
        isSamsung: Boolean,
        onResult: (Bitmap) -> Unit
    ): Boolean {

        try {

            if (isSamsung) {
                SamsungImageUtils.getImageFromSamsung(filePath)?.let(onResult)
            } else {
                onResult(
                    MediaStore.Images.Media.getBitmap(
                        activity.contentResolver,
                        cameraImageUri
                    )
                )
            }

            return true
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return false
    }


    companion object {
        private const val CAMERA_IMAGE_NAME = "photo"
    }
}