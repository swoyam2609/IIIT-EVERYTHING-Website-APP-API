package com.example.iiittrial.ui

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.iiittrial.MainActivity
import com.example.iiittrial.R
import com.example.iiittrial.data.models.FileItem
import com.example.iiittrial.domain.repo.FileRepo
import com.example.iiittrial.presentation.MainViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import okio.IOException
import java.io.File
import java.io.FileOutputStream


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(application: MainActivity) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val viewModel = viewModel<MainViewModel>(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(FileRepo()) as T
        }
    })

    val appContext: Context = application.applicationContext

    val scope = rememberCoroutineScope()

    val searching = remember { mutableStateOf(false) }// to check if searching is going on or not

    var expanded_sub by remember { mutableStateOf(false) }
    var expanded_doc by remember { mutableStateOf(false) }

    var selectedSubject by remember { mutableStateOf("") }
    val subjectList = listOf(
        "BET",
        "CD",
        "DSA Lab",
        "Data Structures",
        "Engineering Chemistry",
        "GRAPH THEORY",
        "Maths 1",
        "Maths-2",
        "OS",
        "PHYSICS",
        "ROBOTICS",
        "WSM",
        "Workshop"
    )

    var selectedDocType by remember { mutableStateOf("") }
    val docTypeList = listOf("NOTES", "PAPER", "BOOK")

    val downloadedFile = viewModel.downloadedFile // Store the downloaded file in the ViewModel


    // Register observer for downloadedFile in a LaunchedEffect block
    LaunchedEffect(downloadedFile) {
        downloadedFile?.let { file ->
            saveResponseBodyAsPdf(
                responseBody = file.responseBody,
                context = appContext,
                fileName = file.fileName, viewModel = viewModel
            )
            viewModel.clearDownloadedFile() // Clear the downloaded file in ViewModel
        }
    }

    Scaffold(modifier = Modifier.background(Color(0xFF302C42)),
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .padding(top = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* Handle upload button click */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_file_upload_24),
                            contentDescription = "Upload"
                        )
                    }
                    FloatingActionButton(
                        onClick = { /* Handle floating button click */ },
                        modifier = Modifier.padding(8.dp),
                        elevation = FloatingActionButtonDefaults.elevation(10.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                    IconButton(onClick = { /* Handle download button click */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_download_24),
                            contentDescription = "Download"
                        )
                    }
                }
            }

        }
    ) {
        Column(
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.iiit),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .size(50.dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
            ) {
                var textfieldSize by remember {
                    mutableStateOf(Size.Zero)
                }

                val icon_sub = if (expanded_sub)
                    Icons.Filled.KeyboardArrowUp
                else
                    Icons.Filled.KeyboardArrowDown


                OutlinedTextField(
                    value = selectedSubject,
                    onValueChange = { selectedSubject = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            //This value is used to assign to the DropDown the same width
                            textfieldSize = coordinates.size.toSize()
                        }
                        .clip(RoundedCornerShape(8.dp)),
                    label = { Text("Select Subject") },
                    trailingIcon = {
                        Icon(
                            icon_sub,
                            "contentDescription",
                            Modifier.clickable { expanded_sub = !expanded_sub },
                            tint = Color.White
                        )
                    }
                )

                DropdownMenu(
                    expanded = expanded_sub,
                    onDismissRequest = { expanded_sub = false },
                    modifier = Modifier.width(with(LocalDensity.current) { textfieldSize.width.toDp() })
                ) {
                    subjectList.forEach { label ->
                        DropdownMenuItem(onClick =
                        {
                            selectedSubject = label
                            expanded_sub = false
                        }, text = { Text(text = label) })
                    }
                }


                Log.d("MainFile", selectedSubject)

                val icon_doc = if (expanded_doc)
                    Icons.Filled.KeyboardArrowUp
                else
                    Icons.Filled.KeyboardArrowDown



                OutlinedTextField(
                    value = selectedDocType,
                    onValueChange = { selectedDocType = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            //This value is used to assign to the DropDown the same width
                            textfieldSize = coordinates.size.toSize()
                        },
                    label = { Text("Document") },
                    trailingIcon = {
                        Icon(icon_doc, "contentDescription",
                            Modifier.clickable { expanded_doc = !expanded_doc })
                    }
                )
                DropdownMenu(
                    expanded = expanded_doc,
                    onDismissRequest = { expanded_doc = false },
                    modifier = Modifier.width(with(LocalDensity.current) { textfieldSize.width.toDp() })
                ) {
                    docTypeList.forEach { label ->
                        DropdownMenuItem(onClick =
                        {
                            selectedDocType = label
                            expanded_doc = false
                        }, text = { Text(text = label) })
                    }
                }

                Log.d("MainFile", selectedDocType)

                GradientButton2(
                    gradientColors = listOf(Color(0xFF8176AF), Color(0xFFC0B7E8)),
                    cornerRadius = 10.dp,
                    nameButton = "Browse",
                    roundedCornerShape = RoundedCornerShape(10.dp)
                ) {
                    Log.e("MainFile", "MainFile: $selectedSubject $selectedDocType")
                    val subject = if (selectedSubject.isEmpty()) {
                        // Handle empty subject case here, e.g., set a default value or show an error message
                        // For now, let's assume an empty string as the default value
                        ""
                    } else {
                        selectedSubject
                    }
                    val docType = if (selectedDocType.isEmpty()) {
                        // Handle empty docType case here, e.g., set a default value or show an error message
                        // For now, let's assume an empty string as the default value
                        ""
                    } else {
                        selectedDocType
                    }
                    scope.launch {
                        viewModel.findFiles(sub = subject, docType = docType)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            val fileListKey = remember { mutableStateOf(0) }

            // Observe findFilesValue in LaunchedEffect
            LaunchedEffect(viewModel.findFilesValue) {
                viewModel.findFilesValue.observe(lifecycleOwner, Observer { response ->
                    if (response.isSuccessful) {
                        searching.value = true
                        response.body()?.let {
                            for (file in it) {
                                Log.d(
                                    "MainFile",
                                    "File: ${file.id} ${file.filename} ${file.sub} ${file.documentType}"
                                )
                            }
                            viewModel.files = it
                            // Update the key to trigger recomposition of the FileListView
                            fileListKey.value += 1
                        }
                        Log.d("MainFile", "MainFile: ${response.body()}")
                    } else {
                        searching.value = false
                        Log.d("MainFile", "MainFile: ${response.errorBody()}")
                    }
                })
            }

            // Show the List of files
            ShowFileListView(
                files = viewModel.files,
                appContext = appContext,
                viewModel = viewModel,
                lifecycleOwner = lifecycleOwner,
                searching = searching.value,
                fileListKey = fileListKey.value
            )
        }
    }


}


@Composable
fun ShowFileListView(
    files: List<FileItem>,
    appContext: Context,
    viewModel: MainViewModel,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    searching: Boolean,
    fileListKey: Int,
    modifier: Modifier = Modifier
) {
    if (searching) {
        // Use the key parameter to trigger recomposition of the FileListView
        key(fileListKey) {
            FileListView(
                files = files,
                appContext = appContext,
                viewModel = viewModel,
                lifecycleOwner = lifecycleOwner,
                modifier = modifier
            )
        }
    }
}

@Composable
fun FileListView(
    files: List<FileItem>,
    appContext: Context,
    viewModel: MainViewModel,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp)
            .background(Color.Transparent), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(files) { file ->

            // Card for each file
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(Color(0xFF403A5F), Color(0xFF211E2E)),
                                center = Offset(500f, 200f),
                                radius = 350f,
                                tileMode = TileMode.Clamp
                            ),
                            shape = RectangleShape
                        )
                        .clip(RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // Column for file details
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            EllipsisText(text = "Name:${file.filename}", maxLength = 35)

                            EllipsisText(text = "Subject:${file.sub}", maxLength = 25)

                            EllipsisText(text = "Uploaded By:${file.uploader}", maxLength = 25)

                            EllipsisText(text = "Uploaded Date:${file.uploadDate}", maxLength = 25)
                        }

                        // Download Icon
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.weight(0.5f)
                        ) {

                            GradientButton2(
                                gradientColors = listOf(Color(0xFF8176AF), Color(0xFFC0B7E8)),
                                cornerRadius = 10.dp,
                                nameButton = "Download",
                                roundedCornerShape = RoundedCornerShape(10.dp)
                            ) {
                                viewModel.downloadFile(file.id)
                                viewModel.downloadfileValue.observe(lifecycleOwner) { response ->
                                    if (response != null) {
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
                                    } else {
                                        Log.d("MainFile", "MainFile: The response Body is null")
                                    }
                                }
                            }

                            GradientButton2(
                                gradientColors = listOf(Color(0xFF8176AF), Color(0xFFC0B7E8)),
                                cornerRadius = 10.dp,
                                nameButton = "Share",
                                roundedCornerShape = RoundedCornerShape(10.dp)
                            ) {


                            }


                        }


                    }

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
        overflow = TextOverflow.Ellipsis,
        color = Color(0xFFC0B7E8)
    )
}

//Download file and save it to Downloads folder
fun saveResponseBodyAsPdf(
    responseBody: ResponseBody?,
    context: Context,
    fileName: String,
    viewModel: MainViewModel
) {
    try {
        viewModel.clearDownloadedFile() // Clear the previous downloaded file, if any

        val storageDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val outputFile = File(storageDir, fileName)

        val inputStream = responseBody?.byteStream()
        val outputStream = FileOutputStream(outputFile)

        val buffer = ByteArray(4096)
        var bytesRead: Int = 0
        while (inputStream?.read(buffer).also {
                if (it != null) {
                    bytesRead = it
                }
            } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }

        outputStream.flush()
        outputStream.close()
        inputStream?.close()

        if (responseBody != null) {
            viewModel.setDownloadedFile(responseBody, fileName)
        } else {
            Log.d("MainFile", "MainFile: The response body is null")
        }

        showToast(context, "File downloaded successfully${responseBody?.contentLength()}")
    } catch (e: IOException) {
        e.printStackTrace()
        showToast(context, "Error downloading file")
    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Composable
fun GradientButton2(
    gradientColors: List<Color>,
    cornerRadius: Dp,
    nameButton: String,
    roundedCornerShape: RoundedCornerShape,
    onClick: () -> Unit
) {

    Button(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(cornerRadius),
        elevation = ButtonDefaults.buttonElevation(10.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(colors = gradientColors),
                    shape = roundedCornerShape
                )
                .clip(roundedCornerShape)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = nameButton,
                color = Color.White
            )
        }
    }
}







