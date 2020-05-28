package com.grumpyshoe.imagemanager.sample

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.grumpyshoe.imagemanager.R
import com.grumpyshoe.module.imagemanager.ImageManager
import com.grumpyshoe.module.imagemanager.impl.ImageManagerImpl
import com.grumpyshoe.module.imagemanager.impl.model.ImagemanagerConfig
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

/*
 * ImageObject
 * android-module-imagemanager
 *
 * Created by Thomas Cirksena on 28.05.20.
 * Copyright © 2020 Thomas Cirksena. All rights reserved.
 */
class MainActivity : AppCompatActivity() {

    val imageManager: ImageManager = ImageManagerImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Here is a example how to add custom texts programmatically if ou don't want to override
        // these in strings.xml.
        // You don't need to override all texts, just the one you want.
        val config = ImagemanagerConfig
        config.texts = ImagemanagerConfig.Texts(
            imagemanager_source_chooser_dialog_title = "My Custom Chooser Title",
            imagemanager_add_image_from_camera_dialog_title = "Use My Camera"
        )

        // add clicklistener to button
        button.setOnClickListener {

            // get image
            imageManager.getImage(
                activity = this,
                sources = listOf(ImageManager.ImageSources.CAMERA, ImageManager.ImageSources.GALLERY),
//                uriOnly = true, // uncomment this to retrieve only the uri of the image and no bitmap (default: false)
                onImageReceived = {
                    Log.d("Main", "Camera Image loaded")
                    imageview.setImageBitmap(it.bitmap!!)

//                    // example of how to save a bitmap as an image to disk
//                    val path = imageManager.saveImage(
//                        context = this,
//                        bitmap = it.bitmap!!,
//                        filename = "compression_jpg_80.jpg",
//                        path = "myPath",
//                        compressFormat = Bitmap.CompressFormat.JPEG,
//                        compressQuality = 80)
//
//                    // example of how to load a image from disk
//                    val f = File(path)
//                    imageManager.loadImagefromDisk(
//                        context = this,
//                        filename = f.name,
//                        path = f.parentFile.name)?.let { bitmap ->
//                        imageview.setImageBitmap(bitmap)
//                    }
//
//                    val deleted = imageManager.deleteImageFromDisk(
//                        context = this,
//                        filename = f.name,
//                        path = f.parentFile.name)
//                    Log.d("ImageManager", "Deletion of '${f.absolutePath}' successful: $deleted")
//
//                    // example of how to convert a image into base64
//                    val b64 = imageManager.imageConverter.toBase64(it.bitmap!!, Bitmap.CompressFormat.JPEG, 100)
                })
        }
    }


    /**
     * handle request permission result
     *
     * @param requestCode - the requestcode to identifiy trigger
     * @param permissions - list of permissions
     * @param grantResults - grant result
     *
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (!imageManager.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


    /**
     * handle activity result
     *
     * @param requestCode - the requestcode to identifiy trigger
     * @param resultCode - the result code
     * @param intent - the intent returned by the previous activity
     *
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!imageManager.onActivityResult(this, requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
