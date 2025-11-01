package com.junnwr.fat.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import java.io.File
import java.security.SecureRandom

@Database(entities = [TotpAccount::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                // Verificar e deletar banco corrompido/não criptografado
                // deleteDatabaseIfCorrupted(context) <-- linha removida devido a exclusao dos dados

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "2fat_database"
                )
                    .openHelperFactory(SupportFactory(getPassphrase(context)))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private fun deleteDatabaseIfCorrupted(context: Context) {
            try {
                val dbFile = context.getDatabasePath("2fat_database")
                if (dbFile.exists()) {
                    // Tentar detectar se é um banco não criptografado
                    val walFile = File(dbFile.parent, "2fat_database-wal")
                    val shmFile = File(dbFile.parent, "2fat_database-shm")

                    // Se o arquivo existe mas não tem uma passphrase válida armazenada,
                    // provavelmente é um banco antigo não criptografado
                    val masterKey = MasterKey.Builder(context)
                        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                        .build()

                    val sharedPreferences = EncryptedSharedPreferences.create(
                        context,
                        "2fat_secure_prefs",
                        masterKey,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                    )

                    val hasPassphrase = sharedPreferences.contains("db_passphrase")

                    // Se já tinha passphrase mas ainda dá erro, deletar tudo
                    if (hasPassphrase) {
                        dbFile.delete()
                        walFile.delete()
                        shmFile.delete()
                        // Limpar a passphrase antiga também
                        sharedPreferences.edit().remove("db_passphrase").apply()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Em caso de erro, tentar deletar o banco
                try {
                    context.deleteDatabase("2fat_database")
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }

        private fun getPassphrase(context: Context): ByteArray {
            // Criar MasterKey para criptografia das preferências
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            // Criar SharedPreferences criptografadas
            val sharedPreferences = EncryptedSharedPreferences.create(
                context,
                "2fat_secure_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            // Buscar ou gerar passphrase
            var passphrase = sharedPreferences.getString("db_passphrase", null)

            if (passphrase == null) {
                // Gerar nova passphrase segura
                passphrase = generateSecurePassphrase()
                sharedPreferences.edit().putString("db_passphrase", passphrase).apply()
            }

            // Converter para ByteArray do SQLCipher
            return SQLiteDatabase.getBytes(passphrase.toCharArray())
        }

        private fun generateSecurePassphrase(): String {
            val random = SecureRandom()
            val bytes = ByteArray(32)
            random.nextBytes(bytes)
            return bytes.joinToString("") { "%02x".format(it) }
        }
    }
}
