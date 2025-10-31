package com.junnwr.fat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.junnwr.fat.databinding.ItemAccountBinding
import com.junnwr.fat.db.TotpAccount
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordConfig
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordGenerator
import org.apache.commons.codec.binary.Base32
import java.util.concurrent.TimeUnit

class AccountAdapter(
    private val accounts: List<TotpAccount>,
    private val onCopyClick: (String) -> Unit,
    private val onDeleteClick: (TotpAccount) -> Unit
) : RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

    inner class AccountViewHolder(val binding: ItemAccountBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val binding = ItemAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val account = accounts[position]

        holder.binding.tvIssuer.text = account.issuer
        holder.binding.tvAccountName.text = account.accountName

        try {
            val code = generateTotpCode(account.secret)
            val timeLeft = getTimeLeft()

            holder.binding.tvCode.text = formatCode(code)
            holder.binding.progressBar.max = 30
            holder.binding.progressBar.progress = timeLeft
            holder.binding.tvTimeLeft.text = "${timeLeft}s"

        } catch (e: Exception) {
            holder.binding.tvCode.text = "ERRO"
            holder.binding.tvTimeLeft.text = "0s"
            e.printStackTrace()
        }

        holder.binding.root.setOnClickListener {
            try {
                val code = generateTotpCode(account.secret)
                onCopyClick(code)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        holder.binding.root.setOnLongClickListener {
            onDeleteClick(account)
            true
        }
    }

    override fun getItemCount(): Int = accounts.size

    private fun generateTotpCode(secret: String): String {
        val base32 = Base32()
        val secretBytes = base32.decode(secret.uppercase())

        val config = TimeBasedOneTimePasswordConfig(
            codeDigits = 6,
            hmacAlgorithm = dev.turingcomplete.kotlinonetimepassword.HmacAlgorithm.SHA1,
            timeStep = 30,
            timeStepUnit = TimeUnit.SECONDS
        )

        val generator = TimeBasedOneTimePasswordGenerator(secretBytes, config)
        return generator.generate()
    }

    private fun getTimeLeft(): Int {
        val currentTime = System.currentTimeMillis() / 1000
        return (30 - (currentTime % 30)).toInt()
    }

    private fun formatCode(code: String): String {
        return if (code.length == 6) {
            "${code.substring(0, 3)} ${code.substring(3)}"
        } else {
            code
        }
    }
}
