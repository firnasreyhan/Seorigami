package com.project.seorigami.view.activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.project.seorigami.databinding.ActivitySignInBinding
import com.project.seorigami.util.ApiResponse
import com.project.seorigami.util.State
import com.project.seorigami.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private val viewModel: SignInViewModel by viewModels()
    private lateinit var dialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = ProgressDialog(this)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.signInFlow.collect {
                    when(it) {
                        is ApiResponse.Loading -> {

                        }
                        is ApiResponse.Error -> {

                        }
                        is ApiResponse.Success -> {

                        }
                    }
                }
            }
        }

        binding.button.setOnClickListener {
            if (validate()) {
                viewModel.signIn(
                    it.context,
                    binding.textInputEditTextEmail.text.toString(),
                    binding.textInputEditTextPassword.text.toString()
                )
            }
        }

        binding.textViewDaftar.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        viewModel.stateLogin.observe(this) {
            when (it) {
                State.COMPLETE -> {
                    dialog.dismiss()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
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

    private fun validate(): Boolean {
        var check = true

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