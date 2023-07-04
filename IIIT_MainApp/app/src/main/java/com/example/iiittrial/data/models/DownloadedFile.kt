package com.example.iiittrial.data.models

import okhttp3.ResponseBody

data class DownloadedFile (
    val responseBody: ResponseBody,
    val fileName: String
)
