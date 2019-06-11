package com.grumpyshoe.module.imagemanager.impl

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

/**
 * If the Image has been taken from a Samsung device the handling needs to be a little bit different
 *
 */
object SamsungImageUtils {

    /**
     * calculate sample size
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private fun calculateInSampleSize(
        options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int
    ): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    /**
     * decode sample Bitmap from resource
     *
     * @param filePath
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    fun decodeSampledBitmapFromResource(filePath: String, reqWidth: Int, reqHeight: Int): Bitmap {

        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(filePath, options)
    }

    /**
     * images cerated by a samsung devace needs to be handled different :(
     *
     */
    fun getImageFromSamsung(filePath: String): Bitmap? {

        var scaledBmp = decodeSampledBitmapFromResource(filePath, 1024, 1024)

        try {
            val exif = ExifInterface(filePath)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)

            var rotationDegree = 0
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                rotationDegree = 90
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                rotationDegree = 180
            } else if (rotationDegree == ExifInterface.ORIENTATION_ROTATE_270) {
                rotationDegree = 270
            }

            val matrix = Matrix()
            matrix.postRotate(rotationDegree.toFloat())
            scaledBmp = Bitmap.createBitmap(scaledBmp, 0, 0, scaledBmp.width, scaledBmp.height, matrix, true)

            updateStoredPhotoFile(filePath, scaledBmp)

            return scaledBmp


        } catch (e: IOException) {
            Log.e("gggg", e.message, e)
        }

        return null
    }

    /**
     * update stored file
     *
     * @param filePath
     * @param bitmap
     * @return
     */
    private fun updateStoredPhotoFile(filePath: String, bitmap: Bitmap): File {
        val outStream: OutputStream

        var file = File(filePath)
        if (file.exists()) {
            file.delete()
            file = File(filePath)
            Log.i("file exist", "" + file)
        }
        try {
            // make a new bitmap from your file

            outStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            outStream.flush()
            outStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return file

    }
}