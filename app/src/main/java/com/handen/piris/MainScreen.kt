package com.handen.piris

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.handen.piris.data.Client
import kotlinx.coroutines.launch

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val navController = LocalNavController.current
    var showDeleteDialog by remember { mutableStateOf<Pair<Boolean, Client?>>(false to null) }

    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .horizontalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Button(onClick = {
                viewModel.setClient(viewModel.createEmptyClient())
                navController.navigate("edit")
            }) {
                Text(text = "Добавить клиента")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                navController.navigate("accounts")
            }) {
                Text(text = "Счета")
            }
        }

        val clients by viewModel.clients.collectAsState(emptyList())
        LazyColumn(Modifier.fillMaxSize()) {
            items(clients) { client ->
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)) {
                    Card() {
                        Row(Modifier.padding(4.dp)) {
                            Column(Modifier.weight(1f)) {
                                Text(text = client.surname, style = MaterialTheme.typography.h6)
                                Spacer(modifier = Modifier.height(4.dp))
                                val desc = "${client.name} ${client.patronymic}"
                                Text(text = desc)
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(painter = rememberVectorPainter(image = Icons.Filled.Info),
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    viewModel.setClient(client)
                                    navController.navigate("info")
                                })
                            Icon(painter = rememberVectorPainter(image = Icons.Filled.Edit),
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    viewModel.setClient(client)
                                    navController.navigate("edit")
                                })
                            Icon(painter = rememberVectorPainter(image = Icons.Filled.Delete),
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    showDeleteDialog = true to client
                                })
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog.first) {

        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onCloseRequest.
                showDeleteDialog = false to null
            },
            title = {
                Text(text = "Подтверждение")
            },
            text = {
                Text("Вы действительно хотите удалить данного клиента?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteClient(showDeleteDialog.second)
                        showDeleteDialog = false to null
                    }) {
                    Text("Да")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false to null
                    }) {
                    Text("Отмена")
                }
            }
        )
    }
}