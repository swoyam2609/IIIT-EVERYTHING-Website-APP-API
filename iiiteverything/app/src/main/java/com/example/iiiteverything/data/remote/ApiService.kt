package com.example.iiiteverything.data.remote

import com.example.iiiteverything.data.model.FileItem
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/find")
    suspend fun getFiles(
        @Query("sub_string") query: String,
        @Query("docType") docType: String
    ): List<FileItem>

    @GET("/download/{file_id}")
    suspend fun downloadFile(@Path("file_id") fileId: String): Response<ResponseBody>
}