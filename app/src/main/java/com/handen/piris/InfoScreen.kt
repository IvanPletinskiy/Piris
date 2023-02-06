package com.handen.piris

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun InfoScreen(viewModel: MainViewModel) {
    val navController = LocalNavController.current
    Scaffold(topBar = {
        TopAppBar(
            backgroundColor = Color.White
        ) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    Icons.Filled.ArrowBack, contentDescription = null, tint = Color.Black
                )
            }
        }
    }) {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(it)
                .padding(4.dp)
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            val client = viewModel.client.collectAsState().value
            val errors = viewModel.uiErrors.collectAsState().value

            Text("Имя", style = MaterialTheme.typography.h6)
            Text(text = client.name)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Фамилия", style = MaterialTheme.typography.h6)
            Text(text = client.surname)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Отчество", style = MaterialTheme.typography.h6)
            Text(text = client.patronymic)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Дата рождения", style = MaterialTheme.typography.h6)
            Text(text = client.birthday)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Серия паспорта", style = MaterialTheme.typography.h6)
            Text(text = client.passportSeries)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Номер паспорта", style = MaterialTheme.typography.h6)
            Text(text = client.passportNumber)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Орган выдачи паспорта", style = MaterialTheme.typography.h6)
            Text(text = client.passportIssuedBy)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Дата выдачи паспорта", style = MaterialTheme.typography.h6)
            Text(text = client.passportIssueDate)
            Spacer(modifier = Modifier.height(4.dp))

            Text("Идентификационный номер", style = MaterialTheme.typography.h6)
            Text(text = client.identificationNumber)
            Spacer(modifier = Modifier.height(4.dp))

            Text("Место рождения", style = MaterialTheme.typography.h6)
            Text(text = client.birthPlace)
            Spacer(modifier = Modifier.height(4.dp))

            Text("Город проживания", style = MaterialTheme.typography.h6)
            Text(text = client.city?.name.orEmpty())
            Spacer(modifier = Modifier.height(4.dp))

            Text("Адрес регистрации", style = MaterialTheme.typography.h6)
            Text(text = client.address)
            Spacer(modifier = Modifier.height(4.dp))

            Text("Email", style = MaterialTheme.typography.h6)
            Text(text = client.email.orEmpty())
            Spacer(modifier = Modifier.height(4.dp))

            Text("Место работы", style = MaterialTheme.typography.h6)
            Text(text = client.workplace.orEmpty())
            Spacer(modifier = Modifier.height(4.dp))

            Text("Должность", style = MaterialTheme.typography.h6)
            Text(text = client.position.orEmpty())
            Spacer(modifier = Modifier.height(4.dp))

            Text("Город регистрации", style = MaterialTheme.typography.h6)
            Text(text = client.registrationCity?.name.orEmpty())
            Spacer(modifier = Modifier.height(4.dp))

            Text("Семейное положение", style = MaterialTheme.typography.h6)
            Text(text = client.marriageStatus?.name.orEmpty())
            Spacer(modifier = Modifier.height(4.dp))

            Text("Гражданство", style = MaterialTheme.typography.h6)
            Text(text = client.citizenship?.name.orEmpty())
            Spacer(modifier = Modifier.height(4.dp))

            Text("Инвалидность", style = MaterialTheme.typography.h6)
            Text(text = client.disability?.name.orEmpty())
            Spacer(modifier = Modifier.height(4.dp))

            Text("Пенсионер", style = MaterialTheme.typography.h6)
            Checkbox(
                checked = client.retired,
                onCheckedChange = viewModel::onRetiredChanged,
                enabled = false
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text("Доход", style = MaterialTheme.typography.h6)
            Text(text = client.income.orEmpty())
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}