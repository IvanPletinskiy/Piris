package com.handen.piris.accounts

import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.handen.piris.LocalNavController
import com.handen.piris.MainViewModel

@Composable
fun AccountsScreen(mainViewModel: MainViewModel) {
    val viewModel: AccountsViewModel = viewModel()
    val accounts by viewModel.accountsUi.collectAsState()
    val currentDate by viewModel.currentDate.collectAsState()
    Column(Modifier.fillMaxSize()) {
        Text(
            text = "Текущая дата: ${MainViewModel.dateFormatter.format(currentDate)}",
            style = MaterialTheme.typography.h6
        )
        Row(
            Modifier
                .horizontalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            val navController = LocalNavController.current
            Button(onClick = {
                navController.navigate("create_deposit")
            }) {
                Text(text = "Открыть депозит")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                viewModel.skip30Days()
            }) {
                Text(text = "Пропустить 30 дней")
            }
        }
        LazyColumn(Modifier.fillMaxSize()) {
            items(accounts) { account ->
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Card {
                        Row(
                            Modifier.padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(text = account.number, style = MaterialTheme.typography.h6)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = account.owner)
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = account.amount,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f),
                                style = MaterialTheme.typography.h6,
                                textAlign = TextAlign.Right
                            )
                        }
                    }
                }
            }
        }
    }
}