package com.handen.piris.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Client::class, City::class, MarriageStatus::class, Citizenship::class, Disability::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract val clientDao: ClientDao
}