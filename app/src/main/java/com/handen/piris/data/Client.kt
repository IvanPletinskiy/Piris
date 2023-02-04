package com.handen.piris.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class Client(
    @PrimaryKey val id: Int,
    val name: String,
    val surname: String,
    val patronymic: String,
    val birthday: String,
    val passportSeries: String,
    val passportNumber: String,
    val passportIssuedBy: String,
    val passportIssueDate: String,
    val identificationNumber: String,
    val birthPlace: String,
    @Embedded
    val city: City?,
    val address: String,
    val homeNumber: String?,
    val mobileNumber: String?,
    val email: String?,
    val workplace: String?,
    val position: String?,
    @Embedded
    val registrationCity: City?,
    @Embedded
    val marriageStatus: MarriageStatus?,
    @Embedded
    val citizenship: Citizenship?,
    @Embedded
    val disability: Disability?,
    val retired: Boolean,
    val income: String?
)

@Entity(tableName = "cities")
data class City(
    @PrimaryKey val id: Int,
    val name: String
)

@Entity(tableName = "marriage_statuses")
data class MarriageStatus(
    @PrimaryKey val id: Int,
    val name: String
)

@Entity(tableName = "citizenships")
data class Citizenship(
    @PrimaryKey val id: Int,
    val name: String
)

@Entity(tableName = "disabilities")
data class Disability(
    @PrimaryKey val id: Int,
    val name: String
)
