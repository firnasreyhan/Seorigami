package com.project.seorigami.view.activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.seorigami.adapter.CartAdapter
import com.project.seorigami.databinding.ActivityCartBinding
import com.project.seorigami.model.request.CartDataModel
import com.project.seorigami.model.request.TransactionItemModel
import com.project.seorigami.model.response.BahanJasaDataModel
import com.project.seorigami.util.ItemClickListener
import com.project.seorigami.util.KeyIntent
import com.project.seorigami.util.State
import com.project.seorigami.util.Utils
import com.project.seorigami.viewmodel.CartViewModel

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
    private lateinit var viewModel: CartViewModel
    private lateinit var dialog: ProgressDialog
    private var rawCartData = ArrayList<BahanJasaDataModel>()
    private var cartAdapter = CartAdapter(listenerIsUpdatePrice)
    private var cartData = mutableListOf<CartDataModel>()
    private var mitraId: Int = 0
    private var subtotal = 0
    private var admin = 4000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = ProgressDialog(this)
        viewModel = ViewModelProvider(this)[CartViewModel::class.java]

        mitraId = intent.getIntExtra(KeyIntent.MITRA_ID.name, 0)
        rawCartData = intent.getSerializableExtra(KeyIntent.CART_DATA.name) as ArrayList<BahanJasaDataModel>
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

        binding.button.setOnClickListener {
            viewModel.transaction(
                this,
                mitra_id = mitraId,
                biaya_admin = admin,
                subtotal = subtotal,
                total = admin + subtotal,
                detail = getTransactionItem()
            )
        }

        viewModel.transactionData.observe(this) {
            if (it != null) {
                val intent = Intent(this, StatusPaymentActivity::class.java)
                intent.putExtra(KeyIntent.TRANSACTION_DATA.name, it)
                startActivity(intent)
            }
        }

        viewModel.stateTransaction.observe(this) {
            when (it) {
                State.COMPLETE -> {
                    dialog.dismiss()
                }

                State.LOADING -> {
                    showProgressDialog()
                }

                else -> {
                    dialog.dismiss()
                }
            }
        }

        viewModel.errorMessage.observe(this) {
            if (!it.isNullOrEmpty()) {
                showAlertDialog(it.toString())
            }
        }
    }

    fun countTotalAndSubTotal() {
        cartAdapter.data.forEach {
            subtotal += it.subtotal
        }
        binding.textViewSubtotal.text = Utils.changePrice(subtotal.toString())
        binding.textViewBiayaAdmin.text = Utils.changePrice(admin.toString())
        binding.textViewTotal.text = Utils.changePrice("${subtotal + admin}")
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

    private fun getTransactionItem() : List<TransactionItemModel> {
        var data = mutableListOf<TransactionItemModel>()
        cartAdapter.data.forEach {
            data.add(
                TransactionItemModel(
                    it.subtotal,
                    it.id,
                    it.qty
                )
            )
        }
        return data.toList()
    }
}