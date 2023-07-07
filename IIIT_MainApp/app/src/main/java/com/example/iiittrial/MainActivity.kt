package com.example.iiittrial

import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.iiittrial.data.models.FileItem
import com.example.iiittrial.domain.repo.FileRepo
import com.example.iiittrial.presentation.MainViewModel
import com.example.iiittrial.ui.EllipsisText
import com.example.iiittrial.ui.FileListView
import com.example.iiittrial.ui.GradientButton2
import com.example.iiittrial.ui.MainScreen
import com.example.iiittrial.ui.ShowFileListView
import com.example.iiittrial.ui.theme.IiittrialTheme
import com.example.iiittrial.util.SetupNavGraph
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IiittrialTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    SetupNavGraph(navController = navController)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun trial() {
    val files = listOf(
        FileItem(
            id = "1",
            filename = "file1",
            uploader = 1,
            uploadDate = "date1",
            sub = "sub1",
            contentType = "application/pdf",
            size = 1000,
            documentType = "pdf",
            path = "path1"
        ),
        FileItem(
            id = "2",
            filename = "file2",
            uploader = 1,
            uploadDate = "date2",
            sub = "sub2", contentType = "application/pdf",
            size = 1000,
            documentType = "pdf",
            path = "path1"
        ),
        FileItem(
            id = "3",
            filename = "file3",
            uploader = 1,
            uploadDate = "date3",
            sub = "sub3", contentType = "application/pdf",
            size = 1000,
            documentType = "pdf",
            path = "path1"
        ),
        FileItem(
            id = "4",
            filename = "file4",
            uploader = 1,
            uploadDate = "date4",
            sub = "sub4", contentType = "application/pdf",
            size = 1000,
            documentType = "pdf",
            path = "path1"
        )
    )

    val appContext: Context

    val lifecycleOwner = LocalLifecycleOwner.current

    val viewModel = viewModel<MainViewModel>(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(FileRepo()) as T
        }
    })

    val modifier = Modifier

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
                painter = painterResource(id =R.drawable.iiit),
                contentDescription =null,
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
                            Modifier.clickable { expanded_sub = !expanded_sub }
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
                        //viewModel.findFiles(sub = subject, docType = docType)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            val fileListKey = remember { mutableStateOf(0) }

            // Observe findFilesValue in LaunchedEffect
            /*LaunchedEffect(viewModel.findFilesValue) {
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
            }*/

            val files_trial = listOf(
                FileItem(
                    id = "1",
                    filename = "file1",
                    uploader = 1,
                    uploadDate = "date1",
                    sub = "sub1",
                    contentType = "application/pdf",
                    size = 1000,
                    documentType = "pdf",
                    path = "path1"
                ),
                FileItem(
                    id = "2",
                    filename = "file2",
                    uploader = 1,
                    uploadDate = "date2",
                    sub = "sub2", contentType = "application/pdf",
                    size = 1000,
                    documentType = "pdf",
                    path = "path1"
                ),
                FileItem(
                    id = "3",
                    filename = "file3",
                    uploader = 1,
                    uploadDate = "date3",
                    sub = "sub3", contentType = "application/pdf",
                    size = 1000,
                    documentType = "pdf",
                    path = "path1"
                ),
                FileItem(
                    id = "4",
                    filename = "file4",
                    uploader = 1,
                    uploadDate = "date4",
                    sub = "sub4", contentType = "application/pdf",
                    size = 1000,
                    documentType = "pdf",
                    path = "path1"
                ),
                FileItem(
                    id = "5",
                    filename = "file1",
                    uploader = 1,
                    uploadDate = "date1",
                    sub = "sub1",
                    contentType = "application/pdf",
                    size = 1000,
                    documentType = "pdf",
                    path = "path1"
                )
            )

            // Show the List of files
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, end = 10.dp)
                    .weight(1f)
                    .background(Color.Transparent), verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(files_trial) { file ->

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
                                    EllipsisText(text = "Name:", maxLength = 35)

                                    EllipsisText(text = "Subject:", maxLength = 25)

                                    EllipsisText(text = "Uploaded By:", maxLength = 25)

                                    EllipsisText(text = "Uploaded Date:", maxLength = 25)
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
                                        //viewModel.downloadFile(file.id)
//                                viewModel.downloadfileValue.observe(lifecycleOwner) { response ->
//                                    if (response != null) {
//                                        if (response.isSuccessful) {
//                                            response.body()?.let {
//                                                saveResponseBodyAsPdf(
//                                                    responseBody = it,
//                                                    context = appContext,
//                                                    fileName = file.filename,
//                                                    viewModel = viewModel
//                                                )
//                                            }
//                                        }
//                                    } else {
//                                        Log.d("MainFile", "MainFile: The response Body is null")
//                                    }
//                                }
                                    }

//                        Button(
//                            onClick = {
//                                // Handle share button logic
//                            },
//                            colors = ButtonDefaults.buttonColors(containerColor  = Color.Transparent),
//                            border = BorderStroke(1.dp, Color.White),
//                            modifier = Modifier
//                                .padding(top = 8.dp)
//                                .fillMaxWidth(),
//                        ) {
//                            Text(text = "Share", color = Color(0xFF343045))
//                        }

                                    GradientButton2(
                                        gradientColors = listOf(Color(0xFF8176AF), Color(0xFFC0B7E8)),
                                        cornerRadius = 10.dp,
                                        nameButton = "share",
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
    }

}