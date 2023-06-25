package com.example.iiiteverything.util

sealed interface DownloadResult{
    object Success : DownloadResult
    object Failure : DownloadResult
}