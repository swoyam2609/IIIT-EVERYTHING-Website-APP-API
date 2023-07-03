package com.example.iiittrial

import com.example.iiittrial.models.FileItem
import com.example.iiittrial.models.FileList
import okhttp3.ResponseBody
import retrofit2.Response

class FileRepo {
    suspend fun findFiles(sub: String, docType: String): Response<List<FileItem>> {
        return RetrofitInstance.api.findFiles(sub, docType)
    }

    suspend fun downloadFile(fileId: String): Response<ResponseBody> {
        return RetrofitInstance.api.downloadFile(fileId)
    }
}