package com.junnwr.fat

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.junnwr.fat.databinding.ActivityMainBinding
import com.junnwr.fat.db.AppDatabase
import com.junnwr.fat.db.TotpAccount
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: AppDatabase
    private lateinit var adapter: AccountAdapter
    private val accounts = mutableListOf<TotpAccount>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Configurar toolbar
            setSupportActionBar(binding.toolbar)

            // Inicializar database
            database = AppDatabase.getDatabase(this)

            setupRecyclerView()
            setupFab()
            loadAccounts()
            startTotpUpdater()

        } catch (e: Exception) {
            Toast.makeText(this, "Erro ao iniciar: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun setupRecyclerView() {
        adapter = AccountAdapter(
            accounts = accounts,
            onCopyClick = { code ->
                copyToClipboard(code)
            },
            onDeleteClick = { account ->
                showDeleteDialog(account)
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddAccountActivity::class.java))
        }
    }

    private fun loadAccounts() {
        lifecycleScope.launch {
            try {
                database.accountDao().getAllAccounts().collect { accountList ->
                    accounts.clear()
                    accounts.addAll(accountList)
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Erro ao carregar contas: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }

    private fun startTotpUpdater() {
        lifecycleScope.launch {
            while (true) {
                delay(1000) // Atualiza a cada segundo
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun copyToClipboard(code: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("TOTP Code", code)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Código copiado!", Toast.LENGTH_SHORT).show()
    }

    private fun showDeleteDialog(account: TotpAccount) {
        AlertDialog.Builder(this)
            .setTitle("Deletar conta")
            .setMessage("Deseja deletar ${account.issuer}?")
            .setPositiveButton("Deletar") { _, _ ->
                lifecycleScope.launch {
                    database.accountDao().delete(account)
                    Toast.makeText(this@MainActivity, "Conta deletada", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
