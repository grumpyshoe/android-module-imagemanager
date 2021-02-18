package com.grumpyshoe.module.imagemanager

import android.content.Context
import androidx.core.content.FileProvider
import java.io.File


/*
 * ImageManagerFileProvider
 * android-module-imagemanager
 *
 * Created by Thomas Cirksena on 18.02.21.
 * Copyright © 2021 Hansefit GmbH & Co. KG. All rights reserved.
 */
class ImageManagerFileProvider:FileProvider(){

    companion object{
        fun getUri(context: Context, authority:String, file: File) = getUriForFile(context, authority,file)
    }
}