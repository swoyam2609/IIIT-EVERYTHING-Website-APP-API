package com.example.iiittrial.data.models


import com.google.gson.annotations.SerializedName

data class FileItem(
    @SerializedName("content_type")
    val contentType: String,
    @SerializedName("documentType")
    val documentType: String,
    @SerializedName("filename")
    val filename: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("path")
    val path: String,
    @SerializedName("size")
    val size: Int,
    @SerializedName("sub")
    val sub: String,
    @SerializedName("upload_date")
    val uploadDate: String,
    @SerializedName("uploader")
    val uploader: Int
)