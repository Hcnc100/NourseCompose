package com.nullpointer.noursecompose.core.utils

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import timber.log.Timber
import java.io.*

class ImageUtils {
    companion object {
        fun saveToInternalStorage(
            bitmapImage: Bitmap,
            nameFile: String,
            context: Context,
        ): String? {
            return try {
                context.openFileOutput(nameFile, Context.MODE_PRIVATE).use { output ->
                    bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, output)
                }
                nameFile
            } catch (e: Exception) {
                Timber.e("Error saved image $nameFile : $e")
                null
            }
        }

        fun loadImageFromStorage(nameFile: String, context: Context): Bitmap? {
            return try {
                context.openFileInput(nameFile).use { input ->
                    BitmapFactory.decodeStream(input)
                }
            } catch (e: Exception) {
                Timber.e("Error load img $nameFile : $e")
                null
            }
        }

        fun deleterImgFromStorage(nameFile:String,context: Context){
            try {
                context.deleteFile(nameFile)
            }catch (e:Exception){
                Timber.d("Error deleter img $nameFile : $e")
            }
        }

    }
}