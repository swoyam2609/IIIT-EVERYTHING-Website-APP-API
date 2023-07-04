package com.example.iiittrial.ui

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.iiittrial.domain.repo.FileRepo
import com.example.iiittrial.MainActivity
import com.example.iiittrial.presentation.MainViewModel
import com.example.iiittrial.R
import com.example.iiittrial.data.models.DownloadedFile
import com.example.iiittrial.data.models.FileItem
import okhttp3.ResponseBody
import okio.IOException
import java.io.File
import java.io.FileOutputStream

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
    val downloadedFile = viewModel.downloadedFile // Store the downloaded file in the ViewModel

    var selectedSubject by remember { mutableStateOf("") }
    val subjects = listOf("Subject 1", "Subject 2", "Subject 3", /* Add more subjects */)

    var selectedDocType by remember { mutableStateOf("") }
    val docTypes = listOf("Notes", "Paper", "Book")

    // Register observer for downloadedFile in a LaunchedEffect block
    LaunchedEffect(downloadedFile) {
        downloadedFile?.let { file ->
            saveResponseBodyAsPdf(
                responseBody = file.responseBody,
                context = appContext,
                fileName = file.fileName, viewModel = viewModel)
            viewModel.clearDownloadedFile() // Clear the downloaded file in ViewModel
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp)) {
        Column(modifier = Modifier
            .fillMaxHeight()
            .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

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
                    //clear the query and docType
                    query = ""
                    docType = ""
                }
            ) {
                Text("Search")
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Observe findFilesValue in LaunchedEffect
            LaunchedEffect(viewModel.findFilesValue) {
                viewModel.findFilesValue.observe(lifecycleOwner, Observer { response ->
                    if (response.isSuccessful) {
                        searching.value = true
                        response.body()?.let {
                            viewModel.files = it
                        }
                    } else {
                        searching.value = false
                        Log.d("MainFile", "MainFile: ${response.errorBody()}")
                    }
                })
            }

            // Show the List of files
            FileListView(files = viewModel.files, appContext, viewModel, lifecycleOwner)

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
                                                saveResponseBodyAsPdf(
                                                    responseBody = it,
                                                    context = appContext,
                                                    fileName = file.filename,
                                                    viewModel = viewModel
                                                )

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

//Download file and save it to Downloads folder
fun saveResponseBodyAsPdf(responseBody: ResponseBody, context: Context, fileName: String,viewModel: MainViewModel) {
    try {
        viewModel.clearDownloadedFile() // Clear the previous downloaded file, if any


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

        viewModel.setDownloadedFile(responseBody, fileName)

        showToast(context, "File downloaded successfully")
    } catch (e: IOException) {
        e.printStackTrace()
        showToast(context, "Error downloading file")
    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}


