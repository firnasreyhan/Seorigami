package com.project.seorigami.view.activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.project.seorigami.R
import com.project.seorigami.databinding.ActivitySignUpBinding
import com.project.seorigami.util.State
import com.project.seorigami.viewmodel.SignUpViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: SignUpViewModel
    private lateinit var dialog: ProgressDialog

    private var daftarSebagai: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = ProgressDialog(this)
        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]

        val jenisAkun = arrayOf("Pelanggan", "Penjahit")
        val adapter = ArrayAdapter(this, R.layout.item_dropdown_text, jenisAkun)
        binding.autoCompleteTextViewDaftarSebagai.setAdapter(adapter)
        binding.autoCompleteTextViewDaftarSebagai.setText("Pelanggan", false)

        binding.autoCompleteTextViewDaftarSebagai.setOnItemClickListener { parent, view, position, id ->
            if (position == 0) {
                daftarSebagai = 3
            } else if (position == 1) {
                daftarSebagai = 2
            }
        }

        binding.button.setOnClickListener {
            if (validate()) {
                viewModel.signUp(
                    it.context,
                    daftarSebagai,
                    binding.textInputEditTextNama.text.toString(),
                    binding.textInputEditTextEmail.text.toString(),
                    binding.textInputEditTextPassword.text.toString()
                )
            }
        }

        binding.textViewMasuk.setOnClickListener {
            finish()
        }

        viewModel.stateRegister.observe(this) {
            when (it) {
                State.COMPLETE -> {
                    dialog.dismiss()
                    showAlertDialog("Sukses melakukan pendaftaran, silahkan masuk ke dalam aplikasi", true)
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
                showAlertDialog(it.toString(), false)
            }
        }
    }

    private fun validate(): Boolean {
        var check = true

        if (binding.textInputEditTextNama.text.isNullOrEmpty()) {
            binding.textInputEditTextNama.error = "Mohon isi Email anda"
            check = false
        }

        if (binding.textInputEditTextEmail.text.isNullOrEmpty()) {
            binding.textInputEditTextEmail.error = "Mohon isi Email anda"
            check = false
        }

        if (binding.textInputEditTextPassword.text.isNullOrEmpty()) {
            binding.textInputEditTextPassword.error = "Mohon isi Password anda"
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
}