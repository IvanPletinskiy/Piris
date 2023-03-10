package com.handen.piris

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.handen.piris.data.Account
import com.handen.piris.data.AppDatabase
import com.handen.piris.data.Citizenship
import com.handen.piris.data.City
import com.handen.piris.data.Client
import com.handen.piris.data.ClientRepository
import com.handen.piris.data.ClientRepositoryImpl
import com.handen.piris.data.Deposit
import com.handen.piris.data.DepositType
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

class MainViewModel() : ViewModel() {

    val context = MainActivity.context!!

    private val databaseCallback = object : RoomDatabase.Callback() {
        override fun onCreate(database: SupportSQLiteDatabase) {
            super.onCreate(database)
            viewModelScope.launch {
                defaultClients.forEach {
                    db.clientDao.insertClient(it)
                }
                val bankAccountNumber = 7327_0000_0000_00000.toString()
                val bankAccountBalance = 100_000_000_000.0
                val bankAccount =
                    Account(0, bankAccountNumber, "BYN", bankAccountBalance, clientId = -1)
                db.clientDao.insertAccount(bankAccount)
                insertFirstDeposit()
                insertSecondDeposit()
            }
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
        }

        override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
            super.onDestructiveMigration(db)
        }
    }

    private suspend fun insertFirstDeposit() {
        val firstMainAccountNumber = 3014_0000_0000_00001.toString()
        val firstMainAccount = Account(
            0, firstMainAccountNumber, "USD", 1000.0, 1
        )
        db.clientDao.insertAccount(firstMainAccount)
        val firstInterestAccountNumber = 3014_0000_0000_00002.toString()
        val firstInterestAccount = Account(
            0, firstInterestAccountNumber, "USD", 0.0, 1
        )
        db.clientDao.insertAccount(firstInterestAccount)
        val firstDeposit = Deposit(
            0,
            0.1f,
            "",
            "1000",
            2,
            3,
            type = DepositType.REVOCABLE,
            startDate = "08 ?????????????? 2023",
            endDate = "09 ???????????? 2023",
            agreementNumber = "1234",
            currencyCode = "USD",
            clientId = 1
        )
        db.clientDao.insertDeposit(firstDeposit)
    }

    private suspend fun insertSecondDeposit() {
        val firstMainAccountNumber = 3014_0000_0000_00003.toString()
        val firstMainAccount = Account(
            0, firstMainAccountNumber, "USD", 1000.0, 2
        )
        db.clientDao.insertAccount(firstMainAccount)
        val firstInterestAccountNumber = 3014_0000_0000_00004.toString()
        val firstInterestAccount = Account(
            0, firstInterestAccountNumber, "USD", 0.0, 2
        )
        db.clientDao.insertAccount(firstInterestAccount)
        val firstDeposit = Deposit(
            0,
            0.1f,
            "",
            "1000",
            4,
            5,
            type = DepositType.NON_REVOCABLE,
            startDate = "08 ?????????????? 2023",
            endDate = "09 ???????????? 2023",
            agreementNumber = "1234",
            currencyCode = "USD",
            clientId = 2
        )
        db.clientDao.insertDeposit(firstDeposit)
    }

    init {
        db = Room.databaseBuilder(context, AppDatabase::class.java, "piris_database")
            .addCallback(databaseCallback).build()
    }

    private val repository: ClientRepository = ClientRepositoryImpl(db.clientDao)

    val cities = listOf(
        City(0, "??????????????????"),
        City(1, "??????????"),
        City(2, "????????????????????"),
        City(3, "????????????"),
        City(4, "??????????")
    )
    val marriageStatuses = listOf(
        MarriageStatus(0, "???? ??????????/???? ??????????????"),
        MarriageStatus(1, "??????????/??????????????"),
        MarriageStatus(2, "????????????/??????????")
    )
    val disabilities = listOf(
        Disability(0, "??????"),
        Disability(1, "?????????????? 1 ????????????"),
        Disability(2, "?????????????? 2 ????????????"),
        Disability(3, "?????????????? 3 ????????????")
    )

    val citizenships = listOf(
        Citizenship(0, "??????????????????????"),
        Citizenship(1, "????????????????????"),
        Citizenship(2, "????????????????????"),
        Citizenship(3, "????????????????"),
    )

    val client = MutableStateFlow(createEmptyClient())

    val clients = repository.getClients()

    val uiErrors = MutableStateFlow(
        UiErrors()
    )

    val uiEvents = MutableSharedFlow<UiEvent>()

    fun setClient(client: Client) {
        this.client.value = client
        uiErrors.value = UiErrors()
    }

    fun onNameChanged(name: String) {
        client.value = client.value.copy(name = name.trim())
        validateName(name)
    }

    private fun validateName(name: String) {
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
        client.update { it.copy(surname = surname.trim()) }
        validateSurname(surname)
    }

    private fun validateSurname(surname: String) {
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
        client.update { it.copy(patronymic = patronymic.trim()) }
        validatePatronymic(patronymic)
    }

    private fun validatePatronymic(patronymic: String) {
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
        client.update { it.copy(birthday = birthday.trim()) }
        validateBirthday(birthday)
    }

    private fun validateBirthday(birthday: String) {
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
        client.update { it.copy(passportSeries = passportSeries.trim()) }
        validatePassportSeries(passportSeries)
    }

    private fun validatePassportSeries(passportSeries: String) {
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
        client.update { it.copy(passportNumber = passportNumber.trim()) }
        validatePassportNumber(passportNumber)
    }

    private fun validatePassportNumber(passportNumber: String) {
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
        client.update { it.copy(passportIssuedBy = passportIssuedBy.trim()) }
        validatePassportIssuedBy(passportIssuedBy)
    }

    private fun validatePassportIssuedBy(passportIssuedBy: String) {
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
        client.update { it.copy(passportIssueDate = passportIssueDate.trim()) }
        validatePassportIssueDate(passportIssueDate)
    }

    private fun validatePassportIssueDate(passportIssueDate: String) {
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
        client.update { it.copy(identificationNumber = identificationNumber.trim()) }
        validateIdentificationNumber(identificationNumber)
    }

    private fun validateIdentificationNumber(identificationNumber: String) {
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
                    it.copy(identificationNumberError = "???????????????????????? ?????????????????????????????????? ??????????")
                }
            }
        }
    }

    fun onBirthPlaceChanged(birthPlace: String) {
        client.update { it.copy(birthPlace = birthPlace.trim()) }
        validateBirthPlace(birthPlace)
    }

    private fun validateBirthPlace(birthPlace: String) {
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

    fun onCityChanged(city: City?) {
        client.update { it.copy(city = city) }
        validateCity()
    }

    private fun validateCity() {
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
        client.update { it.copy(address = address.trim()) }
        validateAddress(address)
    }

    private fun validateAddress(address: String) {
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

    fun onHomeNumberChanged(homeNumber: String?) {
        client.update { it.copy(homeNumber = homeNumber?.trim()) }
        validateHomeNumber(homeNumber)
    }

    private fun validateHomeNumber(homeNumber: String?) {
        val regex = Regex("^(\\+375|80)(29|25|44|33)(\\d{3})(\\d{2})(\\d{2})\$")
        if (homeNumber.isNullOrBlank() || regex.matches(homeNumber.orEmpty())) {
            uiErrors.update {
                it.copy(mobileNumberError = null)
            }
        } else {
            uiErrors.update {
                it.copy(mobileNumberError = "???????????????????????? ?????????? ????????????????")
            }
        }
    }

    fun onMobileNumberChanged(mobileNumber: String?) {
        client.update { it.copy(mobileNumber = mobileNumber?.trim()) }
        validateMobileNumber(mobileNumber)
    }

    private fun validateMobileNumber(mobileNumber: String?) {
        val regex = Regex("^(\\+375|80)(29|25|44|33)(\\d{3})(\\d{2})(\\d{2})\$")
        if (mobileNumber.isNullOrBlank() || regex.matches(mobileNumber.orEmpty())) {
            uiErrors.update {
                it.copy(mobileNumberError = null)
            }
        } else {
            uiErrors.update {
                it.copy(mobileNumberError = "???????????????????????? ?????????? ????????????????")
            }
        }
    }

    fun onEmailChanged(email: String?) {
        client.update { it.copy(email = email?.trim()) }
        validateEmail(email)
    }

    private fun validateEmail(email: String?) {
        if (email.isNullOrBlank()) {
            uiErrors.update {
                it.copy(emailError = null)
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
                    it.copy(emailError = "???????????????????????? email")
                }
            }
        }
    }

    fun onWorkplaceChanged(workplace: String?) {
        client.update { it.copy(workplace = workplace?.trim()) }
    }

    fun onPositionChanged(position: String?) {
        client.update { it.copy(position = position?.trim()) }
    }

    fun onRegistrationCityChanged(registrationCity: City?) {
        client.update { it.copy(registrationCity = registrationCity) }
        validateRegistrationCity()
    }

    private fun validateRegistrationCity() {
        if (client.value.registrationCity == null) {
            uiErrors.update { it.copy(registrationCityError = FIELD_CANNOT_BE_EMPTY) }
        } else {
            uiErrors.update { it.copy(registrationCityError = null) }
        }
    }

    fun onMarriageStatusesChanged(marriageStatus: MarriageStatus?) {
        client.update { it.copy(marriageStatus = marriageStatus) }
        validateMarriageStatus()
    }

    private fun validateMarriageStatus() {
        if (client.value.marriageStatus == null) {
            uiErrors.update { it.copy(marriageStatusError = FIELD_CANNOT_BE_EMPTY) }
        } else {
            uiErrors.update { it.copy(marriageStatusError = null) }
        }
    }

    fun onCitizenshipChanged(citizenship: Citizenship?) {
        client.update { it.copy(citizenship = citizenship) }
        validateCitizenship()
    }

    private fun validateCitizenship() {
        if (client.value.citizenship == null) {
            uiErrors.update { it.copy(citizenshipError = FIELD_CANNOT_BE_EMPTY) }
        } else {
            uiErrors.update { it.copy(citizenshipError = null) }
        }
    }

    fun onDisabilitySelected(disability: Disability?) {
        client.update { it.copy(disability = disability) }
        validateDisability()
    }

    private fun validateDisability() {
        if (client.value.disability == null) {
            uiErrors.update { it.copy(disabilityError = FIELD_CANNOT_BE_EMPTY) }
        } else {
            uiErrors.update { it.copy(disabilityError = null) }
        }
    }

    fun onRetiredChanged(retired: Boolean) {
        client.update { it.copy(retired = retired) }
    }

    fun onIncomeChanged(income: String?) {
        client.update { it.copy(income = income?.trim()) }
    }

    fun onSaveButtonClicked() {
        viewModelScope.launch {
            val clients = repository.getClients().first()
            val client = client.value
            val isEditing = clients.any { it.id == client.id }
            val notExistingName = clients.all {
                it.id != client.id && it.name + it.surname + it.patronymic != client.name + client.surname + client.patronymic
            }
            val notExistingPassport = clients.all {
                it.id != client.id && "${it.passportNumber}${it.passportSeries}" != "${client.passportNumber}${client.passportSeries}"
            }
            val notExistingIdentificationNumber = clients.all {
                it.id != client.id && it.identificationNumber != client.identificationNumber
            }
            when {
//                isEditing -> {
//                    if (checkNoErrors()) {
//                        if (clients.any { it.id == client.id }) {
//                            repository.updateClient(client)
//                        } else {
//                            repository.insertClient(client)
//                        }
//                        uiEvents.emit(UiEvent.NavigateBack)
//                    } else {
//                        uiEvents.emit(UiEvent.Toast("?????????????????? ???????????? ???????????????????? ??????????"))
//                    }
//                }
                !notExistingName -> uiEvents.emit(UiEvent.Toast("???????????? ?? ?????????? ???????????? ?????? ????????????????????"))
                !notExistingPassport -> uiEvents.emit(UiEvent.Toast("???????????? ?? ?????????? ?????????????????? ?????? ????????????????????"))
                !notExistingIdentificationNumber -> uiEvents.emit(UiEvent.Toast("???????????? ?? ?????????? ?????????????????????????????????? ?????????????? ?????? ????????????????????"))
                else -> {
                    if (checkNoErrors()) {
                        if (clients.any { it.id == client.id }) {
                            repository.updateClient(client)
                        } else {
                            repository.insertClient(client)
                        }
                        uiEvents.emit(UiEvent.NavigateBack)
                    } else {
                        uiEvents.emit(UiEvent.Toast("?????????????????? ???????????? ???????????????????? ??????????"))
                    }
                }
            }
        }
    }

    private fun checkNoErrors(): Boolean {
        val client = client.value
        onNameChanged(client.name)
        onSurnameChanged(client.surname)
        onPatronymicChanged(client.patronymic)
        onBirthdayChanged(client.birthday)
        onPassportSeriesChanged(client.passportSeries)
        onPassportNumberChanged(client.passportNumber)
        onPassportIssuedByChanged(client.passportIssuedBy)
        onPassportIssuedDateChanged(client.passportIssueDate)
        onIndentificationNumberChanged(client.identificationNumber)
        onBirthPlaceChanged(client.birthPlace)
        onCityChanged(client.city)
        onAddressChanged(client.address)
        onHomeNumberChanged(client.homeNumber)
        onMobileNumberChanged(client.mobileNumber)
        onEmailChanged(client.email)
        onWorkplaceChanged(client.workplace)
        onPositionChanged(client.position)
        onRegistrationCityChanged(client.registrationCity)
        onMarriageStatusesChanged(client.marriageStatus)
        onCitizenshipChanged(client.citizenship)
        onDisabilitySelected(client.disability)
        onRetiredChanged(client.retired)
        onIncomeChanged(client.income)
        val errors = uiErrors.value
        return with(errors) {
            nameError == null && surnameError == null && patronymicError == null && birthdayError == null && passportSeriesError == null && passportNumberError == null && passportIssuedByError == null && passportIssueDateError == null && identificationNumberError == null && birthPlaceError == null && cityError == null && addressError == null && homeNumberError == null && mobileNumberError == null && emailError == null && workplaceError == null && positionError == null && registrationCityError == null && marriageStatusError == null && citizenshipError == null && disabilityError == null && retiredError == null && incomeError == null
        }
    }

    fun createEmptyClient(): Client {
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
        const val FIELD_CANNOT_BE_EMPTY = "???????? ???? ?????????? ???????? ????????????"
        const val INVALID_DATE = "?????????????? ???????????????????????? ????????"
        val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("ru", "RU"))
        lateinit var db: AppDatabase
    }
}

private val defaultClients = listOf(
    Client(
        id = 0,
        name = "????????",
        surname = "????????????????????",
        patronymic = "????????????????????",
        birthday = "30 ?????????? 2002",
        passportSeries = "MC",
        passportNumber = "2720337",
        passportIssuedBy = "?????????????????????? ????????",
        passportIssueDate = "20 ?????????????? 2020",
        identificationNumber = "5300302B000PB2",
        birthPlace = "??????????????????",
        city = City(id = 0, "??????????????????"),
        address = "??. ??????????, ????-??. ????????????????????, ??. 42, ????. 67",
        homeNumber = null,
        mobileNumber = null,
        email = null,
        workplace = null,
        position = null,
        registrationCity = City(id = 1, "??????????"),
        marriageStatus = MarriageStatus(0, "???? ??????????"),
        citizenship = Citizenship(id = 0, "??????????????????????"),
        disability = Disability(id = 0, "??????"),
        retired = false,
        income = "100 BYN"
    ), Client(
        id = 0,
        name = "??????????????????",
        surname = "??????????",
        patronymic = "????????????????????",
        birthday = "28 ?????????? 2000",
        passportSeries = "MC",
        passportNumber = "2281337",
        passportIssuedBy = "???????? ?????????????????????????? ????????????",
        passportIssueDate = "20 ?????????????? 2020",
        identificationNumber = "1212302B000PB2",
        birthPlace = "??????????",
        city = City(id = 1, "??????????"),
        address = "??. ??????????, ????-??. ??????????????????????, ??. 42, ????. 67",
        homeNumber = null,
        mobileNumber = null,
        email = null,
        workplace = null,
        position = null,
        registrationCity = City(id = 1, "??????????"),
        marriageStatus = MarriageStatus(0, "???? ??????????"),
        citizenship = Citizenship(id = 0, "??????????????????????"),
        disability = Disability(id = 0, "??????"),
        retired = false,
        income = "100 BYN"
    ), Client(
        id = 0,
        name = "????????????",
        surname = "??????????????????",
        patronymic = "????????????????",
        birthday = "30 ?????????? 1997",
        passportSeries = "MK",
        passportNumber = "1234567",
        passportIssuedBy = "?????????????????????? ????????",
        passportIssueDate = "20 ?????????????? 2020",
        identificationNumber = "3452302B000PB2",
        birthPlace = "??????????????????",
        city = City(id = 0, "??????????????????"),
        address = "??. ??????????, ????-??. ????????????????????, ??. 42, ????. 67",
        homeNumber = null,
        mobileNumber = null,
        email = null,
        workplace = null,
        position = null,
        registrationCity = City(id = 1, "??????????"),
        marriageStatus = MarriageStatus(0, "???? ??????????"),
        citizenship = Citizenship(id = 0, "??????????????????????"),
        disability = Disability(id = 0, "??????"),
        retired = false,
        income = null
    ), Client(
        id = 0,
        name = "????????????????????",
        surname = "??????????????",
        patronymic = "????????????????",
        birthday = "30 ?????????? 1997",
        passportSeries = "MK",
        passportNumber = "7654321",
        passportIssuedBy = "?????????????????????? ????????",
        passportIssueDate = "20 ?????????????? 2020",
        identificationNumber = "6666602B000PB2",
        birthPlace = "??????????????????",
        city = City(id = 0, "??????????????????"),
        address = "??. ??????????, ????-??. ????????????????????, ??. 42, ????. 67",
        homeNumber = null,
        mobileNumber = null,
        email = null,
        workplace = null,
        position = null,
        registrationCity = City(id = 1, "??????????"),
        marriageStatus = MarriageStatus(0, "???? ??????????"),
        citizenship = Citizenship(id = 0, "??????????????????????"),
        disability = Disability(id = 0, "??????"),
        retired = false,
        income = null
    ), Client(
        id = 0,
        name = "??????????????",
        surname = "????????????",
        patronymic = "????????????????????",
        birthday = "30 ?????????? 2002",
        passportSeries = "MC",
        passportNumber = "3453451",
        passportIssuedBy = "?????????????????????? ????????",
        passportIssueDate = "20 ?????????????? 2020",
        identificationNumber = "7777702B000PB2",
        birthPlace = "??????????????????",
        city = City(id = 0, "??????????????????"),
        address = "??. ??????????, ????-??. ????????????????????, ??. 42, ????. 67",
        homeNumber = null,
        mobileNumber = null,
        email = null,
        workplace = null,
        position = null,
        registrationCity = City(id = 1, "??????????"),
        marriageStatus = MarriageStatus(0, "???? ??????????"),
        citizenship = Citizenship(id = 0, "??????????????????????"),
        disability = Disability(id = 0, "??????"),
        retired = false,
        income = null
    )
)