package com.handen.piris.createdeposit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handen.piris.MainViewModel
import com.handen.piris.data.Account
import com.handen.piris.data.Client
import com.handen.piris.data.ClientRepository
import com.handen.piris.data.ClientRepositoryImpl
import com.handen.piris.data.Deposit
import com.handen.piris.data.DepositType
import java.time.LocalDate
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateDepositViewModel : ViewModel() {

    val deposit = MutableStateFlow(Deposit(id = 0))

    val client = MutableStateFlow<Client?>(null)

    private val repository: ClientRepository = ClientRepositoryImpl(MainViewModel.db.clientDao)

    val uiErrors = MutableStateFlow(
        CreateDepositErrors()
    )

    val currencies = listOf(
        Currency("USD", "840"), Currency("EUR", "978"), Currency("BYN", "933")
    )

    fun onClientChanged(client: Client?) {
        this.client.value = client
        deposit.update { it.copy(clientId = client?.id ?: 0) }
        if (this.client.value?.id != null && this.client.value?.id != 0) {
            uiErrors.update { it.copy(clientError = null) }
        } else {
            uiErrors.update { it.copy(clientError = "Необходимо выбрать клиента") }
        }
    }

    fun onAgreementNumberChanged(number: String) {
        deposit.update { it.copy(agreementNumber = number) }
        if (number.isNullOrBlank()) {
            uiErrors.update { it.copy(agreementError = "Необходимо указать номер соглашения") }
        } else {
            uiErrors.update { it.copy(agreementError = null) }
        }
    }

    fun onAmountChanged(amount: String) {
        deposit.update { it.copy(amount = amount) }
        if (amount.isNullOrBlank()) {
            uiErrors.update { it.copy(amountError = "Необходимо указать сумму") }
        } else {
            uiErrors.update { it.copy(amountError = null) }
        }
    }

    fun onYieldChanged(yield: String) {
        if (yield.isNotBlank()) {
            deposit.update { it.copy(yieldString = yield) }
            try {
                deposit.update { it.copy(yield = (yield.toInt().toFloat() / 100)) }
            } catch (e: Exception) {
                uiErrors.update { it.copy(yieldError = "Введите положительное число") }
            }
        }
        if (deposit.value.yield <= 0) {
            uiErrors.update { it.copy(yieldError = "Процент должен быть положительным") }
        } else {
            uiErrors.update { it.copy(yieldError = null) }
        }
    }

    fun onCurrencyChanged(currency: String) {
        deposit.update { it.copy(currencyCode = currency) }
        if (deposit.value.currencyCode.isBlank()) {
            uiErrors.update { it.copy(currencyError = "Необходимо указать код валюты") }
        } else {
            uiErrors.update { it.copy(currencyError = null) }
        }
    }

    fun onStartDateChanged(startDate: String) {
        deposit.update { it.copy(startDate = startDate.trim()) }
        if (startDate.isBlank()) {
            uiErrors.update {
                it.copy(startDateError = MainViewModel.FIELD_CANNOT_BE_EMPTY)
            }
        } else {
            uiErrors.update {
                it.copy(startDateError = null)
            }
            try {
                LocalDate.parse(startDate, MainViewModel.dateFormatter)
                uiErrors.update {
                    it.copy(startDateError = null)
                }
            } catch (e: Exception) {
                uiErrors.update {
                    it.copy(startDateError = MainViewModel.INVALID_DATE)
                }
            }
        }
    }

    fun onEndDateChanged(endDate: String) {
        deposit.update { it.copy(endDate = endDate.trim()) }
        if (endDate.isBlank()) {
            uiErrors.update {
                it.copy(endDateError = MainViewModel.FIELD_CANNOT_BE_EMPTY)
            }
        } else {
            uiErrors.update {
                it.copy(endDateError = null)
            }
            try {
                LocalDate.parse(endDate, MainViewModel.dateFormatter)
                uiErrors.update {
                    it.copy(endDateError = null)
                }
            } catch (e: Exception) {
                uiErrors.update {
                    it.copy(endDateError = MainViewModel.INVALID_DATE)
                }
            }
        }
    }

    fun onDepositTypeChanged(depositType: DepositType) {
        deposit.update { it.copy(type = depositType) }
    }

    fun onSaveButtonClicked(mainViewModel: MainViewModel): Deferred<Boolean> = viewModelScope.async {
        if (checkNoErrors()) {
            val deposit = deposit.value
            val totalAccounts = repository.getAccounts().first().size
            val mainAccountNumber = 3014_0000_0000_00000 + totalAccounts
            val mainAccount = Account(
                0,
                mainAccountNumber.toString(),
                deposit.currencyCode,
                deposit.amount.toDouble(),
                deposit.clientId
            )
            val mainAccountId = repository.insertAccount(mainAccount)
            val interestAccountNumber = 3014_0000_0000_00001 + totalAccounts
            val interestAccount = Account(
                0, interestAccountNumber.toString(), deposit.currencyCode, 0.0, deposit.clientId
            )
            val interestAccountId = repository.insertAccount(interestAccount)
            deposit.sourceAccountId = mainAccountId
            deposit.yieldAccountId = interestAccountId
            repository.insertDeposit(deposit)
            true
        } else {
            false
        }
    }

    private fun checkNoErrors(): Boolean {
        val deposit = deposit.value
        onClientChanged(client.value)
        onAmountChanged(deposit.amount)
        onAgreementNumberChanged(deposit.agreementNumber)
        onYieldChanged((deposit.yield * 100).toInt().toString())
        onCurrencyChanged(deposit.currencyCode)
        onStartDateChanged(deposit.startDate)
        onEndDateChanged(deposit.endDate)
        onDepositTypeChanged(deposit.type)
        val errors = uiErrors.value
        return with(errors) {
            clientError == null && agreementError == null && yieldError == null && currencyError == null && startDateError == null && endDateError == null && depositTypeError == null
        }
    }

    data class CreateDepositErrors(
        val clientError: String? = null,
        val agreementError: String? = null,
        val yieldError: String? = null,
        val startDateError: String? = null,
        val endDateError: String? = null,
        val currencyError: String? = null,
        val depositTypeError: String? = null,
        val amountError: String? = null
    )

    data class Currency(val name: String, val code: String)
}