package com.grumpyshoe.module.imagemanager.impl

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AlertDialog
import com.grumpyshoe.module.imagemanager.ImageManager
import com.grumpyshoe.module.imagemanager.impl.model.ImagemanagerConfig
import com.grumpyshoe.module.imagemanager.impl.model.ImagemanagerConfig.Texts.TextKey.*
import com.grumpyshoe.module.permissionmanager.PermissionManager
import com.grumpyshoe.module.permissionmanager.impl.PermissionManagerImpl
import java.io.*


class ImageManagerImpl : ImageManager {


    val permissionManager: PermissionManager = PermissionManagerImpl
    override var cameraManager: ImageManager.CameraManager =
        CameraManagerImpl(permissionManager)
    override var galleryManager: ImageManager.GalleryManager =
        GalleryManagerImpl(permissionManager)
    override var imageConverter: ImageManager.ImageConverter =
        ImageConverterImpl()


    private lateinit var mCurrectActivity: Activity
    private lateinit var onImageReceived: (Bitmap) -> Unit
    private var requestCodeTrigger: Int = 0

    override fun getImage(
        activity: Activity,
        sources: List<ImageManager.ImageSources>,
        onImageReceived: (Bitmap) -> Unit
    ) {

        if (sources.isEmpty()) {
            throw IllegalArgumentException("At least one source needs to be defined")
        }

        this.onImageReceived = onImageReceived
        mCurrectActivity = activity

        if (sources.size == 1) {

            // choose object from source directly
            requestCodeTrigger = if (sources[0].equals(ImageManager.ImageSources.CAMERA)) {
                cameraManager.selectImageFromCamera(activity)
            } else {
                galleryManager.selectImageFromGallery(activity)
            }
        } else {

            // start dialog to select source
            showSourceChooserDialog(
                dialogTitle = ImagemanagerConfig.texts.getValue(activity, SOURCE_CHOOSER_DIALOG_TITLE),
                takePhotoTitle = ImagemanagerConfig.texts.getValue(activity, ADD_IMAGE_FROM_CAMERA_DIALOG_TITLE),
                getImageFromGallerytitle = ImagemanagerConfig.texts.getValue(activity, ADD_IMAGE_FROM_GALLERY_DIALOG_TITLE)
            )
        }

    }


    /**
     * Builds Dialog for user to choose where to load image from.
     * Possibilities are gallery or photo.
     *
     * Triggers imagesource according to users choice
     *
     * @param activity
     * @param dialogTitle
     * @param takePhotoTitle
     * @param getImageFromGallerytitle
     * @param callback
     */
    fun showSourceChooserDialog(
        dialogTitle: String,
        takePhotoTitle: String,
        getImageFromGallerytitle: String
    ) {

        val items = arrayOf<CharSequence>(takePhotoTitle, getImageFromGallerytitle)

        val builder = AlertDialog.Builder(mCurrectActivity)
        builder.setTitle(dialogTitle)
        builder.setItems(items) { dialog, index ->
            requestCodeTrigger = if (items[index] == takePhotoTitle) {
                cameraManager.selectImageFromCamera(mCurrectActivity)
            } else {
                galleryManager.selectImageFromGallery(mCurrectActivity)
            }
        }
        builder.setNegativeButton("Abbrechen", null)
        Handler(Looper.getMainLooper()).post { builder.create().show() }
    }


    /**
     * handle request permission result for creating/getting a picture
     *
     * @param requestCode - the requestcode to identifiy trigger
     * @param permissions - list of permissions
     * @param grantResults - grant result
     *
     * @return flag if the result has been handeld
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ): Boolean {

        return if (requestCode == ImageManager.ImageSources.CAMERA.permissionRequestCode && requestCodeTrigger == ImageManager.ImageSources.CAMERA.permissionRequestCode) {

            // check if permission has been requested for camera and the last requested permission is also camera

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraManager.triggerCamera(mCurrectActivity)
            }
            true

        } else if (requestCode == ImageManager.ImageSources.GALLERY.permissionRequestCode && requestCodeTrigger == ImageManager.ImageSources.GALLERY.permissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                galleryManager.triggerGallery(mCurrectActivity)
            }
            true
        } else {
            false
        }
    }


    /**
     * Gets image mimeType on given path
     *
     * @param imagePath
     */
    override fun getMimeType(imagePath: String): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(imagePath)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }


    /**
     * handle activity result for creating/getting a picture
     *
     * @param context - the context where the result has been catched
     * @param requestCode - the requestcode to identifiy trigger
     * @param resultCode - the result code
     * @param intent - the intent returned by the previous activity
     *
     * @return flag if the result has been handeld
     */
    override fun onActivityResult(
        activity: Activity,
        requestCode: Int,
        resultCode: Int,
        intent: Intent?
    ): Boolean {

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == ImageManager.ImageSources.CAMERA.dataRequestCode && intent != null) {

                return cameraManager.onIntentResult(activity, false) {
                    onImageReceived(it)
                }

            }
            if (requestCode == ImageManager.ImageSources.CAMERA.dataRequestCode + 1) {

                return cameraManager.onIntentResult(activity, true) {
                    onImageReceived(it)
                }

            } else if (requestCode == ImageManager.ImageSources.GALLERY.dataRequestCode && intent != null && intent.data != null) {
                return galleryManager.onIntentResult(intent.data, mCurrectActivity) {
                    onImageReceived(it)
                }
            }
        }

        return false
    }

    /**
     * write image to storage and return path
     *
     */
    override fun saveImage(
        context: Context,
        bitmap: Bitmap,
        filename: String,
        path: String,
        compressFormat: Bitmap.CompressFormat,
        compressQuality: Int
    ): String? {

        var outputStream: FileOutputStream? = null
        val stream = ByteArrayOutputStream()
        try {

            bitmap.compress(compressFormat, compressQuality, stream)

            val folder = context.filesDir.absolutePath + File.separator + path

            val subFolder = File(folder)

            if (!subFolder.exists()) {
                subFolder.mkdirs()
            }

            val finalFile = File(subFolder, filename)
            outputStream = FileOutputStream(finalFile)
            outputStream.write(stream.toByteArray())

            return finalFile.absolutePath

        } catch (e: FileNotFoundException) {
            Log.e(javaClass.simpleName, e.toString())
        } catch (e: IOException) {
            Log.e(javaClass.simpleName, e.toString())
        } finally {
            outputStream?.close()
        }

        return null
    }

    /**
     * load image from storage and return bitmap
     *
     */
    override fun loadImagefromDisk(context:Context, filename: String, path: String): Bitmap? {

        val filePath = File(context.filesDir.absolutePath + File.separator + path, filename).absolutePath
        try {
           return BitmapFactory.decodeFile(filePath)
        }
        catch (e:Exception){
            Log.e("ImageManager", e.message, e)
        }
        return null
    }

    /**
     * delete file from
     *
     */
    override fun deleteImageFromDisk(context:Context, filename: String, path: String): Boolean {

       return  File(context.filesDir.absolutePath + File.separator + path, filename).delete()
    }
}