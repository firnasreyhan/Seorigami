package com.project.seorigami.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.seorigami.R
import com.project.seorigami.databinding.ActivityStatusPaymentBinding
import com.project.seorigami.model.response.TransactionDataModel
import com.project.seorigami.util.KeyIntent
import com.project.seorigami.util.Prefs
import com.project.seorigami.util.Utils

class StatusPaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStatusPaymentBinding
    private lateinit var transactionData: TransactionDataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatusPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transactionData = intent.getParcelableExtra(KeyIntent.TRANSACTION_DATA.name)!!

        binding.textViewTotal.text = "Rp ${Utils.changePrice(transactionData.total.toString())}"
        binding.textViewFaktur.text = transactionData.faktur
        binding.textViewTanggal.text = transactionData.tanggal.split(".")[0].replace("T", ", ")
        binding.textViewNamaPengirim.text = Prefs(this).user?.name.toString()

        binding.button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}