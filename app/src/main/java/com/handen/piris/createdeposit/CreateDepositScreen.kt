package com.handen.piris

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.handen.piris.createdeposit.CreateDepositViewModel
import com.handen.piris.data.DepositType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateDepositScreen(mainViewModel: MainViewModel) {
    val viewModel: CreateDepositViewModel by viewModel()
    val clients by mainViewModel.clients.collectAsState(initial = emptyList())
    val client by viewModel.client.collectAsState()
    val errors by viewModel.uiErrors.collectAsState()
    val deposit by viewModel.deposit.collectAsState()

    Text("Выберите клиента", style = MaterialTheme.typography.h6)
    var isClientFieldExpanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = isClientFieldExpanded, onExpandedChange = {
        isClientFieldExpanded = !isClientFieldExpanded
    }) {
        TextField(readOnly = true,
            value = "${client?.name.orEmpty()} ${client?.surname.orEmpty()} ${client?.patronymic.orEmpty()}",
            onValueChange = { },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = isClientFieldExpanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.fillMaxWidth(),
            isError = errors.clientError != null,
            label = {
                Text(
                    text = errors.clientError.orEmpty()
                )
            })
        ExposedDropdownMenu(expanded = isClientFieldExpanded, onDismissRequest = {
            isClientFieldExpanded = false
        }) {
            clients.forEach { item ->
                DropdownMenuItem(onClick = {
                    viewModel.onClientChanged(item)
                    isClientFieldExpanded = false
                }) {
                    Text(text = "${item.name} ${item.surname} ${item.patronymic}")
                }
            }
        }
    }

    Text("Сумма", style = MaterialTheme.typography.h6)
    OutlinedTextField(value = deposit.amount,
        onValueChange = viewModel::onAmountChanged,
        isError = errors.amountError != null,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        label = {
            Text(
                color = Color.Red, text = errors.amountError.orEmpty()
            )
        })
    Spacer(modifier = Modifier.height(16.dp))

    Text("Номер договора", style = MaterialTheme.typography.h6)
    OutlinedTextField(value = deposit.agreementNumber,
        onValueChange = viewModel::onAgreementNumberChanged,
        isError = errors.agreementError != null,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        label = {
            Text(
                color = Color.Red, text = errors.agreementError.orEmpty()
            )
        })
    Spacer(modifier = Modifier.height(16.dp))


    Text("Процент, %", style = MaterialTheme.typography.h6)
    OutlinedTextField(value = (deposit.yield * 100).toInt().toString(),
        onValueChange = viewModel::onYieldChanged,
        isError = errors.yieldError != null,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        label = {
            Text(
                color = Color.Red, text = errors.yieldError.orEmpty()
            )
        })
    Spacer(modifier = Modifier.height(16.dp))

    Text("Валюта", style = MaterialTheme.typography.h6)
    var isCurrencyFieldExpanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = isCurrencyFieldExpanded, onExpandedChange = {
        isCurrencyFieldExpanded = !isCurrencyFieldExpanded
    }) {
        TextField(readOnly = true,
            value = deposit.currencyCode,
            onValueChange = { },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = isCurrencyFieldExpanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.fillMaxWidth(),
            isError = errors.currencyError != null,
            label = {
                Text(
                    text = errors.currencyError.orEmpty()
                )
            })
        ExposedDropdownMenu(expanded = isCurrencyFieldExpanded, onDismissRequest = {
            isCurrencyFieldExpanded = false
        }) {
            viewModel.currencies.forEach { item ->
                DropdownMenuItem(onClick = {
                    viewModel.onCurrencyChanged(item.name)
                    isCurrencyFieldExpanded = false
                }) {
                    Text(text = item.name)
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))

    Text("Дата начала", style = MaterialTheme.typography.h6)
    OutlinedTextField(value = deposit.startDate,
        onValueChange = viewModel::onStartDateChanged,
        isError = errors.startDateError != null,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ),
        label = {
            Text(
                color = Color.Red, text = errors.startDateError.orEmpty()
            )
        })
    Spacer(modifier = Modifier.height(16.dp))

    Text("Дата окончания", style = MaterialTheme.typography.h6)
    OutlinedTextField(value = deposit.endDate,
        onValueChange = viewModel::onEndDateChanged,
        isError = errors.endDateError != null,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ),
        label = {
            Text(
                color = Color.Red, text = errors.endDateError.orEmpty()
            )
        })
    Spacer(modifier = Modifier.height(16.dp))

    Text("Тип депозита", style = MaterialTheme.typography.h6)
    var isDepositTypeExpanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = isDepositTypeExpanded, onExpandedChange = {
        isDepositTypeExpanded = !isDepositTypeExpanded
    }) {
        TextField(readOnly = true,
            value = deposit.type.displayName,
            onValueChange = { },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = isDepositTypeExpanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.fillMaxWidth(),
            isError = errors.depositTypeError != null,
            label = {
                Text(
                    text = errors.depositTypeError.orEmpty()
                )
            })
        ExposedDropdownMenu(expanded = isDepositTypeExpanded, onDismissRequest = {
            isDepositTypeExpanded = false
        }) {
            DepositType.values().forEach { item ->
                DropdownMenuItem(onClick = {
                    viewModel.onDepositTypeChanged(item)
                    isDepositTypeExpanded = false
                }) {
                    Text(text = item.displayName)
                }
            }
        }
    }
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current
    val context = LocalContext.current
    Box(
        Modifier
            .fillMaxWidth()
            .padding(), contentAlignment = Alignment.Center
    ) {
        Button(onClick = {
            scope.launch {
                val result = viewModel.onSaveButtonClicked(mainViewModel).await()
                if (result) {
                    navController.popBackStack()
                } else {
                    Toast.makeText(context, "Исправьте ошибки заполнения полей", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }) {
            Text("Сохранить")
        }
    }
}