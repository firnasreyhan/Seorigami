package com.project.seorigami.view.activity

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.net.toFile
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.project.seorigami.R
import com.project.seorigami.databinding.ActivityAddMaterialAndServiceBinding
import com.project.seorigami.util.Prefs
import com.project.seorigami.util.State
import com.project.seorigami.viewmodel.AddMaterialAndServiceViewModel
import com.project.seorigami.viewmodel.DetailLayananViewModel

class AddMaterialAndServiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddMaterialAndServiceBinding
    private lateinit var  viewModel: AddMaterialAndServiceViewModel
    private lateinit var dialog: ProgressDialog
    private var kategori = ArrayList<String>()
    private var idKategori = ArrayList<Int>()
    private var layananUri: Uri? = null
    private var selectedKategori = 0
    private var selectedJenis = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMaterialAndServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = ProgressDialog(this)
        viewModel = ViewModelProvider(this)[AddMaterialAndServiceViewModel::class.java]

        viewModel.kategori(this)
        viewModel.dataKategori.observe(this) {
            it.forEach {
                kategori.add(it.keterangan)
                idKategori.add(it.id)
            }
            val adapter = ArrayAdapter(this, R.layout.item_dropdown_text, kategori)
            binding.autoCompleteTextViewKategori.setAdapter(adapter)
            binding.autoCompleteTextViewKategori.setText(kategori.first(), false)
            selectedKategori = idKategori.first()
        }

        val jenis = arrayOf("Jasa", "Bahan")
        val adapter = ArrayAdapter(this, R.layout.item_dropdown_text, jenis)
        binding.autoCompleteTextViewJenis.setAdapter(adapter)
        binding.autoCompleteTextViewJenis.setText("Jasa", false)

        binding.autoCompleteTextViewJenis.setOnItemClickListener { parent, view, position, id ->
            selectedJenis = position
        }

        binding.autoCompleteTextViewKategori.setOnItemClickListener { parent, view, position, id ->
            selectedKategori = idKategori[position]
        }

        binding.imageViewFoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(256)
                .saveDir(getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)
                .start()
        }

        binding.button.setOnClickListener {
            if (validate()) {
                viewModel.tambahLayanan(
                    this,
                    selectedJenis,
                    selectedKategori,
                    binding.textInputEditTextKeterangan.text.toString(),
                    binding.textInputEditTextHarga.text.toString(),
                    layananUri!!.toFile()
                )
            }
        }

        viewModel.stateLayanan.observe(this) {
            when (it) {
                State.COMPLETE -> {
                    dialog.dismiss()
                    showAlertDialog("Sukses menambahkan layanan", true)
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

        viewModel.stateKategori.observe(this) {
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
                showAlertDialog(it.toString(), false)
            }
        }
    }

    private fun validate(): Boolean {
        var check = true

        if (binding.textInputEditTextKeterangan.text.isNullOrEmpty()) {
            binding.textInputEditTextKeterangan.error = "Mohon isi Keterangan layanan"
            check = false
        }

        if (binding.textInputEditTextHarga.text.isNullOrEmpty()) {
            binding.textInputEditTextHarga.error = "Mohon isi Harga Layanan anda"
            check = false
        }

        if (layananUri == null) {
            Toast.makeText(this, "Mohon tambahkan Foto Layanan anda", Toast.LENGTH_SHORT).show()
            check = false
        }

        return check
    }

    private fun showProgressDialog() {
        //show dialog
        dialog.setMessage("Mohon tunggu...")
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun showAlertDialog(message: String, isSuccess: Boolean) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pesan")
        builder.setMessage(message)
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            dialog.dismiss()
            if (isSuccess) {
                finish()
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                val uri: Uri = data?.data!!

                // Use Uri object instead of File to avoid storage permissions
                Glide.with(this)
                    .load(uri)
                    .placeholder(R.drawable.ic_logo_seorigami)
                    .error(R.drawable.ic_logo_seorigami)
                    .into(binding.imageViewFoto)

                layananUri = uri
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}