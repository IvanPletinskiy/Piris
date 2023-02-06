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
}