package com.grumpyshoe.module.imagemanager.impl.model

import android.content.Context
import com.grumpyshoe.getimage.R

object ImagemanagerConfig {

    var texts: Texts = Texts()

    data class Texts(
        private var imagemanager_source_chooser_dialog_title: String? = null,
        private var imagemanager_add_image_from_camera_dialog_title: String? = null,
        private var imagemanager_add_image_from_gallery_dialog_title: String? = null,
        private var imagemanager_camera_permission_explanation_title: String? = null,
        private var imagemanager_camera_permission_explanation_message: String? = null,
        private var imagemanager_camera_permission_explanation_retry_title: String? = null,
        private var imagemanager_camera_permission_explanation_retry_message: String? = null,
        private var imagemanager_gallery_permission_explanation_title: String? = null,
        private var imagemanager_gallery_permission_explanation_message: String? = null,
        private var imagemanager_gallery_permission_explanation_retry_title: String? = null,
        private var imagemanager_gallery_permission_explanation_retry_message: String? = null
    ) {

        enum class TextKey(val stringResourceId: Int) {
            SOURCE_CHOOSER_DIALOG_TITLE(R.string.imagemanager_source_chooser_dialog_title),
            ADD_IMAGE_FROM_CAMERA_DIALOG_TITLE(R.string.imagemanager_add_image_from_camera_dialog_title),
            ADD_IMAGE_FROM_GALLERY_DIALOG_TITLE(R.string.imagemanager_add_image_from_gallery_dialog_title),
            CAMERA_PERMISSION_EXPLANATION_TITLE(R.string.imagemanager_camera_permission_explanation_title),
            CAMERA_PERMISSION_EXPLANATION_MESSAGE(R.string.imagemanager_camera_permission_explanation_message),
            CAMERA_PERMISSION_EXPLANATION_RETRY_TITLE(R.string.imagemanager_camera_permission_explanation_retry_title),
            CAMERA_PERMISSION_EXPLANATION_RETRY_MESSAGE(R.string.imagemanager_camera_permission_explanation_retry_message),
            GALLERY_PERMISSION_EXPLANATION_TITLE(R.string.imagemanager_gallery_permission_explanation_title),
            GALLERY_PERMISSION_EXPLANATION_MESSAGE(R.string.imagemanager_gallery_permission_explanation_message),
            GALLERY_PERMISSION_EXPLANATION_RETRY_TITLE(R.string.imagemanager_gallery_permission_explanation_retry_title),
            GALLERY_PERMISSION_EXPLANATION_RETRY_MESSAGE(R.string.imagemanager_gallery_permission_explanation_retry_message);
        }

        fun getValue(context: Context, key: TextKey): String {
            return when (key) {
                TextKey.SOURCE_CHOOSER_DIALOG_TITLE -> imagemanager_source_chooser_dialog_title
                    ?: context.getString(key.stringResourceId)
                TextKey.ADD_IMAGE_FROM_CAMERA_DIALOG_TITLE -> imagemanager_add_image_from_camera_dialog_title
                    ?: context.getString(key.stringResourceId)
                TextKey.ADD_IMAGE_FROM_GALLERY_DIALOG_TITLE -> imagemanager_add_image_from_gallery_dialog_title
                    ?: context.getString(key.stringResourceId)
                TextKey.CAMERA_PERMISSION_EXPLANATION_TITLE -> imagemanager_camera_permission_explanation_title
                    ?: context.getString(key.stringResourceId)
                TextKey.CAMERA_PERMISSION_EXPLANATION_MESSAGE -> imagemanager_camera_permission_explanation_message
                    ?: context.getString(key.stringResourceId)
                TextKey.CAMERA_PERMISSION_EXPLANATION_RETRY_TITLE -> imagemanager_camera_permission_explanation_retry_title
                    ?: context.getString(key.stringResourceId)
                TextKey.CAMERA_PERMISSION_EXPLANATION_RETRY_MESSAGE -> imagemanager_camera_permission_explanation_retry_message
                    ?: context.getString(key.stringResourceId)
                TextKey.GALLERY_PERMISSION_EXPLANATION_TITLE -> imagemanager_gallery_permission_explanation_title
                    ?: context.getString(key.stringResourceId)
                TextKey.GALLERY_PERMISSION_EXPLANATION_MESSAGE -> imagemanager_gallery_permission_explanation_message
                    ?: context.getString(key.stringResourceId)
                TextKey.GALLERY_PERMISSION_EXPLANATION_RETRY_TITLE -> imagemanager_gallery_permission_explanation_retry_title
                    ?: context.getString(key.stringResourceId)
                TextKey.GALLERY_PERMISSION_EXPLANATION_RETRY_MESSAGE -> imagemanager_gallery_permission_explanation_retry_message
                    ?: context.getString(key.stringResourceId)
            }
        }
    }
}
