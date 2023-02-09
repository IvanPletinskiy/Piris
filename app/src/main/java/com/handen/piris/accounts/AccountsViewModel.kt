package com.handen.piris.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handen.piris.MainViewModel
import com.handen.piris.data.ClientRepository
import com.handen.piris.data.ClientRepositoryImpl
import com.handen.piris.data.DepositType
import java.text.DecimalFormat
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AccountsViewModel : ViewModel() {

    private val repository: ClientRepository = ClientRepositoryImpl(MainViewModel.db.clientDao)
    val accountsUi = MutableStateFlow<List<UiAccount>>(emptyList())

    val currentDate = MutableStateFlow(LocalDate.now())

    init {
        viewModelScope.launch {
            repository.getAccounts().collect() {
                val clients = repository.getClients().first()
                accountsUi.value = it.map { account ->
                    val client = clients.firstOrNull { it.id == account.clientId }
                    val owner = if (client != null ) {
                        "${client.name} ${client.surname} ${client.patronymic}"
                    } else {
                        "Технобанк"
                    }
                    val format = DecimalFormat("#.##")

                    val amount = "${format.format(account.balance)} ${account.currency}"
                    UiAccount(account.id, account.number, owner, amount)
                }
            }
        }
    }

    fun skip30Days() = viewModelScope.launch {
        val oldDate = currentDate.value
        val newDate = oldDate.plusDays(30)
        currentDate.value = newDate

        val deposits = repository.getDeposits()
        deposits.forEach { deposit ->
            if (deposit.type == DepositType.NON_REVOCABLE) {
                val endDate = LocalDate.from(MainViewModel.dateFormatter.parse(deposit.endDate)).minusDays(1)
                if (endDate.isAfter(oldDate) && endDate.isBefore(newDate)) {
                    val startDate = LocalDate.from(MainViewModel.dateFormatter.parse(deposit.startDate))
                    val monthCount = endDate.monthValue - startDate.monthValue
                    val interest = deposit.amount.toDouble() * deposit.yield / 12 * monthCount
                    repository.updateBalance(deposit.yieldAccountId, interest)
                }
            } else {
                val endDate = LocalDate.from(MainViewModel.dateFormatter.parse(deposit.endDate)).plusDays(1)
                if (newDate.isBefore(endDate)) {
                    val interest = deposit.amount.toDouble() * deposit.yield / 12
                    val accounts = repository.getAccounts().first()
                    val currentBalance = accounts.first { it.id == deposit.yieldAccountId }.balance
                    val newBalance = currentBalance + interest
                    repository.updateBalance(deposit.yieldAccountId, newBalance)
                }
            }
        }
    }

    data class UiAccount(val id: Int, val number: String, val owner: String, val amount: String)
}