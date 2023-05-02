package com.project.seorigami.view.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.project.seorigami.R
import com.project.seorigami.adapter.BahanJasaAdapter
import com.project.seorigami.databinding.ActivityDetailLayananBinding
import com.project.seorigami.viewmodel.DetailLayananViewModel
import com.project.seorigami.viewmodel.HomeViewModel

class DetailLayananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailLayananBinding
    private lateinit var  viewModel: DetailLayananViewModel
    private var bahanAdapter = BahanJasaAdapter()
    private var jasaAdapter = BahanJasaAdapter()
    private var pinpoint: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLayananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[DetailLayananViewModel::class.java]

        binding.recyclerViewBahan.apply {
            layoutManager = LinearLayoutManager(this@DetailLayananActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = bahanAdapter
        }
        binding.recyclerViewJasa.apply {
            layoutManager = LinearLayoutManager(this@DetailLayananActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = jasaAdapter
        }

        viewModel.layanan(this, 1)
        viewModel.dataLayanan.observe(this) {
            binding.textViewNama.text = it.nama
            binding.textViewKota.text = it.kota
            binding.textViewDeskripsi.text = it.deskripsi
            pinpoint = it.pinpoint

            Glide.with(this)
                .load(it.foto)
                .into(binding.imageViewLayanan)

            bahanAdapter.data = it.bahan
            jasaAdapter.data = it.jasa
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
    }
}