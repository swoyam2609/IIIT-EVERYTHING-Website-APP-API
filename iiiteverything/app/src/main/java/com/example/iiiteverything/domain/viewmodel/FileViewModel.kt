package com.example.iiiteverything.domain.viewmodel

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iiiteverything.Manifest
import com.example.iiiteverything.data.model.FileItem
import com.example.iiiteverything.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.lang.ref.WeakReference

class FileViewModel(context: Context): ViewModel() {

    private val contextRef: WeakReference<Context> = WeakReference(context)

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://your-api-url.com") // Replace with your API base URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val fileApi = retrofit.create(ApiService::class.java)

    private val _files = mutableStateListOf<FileItem>()
    val files: List<FileItem> get() = _files

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    fun searchFiles(query: String, docType: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = fileApi.getFiles(query, docType)
                _files.clear()
                _files.addAll(result)
            } catch (e: Exception) {
                Log.e("FileViewModel", "Error searching files", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun downloadFile(fileId: String): Boolean {
        val context = contextRef.get()
        if (context == null) {
            // Context is no longer available
            return false
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = fileApi.downloadFile(fileId)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        saveFileToStorage(context, responseBody)
                        return@withContext true
                    }
                }
            } catch (e: Exception) {
                Log.e("FileViewModel", "Error downloading file", e)
            }

            return@withContext false
        }
    }

    private fun saveFileToStorage(context: Context, responseBody: ResponseBody) {
        val externalDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val fileName = "downloaded_file.pdf" // Provide the desired file name and extension here

        val file = File(externalDir, fileName)
        val inputStream = responseBody.byteStream()
        val outputStream = FileOutputStream(file)

        val buffer = ByteArray(4096)
        var bytesRead: Int

        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }

        outputStream.flush()
        outputStream.close()
        inputStream.close()
    }
}