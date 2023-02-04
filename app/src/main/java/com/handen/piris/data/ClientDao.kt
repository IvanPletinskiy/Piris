package com.handen.piris.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {

    @Query("SELECT * FROM clients WHERE id = :id")
    suspend fun getClient(id: Int): Client

    @Query("SELECT * FROM clients")
    fun getClients(): Flow<List<Client>>

    @Update
    suspend fun updateClient(client: Client)

    @Insert
    suspend fun insertClient(client: Client)

    @Delete
    suspend fun deleteClient(client: Client)
}