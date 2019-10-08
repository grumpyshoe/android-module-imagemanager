package com.grumpyshoe.imagemanager.sample

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.grumpyshoe.imagemanager.R
import com.grumpyshoe.module.imagemanager.ImageManager
import com.grumpyshoe.module.imagemanager.impl.model.ImagemanagerConfig
import com.grumpyshoe.module.imagemanager.impl.ImageManagerImpl
import kotlinx.android.synthetic.main.activity_main.*


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
                onImageReceived = {
                    Log.d("Main", "Camera Image loaded")
                    imageview.setImageBitmap(it)

//                    // example on how to save bitmap as image to disk
//                    imageManager.saveImage(
//                        context = this,
//                        bitmap = it,
//                        filename = "compression_jpg_80.jpg",
//                        path = this.cacheDir.absolutePath,
//                        compressFormat = Bitmap.CompressFormat.JPEG,
//                        compressQuality = 80)

                    // example of how to convert image into base64
                    //val b64 = imageManager.imageConverter.toBase64(it, Bitmap.CompressFormat.JPEG, 100)
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
