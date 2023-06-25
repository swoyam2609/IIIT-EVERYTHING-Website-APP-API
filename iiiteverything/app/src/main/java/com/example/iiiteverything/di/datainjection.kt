package com.example.iiiteverything.di

import android.util.Log
import com.example.iiiteverything.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream

/*suspend fun downloadPdfFile(fileId: String, destinationFile: File) {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.example.com")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)

    try {
        val response = withContext(Dispatchers.IO) {
            apiService.downloadFile(fileId)
        }

        response.body()?.let { responseBody ->
            writeResponseBodyToFile(responseBody, destinationFile)
        }
    } catch (e: Exception) {
        // Handle exceptions
        Log.e("Download", "Exception: ${e.message}")
    }
}

private fun writeResponseBodyToFile(body: ResponseBody, file: File) {
    FileOutputStream(file).use { outputStream ->
        val buffer = ByteArray(4096)
        var bytesRead: Int
        while (body.byteStream().read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }
        outputStream.flush()
    }
}*/