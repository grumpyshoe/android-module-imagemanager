package com.grumpyshoe.module.imagemanager.impl.model

import com.grumpyshoe.getimage.R

data class SourceChooserDialogValues(
    val dialogTitle: Int = R.string.imagemanager_source_chooser_dialog_title,
    val takePhotoTitle: Int = R.string.imagemanager_add_image_from_camera_dialog_title,
    val getImageFromGallerytitle: Int = R.string.imagemanager_add_image_from_gallery_dialog_title
)
