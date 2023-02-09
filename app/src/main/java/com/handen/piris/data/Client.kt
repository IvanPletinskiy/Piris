package com.handen.piris.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "clients")
data class Client(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String = "",
    val surname: String = "",
    val patronymic: String ="",
    val birthday: String = "",
    val passportSeries: String = "",
    val passportNumber: String = "",
    val passportIssuedBy: String = "",
    val passportIssueDate: String = "",
    val identificationNumber: String = "",
    val birthPlace: String = "",
    @Embedded(prefix = "city_")
    val city: City? = null,
    val address: String = "",
    val homeNumber: String? = null,
    val mobileNumber: String? = "",
    val email: String? = null,
    val workplace: String? = null,
    val position: String? = null,
    @Embedded(prefix = "registration_city_")
    val registrationCity: City? = null,
    @Embedded(prefix = "marriage_status_")
    val marriageStatus: MarriageStatus? = null,
    @Embedded(prefix = "citizenship_")
    val citizenship: Citizenship? = null,
    @Embedded(prefix = "disability_")
    val disability: Disability? = null,
    val retired: Boolean = false,
    val income: String? = null
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

@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val number: String,
    val currency: String,
//    val currencyCode: String, // USD 840, EUR 978, BYN 933
    val balance: Double,
    val clientId: Int = 0
)

@Entity(tableName = "deposits")
data class Deposit(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val yield: Float = 0f,
    val yieldString: String = "",
    val amount: String = "",
    var sourceAccountId: Int = 0,
    var yieldAccountId: Int = 0,
    val type: DepositType = DepositType.REVOCABLE,
    val startDate: String = "",
    val endDate: String = "",
    val agreementNumber: String = "",
    val currencyCode: String = "",
    val clientId: Int = 0
)

enum class DepositType(val displayName: String) {
    REVOCABLE("Отзывный"), NON_REVOCABLE("Безотзывный")
}
