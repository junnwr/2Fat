package com.junnwr.fat.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "totp_accounts")
data class TotpAccount(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val issuer: String,
    val accountName: String,
    val secret: String
)
