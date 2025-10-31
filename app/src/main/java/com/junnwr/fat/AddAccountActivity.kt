package com.junnwr.fat

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.zxing.integration.android.IntentIntegrator
import com.junnwr.fat.databinding.ActivityAddAccountBinding
import com.junnwr.fat.db.AppDatabase
import com.junnwr.fat.db.TotpAccount
import kotlinx.coroutines.launch

class AddAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAccountBinding
    private lateinit var database: AppDatabase

    // ActivityResultLauncher para permissão de câmera (moderno)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startQrScanner()
        } else {
            Toast.makeText(this, "Permissão de câmera necessária", Toast.LENGTH_SHORT).show()
        }
    }

    // ActivityResultLauncher para QR Scanner (moderno)
    private val qrScanLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val contents = IntentIntegrator.parseActivityResult(result.resultCode, result.data)?.contents
        if (contents != null) {
            parseQrCode(contents)
        } else {
            Toast.makeText(this, "Scan cancelado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Adicionar Conta"

        database = AppDatabase.getDatabase(this)

        setupButtons()
    }

    private fun setupButtons() {
        binding.btnScanQr.setOnClickListener {
            if (checkCameraPermission()) {
                startQrScanner()
            } else {
                requestCameraPermission()
            }
        }

        binding.btnAddManual.setOnClickListener {
            addAccountManually()
        }
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun startQrScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Escaneie o QR Code")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(false)
        integrator.setOrientationLocked(true)
        qrScanLauncher.launch(integrator.createScanIntent())
    }

    private fun parseQrCode(content: String) {
        try {
            // Formato: otpauth://totp/issuer:account?secret=SECRET&issuer=ISSUER
            val uri = Uri.parse(content)

            if (uri.scheme != "otpauth" || uri.host != "totp") {
                Toast.makeText(this, "QR Code inválido", Toast.LENGTH_SHORT).show()
                return
            }

            val path = uri.path?.removePrefix("/") ?: ""
            val parts = path.split(":")
            val issuer = if (parts.size > 1) parts[0] else uri.getQueryParameter("issuer") ?: "Unknown"
            val accountName = if (parts.size > 1) parts[1] else parts[0]
            val secret = uri.getQueryParameter("secret") ?: ""

            if (secret.isEmpty()) {
                Toast.makeText(this, "Secret não encontrado no QR Code", Toast.LENGTH_SHORT).show()
                return
            }

            saveAccount(issuer, accountName, secret)

        } catch (e: Exception) {
            Toast.makeText(this, "Erro ao processar QR Code: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addAccountManually() {
        val issuer = binding.etIssuer.text.toString().trim()
        val accountName = binding.etAccountName.text.toString().trim()
        val secret = binding.etSecret.text.toString().trim().replace(" ", "")

        if (issuer.isEmpty() || accountName.isEmpty() || secret.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        saveAccount(issuer, accountName, secret)
    }

    private fun saveAccount(issuer: String, accountName: String, secret: String) {
        val account = TotpAccount(
            issuer = issuer,
            accountName = accountName,
            secret = secret
        )

        lifecycleScope.launch {
            try {
                database.accountDao().insert(account)
                Toast.makeText(this@AddAccountActivity, "Conta adicionada!", Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@AddAccountActivity, "Erro ao salvar: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
