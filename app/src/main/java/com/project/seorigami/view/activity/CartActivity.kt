package com.project.seorigami.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.seorigami.R
import com.project.seorigami.adapter.CartAdapter
import com.project.seorigami.databinding.ActivityCartBinding
import com.project.seorigami.model.request.CartDataModel
import com.project.seorigami.model.response.BahanJasaDataModel
import com.project.seorigami.model.response.MitraDataModel
import com.project.seorigami.util.ItemClickListener
import com.project.seorigami.util.KeyIntent
import com.project.seorigami.util.Utils

class CartActivity : AppCompatActivity() {
    private val listenerIsUpdatePrice = object : ItemClickListener<Boolean> {
        override fun onClickItem(item: Boolean) {
            if (item) {
                countTotalAndSubTotal()
            } else {
                finish()
            }
        }
    }

    private lateinit var binding: ActivityCartBinding
    private lateinit var rawCartData: ArrayList<BahanJasaDataModel>
    private var cartAdapter = CartAdapter(listenerIsUpdatePrice)
    private var cartData = mutableListOf<CartDataModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rawCartData = intent.getParcelableExtra(KeyIntent.CART_DATA.name)!!
        rawCartData.forEach {
            cartData.add(CartDataModel(
                    it.foto,
                    it.harga,
                    it.id,
                    it.keterangan,
                    1,
                    it.harga,
            ))
        }

        binding.recyclerViewPesanan.apply {
            layoutManager = LinearLayoutManager(this@CartActivity)
            adapter = cartAdapter
        }

        cartAdapter.data = cartData
        cartAdapter.notifyDataSetChanged()

        countTotalAndSubTotal()

        binding.imageViewBack.setOnClickListener {
            finish()
        }
    }

    fun countTotalAndSubTotal() {
        var subtotal = 0
        var admin = 4000
        cartAdapter.data.forEach {
            subtotal += it.subtotal
        }
        binding.textViewSubtotal.text = Utils.changePrice(subtotal.toString())
        binding.textViewBiayaAdmin.text = Utils.changePrice(admin.toString())
        binding.textViewTotal.text = Utils.changePrice("${subtotal + admin}")
    }
}