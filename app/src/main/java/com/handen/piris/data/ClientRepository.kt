package com.handen.piris.data

import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface ClientRepository {
//    suspend fun getClient(id: Int): Client

    fun getClients(): Flow<List<Client>>

    suspend fun updateClient(client: Client)

    suspend fun insertClient(client: Client)

    suspend fun deleteClient(client: Client)

    fun getAccounts(): Flow<List<Account>>

    suspend fun insertAccount(account: Account): Int

    suspend fun insertDeposit(deposit: Deposit)

    suspend fun getDeposits(): List<Deposit>

    suspend fun updateBalance(accountId: Int, balance: Double)
}

class ClientRepositoryImpl(private val clientDao: ClientDao) : ClientRepository {
//    override suspend fun getClient(id: Int): Client {
//        return clientDao.getClient(id)
//    }

    override fun getClients(): Flow<List<Client>> {
        return clientDao.getClients()
    }

    override suspend fun updateClient(client: Client) {
        return clientDao.updateClient(client)
    }

    override suspend fun insertClient(client: Client) {
        return clientDao.insertClient(client)
    }

    override suspend fun deleteClient(client: Client) {
        return clientDao.deleteClient(client)
    }

    override fun getAccounts(): Flow<List<Account>> {
        return clientDao.getAccounts()
    }

    override suspend fun insertAccount(account: Account): Int {
        return clientDao.insertAccount(account).toInt()
    }

    override suspend fun insertDeposit(deposit: Deposit) {
        clientDao.insertDeposit(deposit)
    }

    override suspend fun getDeposits(): List<Deposit> {
        return clientDao.getDeposits()
    }

    override suspend fun updateBalance(accountId: Int, balance: Double) {
        clientDao.updateBalance(accountId, balance)
    }
}