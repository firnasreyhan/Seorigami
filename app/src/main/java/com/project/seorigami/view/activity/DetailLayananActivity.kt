package com.project.seorigami.view.activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.project.seorigami.R
import com.project.seorigami.adapter.BahanJasaAdapter
import com.project.seorigami.databinding.ActivityDetailLayananBinding
import com.project.seorigami.model.response.BahanJasaDataModel
import com.project.seorigami.model.response.MitraDataModel
import com.project.seorigami.util.ItemClickListener
import com.project.seorigami.util.KeyIntent
import com.project.seorigami.util.Prefs
import com.project.seorigami.util.State
import com.project.seorigami.util.Utils
import com.project.seorigami.viewmodel.DetailLayananViewModel
import com.project.seorigami.viewmodel.HomeViewModel

class DetailLayananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailLayananBinding
    private lateinit var  viewModel: DetailLayananViewModel
    private lateinit var dialog: ProgressDialog
    private var bahanAdapter = BahanJasaAdapter()
    private var jasaAdapter = BahanJasaAdapter()
    private var pinpoint: String = ""
    private var mitraName: String = ""
    private var mitraId: Int = 0
    private var userMitraId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLayananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mitraId = intent.getIntExtra(KeyIntent.MITRA_ID.name, 0)
        dialog = ProgressDialog(this)
        viewModel = ViewModelProvider(this)[DetailLayananViewModel::class.java]

        binding.recyclerViewBahan.apply {
            layoutManager = LinearLayoutManager(this@DetailLayananActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = bahanAdapter
        }
        binding.recyclerViewJasa.apply {
            layoutManager = LinearLayoutManager(this@DetailLayananActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = jasaAdapter
        }

        viewModel.layanan(this, mitraId)
        viewModel.dataLayanan.observe(this) {
            userMitraId = it.user_id
            mitraName = it.nama
            binding.textViewTitle.text = it.nama
            binding.textViewNama.text = it.nama
            binding.textViewKota.text = it.kota
            binding.textViewDeskripsi.text = it.deskripsi
            pinpoint = it.pinpoint

            Glide.with(this)
//                .load(Utils.reformatImageUrl(it.foto))
                .load(it.foto)
                .placeholder(R.drawable.ic_logo_seorigami)
                .error(R.drawable.ic_logo_seorigami)
                .into(binding.imageViewLayanan)

            bahanAdapter.data.clear()
            jasaAdapter.data.clear()
            bahanAdapter.data.addAll(it.bahan)
            jasaAdapter.data.addAll(it.jasa)
            bahanAdapter.notifyDataSetChanged()
            jasaAdapter.notifyDataSetChanged()
        }

        viewModel.stateLayanan.observe(this) {
            when (it) {
                State.COMPLETE -> {
                    dialog.dismiss()
                }

                State.LOADING -> {
                    showProgressDialog()
                }

                State.ERROR -> {
                    dialog.dismiss()
                }

                else -> {
                    dialog.dismiss()
                    Prefs(this).jwt = null
                    Prefs(this).user = null
                    val intent = Intent(this, SignInActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    Toast.makeText(this, "Silahkan login kembali", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.errorMessage.observe(this) {
            if (!it.isNullOrEmpty()) {
                showAlertDialog(it.toString())
            }
        }

        binding.buttonChat.setOnClickListener {
            val intent = Intent(this, ChatWindowActivity::class.java)
            intent.putExtra(KeyIntent.MITRA_ID.name, userMitraId)
            intent.putExtra(KeyIntent.MITRA_NAME.name, mitraName)
            startActivity(intent)
        }

        binding.imageViewMaps.setOnClickListener {
            if (pinpoint.isNotEmpty()) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://maps.google.com/?q=${pinpoint}")
                )
                startActivity(intent)
            }
        }

        binding.imageViewBack.setOnClickListener {
            finish()
        }

        binding.button.setOnClickListener {
            val cartData = ArrayList<BahanJasaDataModel>()
            cartData.addAll(bahanAdapter.selectedData)
            cartData.addAll(jasaAdapter.selectedData)
            
            if (cartData.isNullOrEmpty()) {
                Toast.makeText(this, "Mohon pilih layanan yang tersedia", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, CartActivity::class.java)
                intent.putExtra(KeyIntent.CART_DATA.name, cartData)
                intent.putExtra(KeyIntent.MITRA_ID.name, mitraId)
                startActivity(intent)
            }
        }
    }

    private fun showProgressDialog() {
        //show dialog
        dialog.setMessage("Mohon tunggu...")
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun showAlertDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pesan")
        builder.setMessage(message)
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }
}