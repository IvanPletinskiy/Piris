package com.handen.piris.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {

    @Query("SELECT * FROM clients")
    fun getClients(): Flow<List<Client>>

    @Update(entity = Client::class, onConflict = REPLACE)
    suspend fun updateClient(client: Client)

    @Insert(entity = Client::class)
    suspend fun insertClient(client: Client)

    @Delete(entity = Client::class)
    suspend fun deleteClient(client: Client)

    @Query("SELECT * FROM accounts")
    fun getAccounts(): Flow<List<Account>>

    @Insert
    suspend fun insertAccount(account: Account): Long

    @Insert
    suspend fun insertDeposit(deposit: Deposit)

    @Query("SELECT * FROM deposits")
    suspend fun getDeposits(): List<Deposit>

    @Query("UPDATE accounts SET balance=:balance WHERE id = :accountId")
    suspend fun updateBalance(accountId: Int, balance: Double)
}