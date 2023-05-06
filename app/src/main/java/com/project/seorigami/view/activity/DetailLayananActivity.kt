package com.project.seorigami.view.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.project.seorigami.R
import com.project.seorigami.adapter.BahanJasaAdapter
import com.project.seorigami.databinding.ActivityDetailLayananBinding
import com.project.seorigami.model.response.BahanJasaDataModel
import com.project.seorigami.model.response.MitraDataModel
import com.project.seorigami.util.ItemClickListener
import com.project.seorigami.util.KeyIntent
import com.project.seorigami.viewmodel.DetailLayananViewModel
import com.project.seorigami.viewmodel.HomeViewModel

class DetailLayananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailLayananBinding
    private lateinit var  viewModel: DetailLayananViewModel
    private var bahanAdapter = BahanJasaAdapter()
    private var jasaAdapter = BahanJasaAdapter()
    private var pinpoint: String = ""
    private var mitraId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLayananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mitraId = intent.getIntExtra(KeyIntent.MITRA_ID.name, 0)

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
            binding.textViewTitle.text = it.nama
            binding.textViewNama.text = it.nama
            binding.textViewKota.text = it.kota
            binding.textViewDeskripsi.text = it.deskripsi
            pinpoint = it.pinpoint

            Glide.with(this)
                .load(it.foto)
                .into(binding.imageViewLayanan)

            bahanAdapter.data.clear()
            jasaAdapter.data.clear()
            bahanAdapter.data.addAll(it.bahan)
            jasaAdapter.data.addAll(it.jasa)
            bahanAdapter.notifyDataSetChanged()
            jasaAdapter.notifyDataSetChanged()
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

            val intent = Intent(this, CartActivity::class.java)
            intent.putExtra(KeyIntent.CART_DATA.name, cartData)
            intent.putExtra(KeyIntent.MITRA_ID.name, mitraId)
            startActivity(intent)
        }
    }
}