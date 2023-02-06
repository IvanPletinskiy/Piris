package com.handen.piris

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import javax.net.ssl.KeyStoreBuilderParameters

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
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .wrapContentHeight()
        ) {
            val client = viewModel.client.collectAsState().value
            val errors = viewModel.uiErrors.collectAsState().value

            Text("Имя", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.name,
                onValueChange = viewModel::onNameChanged,
                isError = errors.nameError != null,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words, keyboardType = KeyboardType.Text
                ),
                label = {
                    Text(
                        color = Color.Red, text = errors.nameError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            Text("Фамилия", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.surname,
                onValueChange = viewModel::onSurnameChanged,
                isError = errors.surnameError != null,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words, keyboardType = KeyboardType.Text
                ),
                label = {
                    Text(
                        color = Color.Red, text = errors.surnameError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            Text("Отчество", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.patronymic,
                onValueChange = viewModel::onPatronymicChanged,
                isError = errors.patronymicError != null,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words, keyboardType = KeyboardType.Text
                ),
                label = {
                    Text(
                        color = Color.Red, text = errors.patronymicError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            Text("Дата рождения", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.birthday,
                onValueChange = viewModel::onBirthdayChanged,
                isError = errors.birthdayError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                label = {
                    Text(
                        color = Color.Red, text = errors.birthdayError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            Text("Серия паспорта", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.passportSeries,
                onValueChange = viewModel::onPassportSeriesChanged,
                isError = errors.passportSeriesError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Characters
                ),
                label = {
                    Text(
                        color = Color.Red, text = errors.passportSeriesError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            Text("Номер паспорта", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.passportNumber,
                onValueChange = viewModel::onPassportNumberChanged,
                isError = errors.passportNumberError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                label = {
                    Text(
                        color = Color.Red, text = errors.passportNumberError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            Text("Орган выдачи паспорта", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.passportIssuedBy,
                onValueChange = viewModel::onPassportIssuedByChanged,
                isError = errors.passportIssuedByError != null,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words, keyboardType = KeyboardType.Text
                ),
                label = {
                    Text(
                        color = Color.Red, text = errors.passportIssuedByError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            Text("Дата выдачи паспорта", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.passportIssueDate,
                onValueChange = viewModel::onPassportIssuedDateChanged,
                isError = errors.passportIssueDateError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                label = {
                    Text(
                        color = Color.Red, text = errors.passportIssueDateError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            Text("Идентификационный номер", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.identificationNumber,
                onValueChange = viewModel::onIndentificationNumberChanged,
                isError = errors.identificationNumberError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Characters
                ),
                label = {
                    Text(
                        color = Color.Red, text = errors.identificationNumberError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            Text("Место рождения", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.birthPlace,
                onValueChange = viewModel::onBirthPlaceChanged,
                isError = errors.birthPlaceError != null,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words, keyboardType = KeyboardType.Text
                ),
                label = {
                    Text(
                        color = Color.Red, text = errors.birthPlaceError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            var isCityExpanded by remember { mutableStateOf(false) }
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
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                label = {
                    Text(
                        color = Color.Red, text = errors.addressError.orEmpty()
                    )
                })
            Text("Домашний номер", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.homeNumber.orEmpty(),
                onValueChange = viewModel::onHomeNumberChanged,
                isError = errors.homeNumberError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                visualTransformation = MaskTransformation(),
                label = {
                    Text(
                        color = Color.Red, text = errors.homeNumberError.orEmpty()
                    )
                })
            Text("Мобильный номер", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.mobileNumber.orEmpty(),
                onValueChange = viewModel::onMobileNumberChanged,
                isError = errors.mobileNumberError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                visualTransformation = MaskTransformation(),
                label = {
                    Text(
                        color = Color.Red, text = errors.mobileNumberError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            Text("Email", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.email.orEmpty(),
                onValueChange = viewModel::onEmailChanged,
                isError = errors.emailError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                label = {
                    Text(
                        color = Color.Red, text = errors.emailError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            Text("Место работы", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.workplace.orEmpty(),
                onValueChange = viewModel::onWorkplaceChanged,
                isError = errors.workplaceError != null,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words, keyboardType = KeyboardType.Text
                ),
                label = {
                    Text(
                        color = Color.Red, text = errors.workplaceError.orEmpty()
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            Text("Должность", style = MaterialTheme.typography.h6)
            OutlinedTextField(value = client.position.orEmpty(),
                onValueChange = viewModel::onPositionChanged,
                isError = errors.positionError != null,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words, keyboardType = KeyboardType.Text
                ),
                label = {
                    Text(
                        color = Color.Red, text = errors.positionError.orEmpty()
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
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                label = {
                    Text(
                        color = Color.Red, text = errors.incomeError.orEmpty()
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

class MaskTransformation() : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return maskFilter(text)
    }
}


fun maskFilter(text: AnnotatedString): TransformedText {

    // +375 (29) 319-03-65
    val trimmed = if (text.text.length >= 13) text.text.substring(0..12) else text.text
    var out = ""
//    for (i in trimmed.indices) {
//        out += trimmed[i]
//        if (i==4) out += "-"
        out += text.getOrNull(0) ?: "+"
        out += text.getOrNull(1) ?: " "
        out += text.getOrNull(2) ?: " "
        out += text.getOrNull(3) ?: " "
        out += " "
        out += "("
        out += text.getOrNull(4) ?: " "
        out += text.getOrNull(5) ?: " "
        out += ")"
        out += " "
        out += text.getOrNull(6) ?: " "
        out += text.getOrNull(7) ?: " "
        out += text.getOrNull(8) ?: " "
        out += "-"
        out += text.getOrNull(9) ?: " "
        out += text.getOrNull(10) ?: " "
        out += "-"
        out += text.getOrNull(11) ?: " "
        out += text.getOrNull(12) ?: " "
//    }

    val numberOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 4) return offset
            if (offset <= 6) return offset + 2
            if (offset <= 9) return offset + 4
            if (offset <= 11) return offset + 5
            return 19

        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <= 4) return offset
            if (offset <= 6) return offset - 2
            if (offset <= 9) return offset - 4
            if (offset <= 11) return offset - 5
            return 12
        }
    }

    return TransformedText(AnnotatedString(out), numberOffsetTranslator)
}