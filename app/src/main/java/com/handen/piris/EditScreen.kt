package com.handen.piris

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditScreen(viewModel: MainViewModel) {
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
        ) {
            val client = viewModel.client.collectAsState().value
            val errors = viewModel.uiErrors.collectAsState().value

            Text("Имя", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.name,
                onValueChange = viewModel::onNameChanged,
                isError = errors.nameError != null,
                label = {
                    Text(
                        text = errors.nameError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            Text("Фамилия", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.surname,
                onValueChange = viewModel::onSurnameChanged,
                isError = errors.surnameError != null,
                label = {
                    Text(
                        text = errors.surnameError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            Text("Отчество", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.patronymic,
                onValueChange = viewModel::onPatronymicChanged,
                isError = errors.patronymicError != null,
                label = {
                    Text(
                        text = errors.patronymicError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            Text("Дата рождения", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.birthday,
                onValueChange = viewModel::onBirthdayChanged,
                isError = errors.birthdayError != null,
                label = {
                    Text(
                        text = errors.birthdayError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            Text("Серия паспорта", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.passportSeries,
                onValueChange = viewModel::onPassportSeriesChanged,
                isError = errors.passportSeriesError != null,
                label = {
                    Text(
                        text = errors.passportSeriesError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            Text("Номер паспорта", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.passportNumber,
                onValueChange = viewModel::onPassportNumberChanged,
                isError = errors.passportNumberError != null,
                label = {
                    Text(
                        text = errors.passportNumberError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            Text("Орган выдачи паспорта", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.passportIssuedBy,
                onValueChange = viewModel::onPassportIssuedByChanged,
                isError = errors.passportIssuedByError != null,
                label = {
                    Text(
                        text = errors.passportIssuedByError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            Text("Дата выдачи паспорта", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.passportIssueDate,
                onValueChange = viewModel::onPassportIssuedDateChanged,
                isError = errors.passportIssueDateError != null,
                label = {
                    Text(
                        text = errors.passportIssueDateError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            Text("Идентификационный номер", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.identificationNumber,
                onValueChange = viewModel::onIndentificationNumberChanged,
                isError = errors.identificationNumberError != null,
                label = {
                    Text(
                        text = errors.identificationNumberError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            Text("Место рождения", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.birthPlace,
                onValueChange = viewModel::onBirthPlaceChanged,
                isError = errors.birthPlaceError != null,
                label = {
                    Text(
                        text = errors.birthPlaceError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            var isCityExpanded = false
            Text("Город проживания", style = MaterialTheme.typography.h6)
            ExposedDropdownMenuBox(expanded = isCityExpanded, onExpandedChange = {
                isCityExpanded = !isCityExpanded
            }) {
                TextField(readOnly = true,
                    value = client.city?.name.orEmpty(),
                    onValueChange = { },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = isCityExpanded
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier.fillMaxWidth(),
                    isError = errors.cityError != null,
                    label = {
                        Text(
                            text = errors.cityError.orEmpty()
                        )
                    })
                ExposedDropdownMenu(expanded = isCityExpanded, onDismissRequest = {
                    isCityExpanded = false
                }) {
                    viewModel.cities.forEach { item ->
                        DropdownMenuItem(onClick = {
                            viewModel.onCityChanged(item)
                            isCityExpanded = false
                        }) {
                            Text(text = item.name)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Адрес регистрации", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.address,
                onValueChange = viewModel::onAddressChanged,
                isError = errors.addressError != null,
                label = {
                    Text(
                        text = errors.addressError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            Text("Email", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.email.orEmpty(),
                onValueChange = viewModel::onEmailChanged,
                isError = errors.emailError != null,
                label = {
                    Text(
                        text = errors.emailError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            Text("Место работы", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.workplace.orEmpty(),
                onValueChange = viewModel::onWorkplaceChanged,
                isError = errors.workplaceError != null,
                label = {
                    Text(
                        text = errors.workplaceError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            Text("Должность", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.position.orEmpty(),
                onValueChange = viewModel::onPositionChanged,
                isError = errors.positionError != null,
                label = {
                    Text(
                        text = errors.positionError.orEmpty()
                    )
                })

            Text("Город регистрации", style = MaterialTheme.typography.h6)
            var isRegistrationCityExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = isRegistrationCityExpanded, onExpandedChange = {
                isRegistrationCityExpanded = !isRegistrationCityExpanded
            }) {
                TextField(readOnly = true,
                    value = client.registrationCity?.name.orEmpty(),
                    onValueChange = { },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = isRegistrationCityExpanded
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier.fillMaxWidth(),
                    isError = errors.registrationCityError != null,
                    label = {
                        Text(
                            text = errors.registrationCityError.orEmpty()
                        )
                    })
                ExposedDropdownMenu(expanded = isRegistrationCityExpanded, onDismissRequest = {
                    isRegistrationCityExpanded = false
                }) {
                    viewModel.cities.forEach { item ->
                        DropdownMenuItem(onClick = {
                            viewModel.onRegistrationCityChanged(item)
                            isRegistrationCityExpanded = false
                        }) {
                            Text(text = item.name)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text("Семейное положение", style = MaterialTheme.typography.h6)
            var isMarriageStatusExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = isMarriageStatusExpanded, onExpandedChange = {
                isMarriageStatusExpanded = !isMarriageStatusExpanded
            }) {
                TextField(readOnly = true,
                    value = client.marriageStatus?.name.orEmpty(),
                    onValueChange = { },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = isMarriageStatusExpanded
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier.fillMaxWidth(),
                    isError = errors.marriageStatusError != null,
                    label = {
                        Text(
                            text = errors.marriageStatusError.orEmpty()
                        )
                    })
                ExposedDropdownMenu(expanded = isMarriageStatusExpanded, onDismissRequest = {
                    isMarriageStatusExpanded = false
                }) {
                    viewModel.marriageStatuses.forEach { item ->
                        DropdownMenuItem(onClick = {
                            viewModel.onMarriageStatusesChanged(item)
                            isMarriageStatusExpanded = false
                        }) {
                            Text(text = item.name)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text("Гражданство", style = MaterialTheme.typography.h6)
            var isCitizenshipExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = isCitizenshipExpanded, onExpandedChange = {
                isCitizenshipExpanded = !isCitizenshipExpanded
            }) {
                TextField(readOnly = true,
                    value = client.citizenship?.name.orEmpty(),
                    onValueChange = { },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = isCitizenshipExpanded
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier.fillMaxWidth(),
                    isError = errors.citizenshipError != null,
                    label = {
                        Text(
                            text = errors.citizenshipError.orEmpty()
                        )
                    })
                ExposedDropdownMenu(expanded = isCitizenshipExpanded, onDismissRequest = {
                    isCitizenshipExpanded = false
                }) {
                    viewModel.citizenships.forEach { item ->
                        DropdownMenuItem(onClick = {
                            viewModel.onCitizenshipChanged(item)
                            isCitizenshipExpanded = false
                        }) {
                            Text(text = item.name)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text("Инвалидность", style = MaterialTheme.typography.h6)
            var isDisabilitiesExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = isDisabilitiesExpanded, onExpandedChange = {
                isDisabilitiesExpanded = !isDisabilitiesExpanded
            }) {
                TextField(readOnly = true,
                    value = client.disability?.name.orEmpty(),
                    onValueChange = { },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = isDisabilitiesExpanded
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier.fillMaxWidth(),
                    isError = errors.disabilityError != null,
                    label = {
                        Text(
                            text = errors.disabilityError.orEmpty()
                        )
                    })
                ExposedDropdownMenu(expanded = isDisabilitiesExpanded, onDismissRequest = {
                    isDisabilitiesExpanded = false
                }) {
                    viewModel.disabilities.forEach { item ->
                        DropdownMenuItem(onClick = {
                            viewModel.onDisabilitySelected(item)
                            isDisabilitiesExpanded = false
                        }) {
                            Text(text = item.name)
                        }
                    }
                }
            }


            Text("Пенсионер", style = MaterialTheme.typography.h6)
            Checkbox(
                checked = client.retired, onCheckedChange = viewModel::onRetiredChanged,
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Доход", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.income.orEmpty(),
                onValueChange = viewModel::onIncomeChanged,
                isError = errors.incomeError != null,
                label = {
                    Text(
                        text = errors.incomeError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(), contentAlignment = Alignment.Center
            ) {
                Button(onClick = viewModel::onSaveButtonClicked) {
                    Text("Сохранить")
                }
            }
        }
    }
}