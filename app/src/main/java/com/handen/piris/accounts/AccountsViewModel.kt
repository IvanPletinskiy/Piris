package com.handen.piris.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handen.piris.MainViewModel
import com.handen.piris.data.ClientRepository
import com.handen.piris.data.ClientRepositoryImpl
import com.handen.piris.data.DepositType
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
                    val client = clients.first { it.id == account.id }
                    val owner = "${client.name} ${client.surname} ${client.patronymic}"
                    val amount = "${account.balance} ${account.currency}"
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
            val endDate = MainViewModel.dateFormatter.parse(deposit.endDate) as LocalDate
            if (deposit.type == DepositType.NON_REVOCABLE) {
                if (endDate.isAfter(oldDate) && endDate.isBefore(newDate)) {
                    val interest = deposit.amount.toDouble() * deposit.yield / 12
                    repository.updateBalance(deposit.yieldAccountId, interest)
                }
            } else {
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