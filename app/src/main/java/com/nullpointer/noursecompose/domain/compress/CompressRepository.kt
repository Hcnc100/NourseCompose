package com.nullpointer.noursecompose.domain.compress

import android.net.Uri

interface CompressRepository {
    suspend fun compressImage(uriImg: Uri): Uri
}