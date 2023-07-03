package com.example.iiittrial

import com.example.iiittrial.models.FileItem
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/find")
    suspend fun findFiles(
        @Query("sub") sub: String,
        @Query("docType") docType: String
    ): Response<List<FileItem>>

    @GET("/download/{fileId}")
    suspend fun downloadFile(
        @Path("fileId") fileId: String
    ): Response<ResponseBody>

}
