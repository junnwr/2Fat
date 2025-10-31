package com.junnwr.fat.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Query("SELECT * FROM totp_accounts ORDER BY issuer ASC")
    fun getAllAccounts(): Flow<List<TotpAccount>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: TotpAccount)

    @Delete
    suspend fun delete(account: TotpAccount)

    @Query("DELETE FROM totp_accounts")
    suspend fun deleteAll()
}
