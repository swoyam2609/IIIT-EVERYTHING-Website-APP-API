package com.example.iiittrial.ui

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.VectorDrawable
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.net.toUri
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.iiittrial.FileRepo
import com.example.iiittrial.MainActivity
import com.example.iiittrial.MainViewModel
import com.example.iiittrial.R
import com.example.iiittrial.models.FileItem
import com.itextpdf.text.Document
import com.itextpdf.text.pdf.PdfCopy
import com.itextpdf.text.pdf.PdfReader
import dagger.hilt.android.internal.Contexts.getApplication
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okio.IOException
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainFile(application: MainActivity){
    val lifecycleOwner = LocalLifecycleOwner.current

    val viewModel = viewModel<MainViewModel>(factory = object : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(FileRepo()) as T
        }
    })

    val appContext: Context = application.applicationContext

    var query by remember { mutableStateOf("") } // query for searching with Subject
    var docType by remember { mutableStateOf("") }// query for searching with Document type Ex: Notes, Question Paper, etc
    val searching = remember { mutableStateOf(false) }// to check if searching is going on or not

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp)) {
        Column(modifier = Modifier
            .fillMaxHeight()
            .padding(5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                // Search bar for searching with Subject
                TextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Search Subject") },
                    modifier = Modifier
                        .weight(1f)
                        .clip(shape = RoundedCornerShape(10.dp))
                )

                Spacer(modifier = Modifier.width(5.dp))

                // Search bar for searching with Document type Ex: Notes, Question Paper, etc
                TextField(
                    value = docType,
                    onValueChange = { docType = it },
                    label = { Text("Document type") },
                    modifier = Modifier
                        .weight(1f)
                        .clip(shape = RoundedCornerShape(10.dp))
                )
            }

            Spacer(modifier =Modifier.height(10.dp))

            // Search button
            Button(
                onClick = {
                    viewModel.findFiles(query, docType)
                    viewModel.findFilesValue.observe(lifecycleOwner, Observer { response ->
                        if (response.isSuccessful){
                            searching.value = true
                            response.body()?.let {
                                viewModel.files = it                                    // set the files list to the response body
                                for (i in viewModel.files){
                                    Log.d("MainFile", "MainFile: ${i.filename}")
                                    Log.d("MainFile", "MainFile: ${i.id}")
                                    Log.d("MainFile", "MainFile: ${i.documentType}")
                                    Log.d("MainFile", "MainFile: ${i.sub}")
                                }

                            }
                        }else{
                            searching.value = false
                            Log.d("MainFile", "MainFile: ${response.errorBody()}")
                        }
                    })
                    //clear the query and docType
                    query = ""
                    docType = ""
                }
            ) {
                Text("Search")
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Show the List of files
            FileListView(files = viewModel.files, appContext, viewModel, lifecycleOwner)

            /*{ fileId ->
                // Handle file selection here

                downloadFileSave(fileId, appContext)

                Log.d("download clicked", "MainFile: $fileId")
                /*viewModel.downloadFile(fileId)
                viewModel.downloadfileValue.observe(lifecycleOwner, Observer { response ->
                    if (response.isSuccessful){
                        response.body()?.let {
                            saveFile(it, appContext)
                        }
                    }

                })*/
            }*/
        }
    }

}

@Composable
fun FileListView(
    files: List<FileItem>,
    appContext: Context,
    viewModel: MainViewModel,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner
){

    LazyColumn(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(files) { file ->

            // Card for each file
            Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(8.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){

                    // Column for file details
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)) {
                        EllipsisText(text = "Name:${file.filename}", maxLength = 35)

                        EllipsisText(text = "Subject:${file.sub}" , maxLength = 25)

                        EllipsisText(text = "Type:${file.documentType}", maxLength = 25)
                    }
                    /*Button(onClick = {
                        viewModel.downloadFile(file.id)
                        downloadFileSave(file.id, appContext)
                        /*viewModel.downloadfileValue.observe(lifecycleOwner, Observer { response ->
                            if (response.isSuccessful) {
                                response.body()?.let {
                                    downloadFileSave(it, appContext)
                                }
                            }
                        })*/
                    },modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f))
                    {
                        Text(text ="Download")
                    }*/

                    // Download Icon
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_download_24),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .clickable {
                                viewModel.downloadFile(file.id)
                                viewModel.downloadfileValue.observe(lifecycleOwner, Observer { response ->
                                        if (response.isSuccessful) {
                                            response.body()?.let {
                                                saveResponseBodyAsPdf(it,appContext, file.filename)

                                            }
                                        }

                                })
                            }
                    )
                }

            }
        }
    }
}
@Composable
fun EllipsisText(text: String, maxLength: Int) {
    val trimmedText = remember {
        if (text.length > maxLength) {
            text.substring(0, maxLength - 3) + "..."
        } else {
            text
        }
    }

    Text(
        text = trimmedText,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}


/*fun downloadFileSave(file_id:ResponseBody, context: Context){

    try {
        val downloadUrl = "https://dbiiit.swoyam.engineer/download/$file_id"
        val downloadManager: DownloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(downloadUrl)
        val request = DownloadManager.Request(uri)
        request.setNotificationVisibility(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            .setMimeType("application/pdf")
            .setTitle("File")
            .setDescription("Downloading")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "file.pdf")
        downloadManager.enqueue(request)
    }catch (e:Exception){
        Log.e("FileViewModel", "Error downloading file", e)
    }
}*/

/*fun downloadFileSave(fileId: String, context: Context) {
    val downloadUrl = "https://dbiiit.swoyam.engineer/download/$fileId"

    val okHttpClient = OkHttpClient()
    val request = Request.Builder()
        .url(downloadUrl)
        .build()

    okHttpClient.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("FileViewModel", "Error downloading file", e)
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val fileName = "file.pdf" // Replace with the actual file name and extension
                val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)

                response.body?.let { responseBody ->
                    FileOutputStream(file).use { outputStream ->
                        responseBody.byteStream().use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }

                    // Convert the binary file to PDF
                    val outputFile = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "output.pdf") // Replace with the desired output file name
                    convertToPDF(file, outputFile)

                    // Notify the download completion
                    val intent = Intent(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                    intent.putExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
                    context.sendBroadcast(intent)
                }
            } else {
                Log.e("FileViewModel", "Error downloading file. Response code: ${response.code}")
            }
        }
    })
}*/

/*private fun saveFile(responseBody: ResponseBody, context: Context) {
    val fileName = "file.pdf" // Replace with the actual file name and extension

    val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)
    val outputStream: OutputStream = FileOutputStream(file)

    responseBody.byteStream().use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }

    val uri: Uri = FileProvider.getUriForFile(
        context,
        context.packageName + ".fileprovider",
        file
    )

    val downloadManager: DownloadManager =
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    val request = DownloadManager.Request(uri)
        .setTitle("File")
        .setDescription("Downloading")
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

    downloadManager.enqueue(request)
}*/

/*fun convertToPDF(inputFile: File, outputFile: File) {
    val document = Document()

    try {
        val outputStream = FileOutputStream(outputFile)
        val writer = PdfCopy(document, outputStream)
        document.open()

        val reader = PdfReader(inputFile.absolutePath)
        val pageCount = reader.numberOfPages

        for (pageIndex in 0 until pageCount) {
            val importedPage = writer.getImportedPage(reader, pageIndex + 1)
            writer.addPage(importedPage)
        }

        writer.close()
        reader.close()
        document.close()
        outputStream.close()
    } catch (e: IOException) {
        Log.e("FileViewModel", "Error converting file to PDF", e)
        e.printStackTrace()
    }
}*/

//Download file and save it to Downloads folder
fun saveResponseBodyAsPdf(responseBody: ResponseBody, context: Context, fileName: String) {
    try {
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val outputFile = File(storageDir, fileName)

        val inputStream = responseBody.byteStream()
        val outputStream = FileOutputStream(outputFile)

        val buffer = ByteArray(4096)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }

        outputStream.flush()
        outputStream.close()
        inputStream.close()

        showToast(context, "File downloaded successfully")
    } catch (e: IOException) {
        e.printStackTrace()
        showToast(context, "Error downloading file")
    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}


