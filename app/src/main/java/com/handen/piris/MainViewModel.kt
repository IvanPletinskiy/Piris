package com.handen.piris

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.handen.piris.data.AppDatabase
import com.handen.piris.data.Citizenship
import com.handen.piris.data.City
import com.handen.piris.data.Client
import com.handen.piris.data.ClientRepository
import com.handen.piris.data.ClientRepositoryImpl
import com.handen.piris.data.Disability
import com.handen.piris.data.MarriageStatus
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel(private val context: Context) : ViewModel() {
    private val db: AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "piris_database")
            .allowMainThreadQueries().addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                }

                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                }

                override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                    super.onDestructiveMigration(db)
                }
            }).build()

    private val repository: ClientRepository = ClientRepositoryImpl(db.clientDao)

    val cities = listOf(
        City(0, "Солигорск"),
        City(1, "Минск"),
        City(2, "Барановичи"),
        City(3, "Гродно"),
        City(4, "Брест")
    )
    val marriageStatuses = listOf(
        MarriageStatus(0, "Не женат/не замужем"),
        MarriageStatus(1, "Женат/замужем"),
        MarriageStatus(2, "Вдовец/вдова")
    )
    val disabilities = listOf(
        Disability(0, "Нет"),
        Disability(1, "Инвалид 1 группы"),
        Disability(2, "Инвалид 2 группы"),
        Disability(3, "Инвалид 3 группы")
    )

    val citizenships = listOf(
        Citizenship(0, "белорусское"),
        Citizenship(1, "российское"),
        Citizenship(2, "украинское"),
        Citizenship(3, "немецкое"),
    )

    val client = MutableStateFlow(
        Client(
            id = 0,
            name = "Иван",
            surname = "Плетинский",
            patronymic = "Валерьевич",
            birthday = "30 марта 2002",
            passportSeries = "MC",
            passportNumber = "2720337",
            passportIssuedBy = "Солигорский РОВД",
            passportIssueDate = "20.10.2020",
            identificationNumber = "53003PB002",
            birthPlace = "Солигорск",
            city = City(id = 0, "Солигорск"),
            address = "г. Минск, пр-т. Прытыцкого, д. 42, кв. 67",
            homeNumber = null,
            mobileNumber = null,
            email = null,
            workplace = null,
            position = null,
            registrationCity = City(id = 1, "Минск"),
            marriageStatus = MarriageStatus(0, "Не женат"),
            citizenship = Citizenship(id = 0, "белорус"),
            disability = Disability(id = 0, "Нет"),
            retired = false,
            income = null
        )
    )

    val clients = repository.getClients()

    val uiErrors = MutableStateFlow(
        UiErrors()
    )

    val uiEvents = MutableSharedFlow<UiEvent>()

    fun onNameChanged(name: String) {
        client.value = client.value.copy(name = name)
        if (name.isBlank()) {
            uiErrors.update {
                it.copy(nameError = FIELD_CANNOT_BE_EMPTY)
            }
        } else {
            uiErrors.update {
                it.copy(nameError = null)
            }
        }
    }

    fun onSurnameChanged(surname: String) {
        client.update { it.copy(surname = surname) }
        if (surname.isBlank()) {
            uiErrors.update {
                it.copy(surnameError = FIELD_CANNOT_BE_EMPTY)
            }
        } else {
            uiErrors.update {
                it.copy(surnameError = null)
            }
        }
    }

    fun onPatronymicChanged(patronymic: String) {
        client.update { it.copy(patronymic = patronymic) }
        if (patronymic.isBlank()) {
            uiErrors.update {
                it.copy(patronymicError = FIELD_CANNOT_BE_EMPTY)
            }
        } else {
            uiErrors.update {
                it.copy(patronymicError = null)
            }
        }
    }

    fun onBirthdayChanged(birthday: String) {
        client.update { it.copy(birthday = birthday) }
        if (birthday.isBlank()) {
            uiErrors.update {
                it.copy(birthdayError = FIELD_CANNOT_BE_EMPTY)
            }
        } else {
            uiErrors.update {
                it.copy(birthdayError = null)
            }
            try {
                LocalDate.parse(birthday, dateFormatter)
                uiErrors.update {
                    it.copy(birthdayError = null)
                }
            } catch (e: Exception) {
                uiErrors.update {
                    it.copy(birthdayError = INVALID_DATE)
                }
            }
        }
    }

    fun onPassportSeriesChanged(passportSeries: String) {
        client.update { it.copy(passportSeries = passportSeries) }
        if (passportSeries.isBlank()) {
            uiErrors.update {
                it.copy(passportSeriesError = FIELD_CANNOT_BE_EMPTY)
            }
        } else {
            uiErrors.update {
                it.copy(passportSeriesError = null)
            }
        }
    }

    fun onPassportNumberChanged(passportNumber: String) {
        client.update { it.copy(passportNumber = passportNumber) }
        if (passportNumber.isBlank()) {
            uiErrors.update {
                it.copy(passportNumberError = FIELD_CANNOT_BE_EMPTY)
            }
        } else {
            uiErrors.update {
                it.copy(passportNumberError = null)
            }
        }
    }

    fun onPassportIssuedByChanged(passportIssuedBy: String) {
        client.update { it.copy(passportIssuedBy = passportIssuedBy) }
        if (passportIssuedBy.isBlank()) {
            uiErrors.update {
                it.copy(passportIssuedByError = FIELD_CANNOT_BE_EMPTY)
            }
        } else {
            uiErrors.update {
                it.copy(passportIssuedByError = null)
            }
        }
    }

    fun onPassportIssuedDateChanged(passportIssueDate: String) {
        client.update { it.copy(passportIssueDate = passportIssueDate) }
        if (passportIssueDate.isBlank()) {
            uiErrors.update {
                it.copy(passportIssueDateError = FIELD_CANNOT_BE_EMPTY)
            }
        } else {
            uiErrors.update {
                it.copy(passportIssueDateError = null)
            }
            try {
                LocalDate.parse(passportIssueDate, dateFormatter)
                uiErrors.update {
                    it.copy(passportIssueDateError = null)
                }
            } catch (e: Exception) {
                uiErrors.update {
                    it.copy(passportIssueDateError = INVALID_DATE)
                }
            }
        }
    }

    fun onIndentificationNumberChanged(identificationNumber: String) {
        client.update { it.copy(identificationNumber = identificationNumber) }
        if (identificationNumber.isBlank()) {
            uiErrors.update {
                it.copy(identificationNumberError = FIELD_CANNOT_BE_EMPTY)
            }
        } else {
            uiErrors.update {
                it.copy(identificationNumberError = null)
            }
            val regex = Regex("^[0-9]{7}[ABCKEMH]{1}[0-9]{3}(GB|PB|BA|BI)[0-9]{1}\$")
            if (regex.matches(identificationNumber)) {
                uiErrors.update {
                    it.copy(identificationNumberError = null)
                }
            } else {
                uiErrors.update {
                    it.copy(identificationNumberError = "Некорректный идентефикационный номер")
                }
            }
        }
    }

    fun onBirthPlaceChanged(birthPlace: String) {
        client.update { it.copy(birthPlace = birthPlace) }
        if (birthPlace.isBlank()) {
            uiErrors.update {
                it.copy(birthPlaceError = FIELD_CANNOT_BE_EMPTY)
            }
        } else {
            uiErrors.update {
                it.copy(birthPlaceError = null)
            }
        }
    }

    fun onCityChanged(city: City) {
        client.update { it.copy(city = city) }
        if (client.value.city == null) {
            uiErrors.update {
                it.copy(cityError = FIELD_CANNOT_BE_EMPTY)
            }
        } else {
            uiErrors.update {
                it.copy(cityError = null)
            }
        }
    }

    fun onAddressChanged(address: String) {
        client.update { it.copy(address = address) }
        if (address.isBlank()) {
            uiErrors.update {
                it.copy(addressError = FIELD_CANNOT_BE_EMPTY)
            }
        } else {
            uiErrors.update {
                it.copy(addressError = null)
            }
        }
    }

    fun onEmailChanged(email: String) {
        client.update { it.copy(email = email) }
        if (email.isBlank()) {
            uiErrors.update {
                it.copy(emailError = FIELD_CANNOT_BE_EMPTY)
            }
        } else {
            uiErrors.update {
                it.copy(emailError = null)
            }
            val regex = Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
            if (regex.matches(email)) {
                uiErrors.update {
                    it.copy(emailError = null)
                }
            } else {
                uiErrors.update {
                    it.copy(emailError = "Некорректный email")
                }
            }
        }
    }

    fun onWorkplaceChanged(workplace: String) {
        client.update { it.copy(workplace = workplace) }
        if (workplace.isBlank()) {
            uiErrors.update {
                it.copy(workplaceError = FIELD_CANNOT_BE_EMPTY)
            }
        } else {
            uiErrors.update {
                it.copy(workplaceError = null)
            }
        }
    }

    fun onPositionChanged(position: String) {
        client.update { it.copy(position = position) }
        if (position.isBlank()) {
            uiErrors.update {
                it.copy(positionError = FIELD_CANNOT_BE_EMPTY)
            }
        } else {
            uiErrors.update {
                it.copy(positionError = null)
            }
        }
    }

    fun onRegistrationCityChanged(registrationCity: City) {
        client.update { it.copy(registrationCity = registrationCity) }
        if (client.value.registrationCity == null) {
            uiErrors.update { it.copy(registrationCityError = FIELD_CANNOT_BE_EMPTY) }
        } else {
            uiErrors.update { it.copy(registrationCityError = null) }
        }
    }

    fun onMarriageStatusesChanged(marriageStatus: MarriageStatus) {
        client.update { it.copy(marriageStatus = marriageStatus) }
        if (client.value.marriageStatus == null) {
            uiErrors.update { it.copy(marriageStatusError = FIELD_CANNOT_BE_EMPTY) }
        } else {
            uiErrors.update { it.copy(marriageStatusError = null) }
        }
    }

    fun onCitizenshipChanged(citizenship: Citizenship) {
        client.update { it.copy(citizenship = citizenship) }
        if (client.value.citizenship == null) {
            uiErrors.update { it.copy(citizenshipError = FIELD_CANNOT_BE_EMPTY) }
        } else {
            uiErrors.update { it.copy(citizenshipError = null) }
        }
    }

    fun onDisabilitySelected(disability: Disability) {
        client.update { it.copy(disability = disability) }
        if (client.value.disability == null) {
            uiErrors.update { it.copy(disabilityError = FIELD_CANNOT_BE_EMPTY) }
        } else {
            uiErrors.update { it.copy(disabilityError = null) }
        }
    }

    fun onRetiredChanged(retired: Boolean) {
        client.update { it.copy(retired = retired) }
    }

    fun onIncomeChanged(income: String) {
        client.update { it.copy(income = income) }
        if (income.isBlank()) {
            uiErrors.update { it.copy(incomeError = FIELD_CANNOT_BE_EMPTY) }
        } else {
            uiErrors.update { it.copy(incomeError = null) }
        }
    }

    fun onSaveButtonClicked(): Boolean {
        return runBlocking<Boolean> {
            val clients = repository.getClients().first()
            val client = client.value
            val notExistingName = clients.all {
                it.name + it.surname + it.patronymic != client.name + client.surname + client.patronymic
            }
            val notExistingPassport = clients.all {
                it.passportNumber + it.passportSeries != client.passportNumber + client.passportSeries
            }
            val notExistingIdentificationNumber = clients.all {
                it.identificationNumber != client.identificationNumber
            }
            when {
                !notExistingName -> uiEvents.emit(UiEvent.Toast("Клиент с таким именем уже существует"))
                !notExistingPassport -> uiEvents.emit(UiEvent.Toast("Клиент с таким паспортом уже существует"))
                !notExistingIdentificationNumber -> uiEvents.emit(UiEvent.Toast("Клиент с таким идентификационным номером уже существует"))
                else -> {
                    if (clients.any { it.id == client.id }) {
                        repository.updateClient(client)
                    } else {
                        repository.insertClient(client)
                    }
                    uiEvents.emit(UiEvent.NavigateBack)
                    return@runBlocking true
                }
            }
            return@runBlocking false
        }
    }

    fun createDefaultClient(): Client {
        return Client(
            id = 0,
            name = "",
            surname = "",
            patronymic = "",
            birthday = "",
            passportSeries = "",
            passportNumber = "",
            passportIssuedBy = "",
            passportIssueDate = "",
            identificationNumber = "",
            birthPlace = "",
            city = null,
            address = "",
            homeNumber = null,
            mobileNumber = null,
            email = null,
            workplace = null,
            position = null,
            registrationCity = null,
            marriageStatus = null,
            citizenship = null,
            disability = null,
            retired = false,
            income = null
        )
    }

    fun deleteClient(deletedClient: Client?) = viewModelScope.launch {
        if (deletedClient != null) {
            repository.deleteClient(deletedClient)
        }
    }

    data class UiErrors(
        val nameError: String? = null,
        val surnameError: String? = null,
        val patronymicError: String? = null,
        val birthdayError: String? = null,
        val passportSeriesError: String? = null,
        val passportNumberError: String? = null,
        val passportIssuedByError: String? = null,
        val passportIssueDateError: String? = null,
        val identificationNumberError: String? = null,
        val birthPlaceError: String? = null,
        val cityError: String? = null,
        val addressError: String? = null,
        val homeNumberError: String? = null,
        val mobileNumberError: String? = null,
        val emailError: String? = null,
        val workplaceError: String? = null,
        val positionError: String? = null,
        val registrationCityError: String? = null,
        val marriageStatusError: String? = null,
        val citizenshipError: String? = null,
        val disabilityError: String? = null,
        val retiredError: String? = null,
        val incomeError: String? = null,
    )

    sealed interface UiEvent {
        data class Toast(val string: String) : UiEvent
        object NavigateBack : UiEvent
    }

    companion object {
        private const val FIELD_CANNOT_BE_EMPTY = "Поле не может быть пустым"
        private const val INVALID_DATE = "Введена некоррентная дата"
        private val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru", "RU"))
    }
}