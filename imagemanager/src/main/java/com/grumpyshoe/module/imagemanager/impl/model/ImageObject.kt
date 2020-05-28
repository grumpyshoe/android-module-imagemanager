package com.grumpyshoe.module.imagemanager.impl.model

import android.graphics.Bitmap
import android.net.Uri

/*
 * ImageObject
 * android-module-imagemanager
 *
 * Created by Thomas Cirksena on 28.05.20.
 * Copyright © 2020 Thomas Cirksena. All rights reserved.
 */
data class ImageObject(val uri: Uri?, val bitmap: Bitmap?=null)