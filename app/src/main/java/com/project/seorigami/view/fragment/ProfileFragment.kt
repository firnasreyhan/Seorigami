package com.project.seorigami.view.fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.project.seorigami.databinding.FragmentProfileBinding
import com.project.seorigami.model.response.UserDataModel
import com.project.seorigami.util.Prefs
import com.project.seorigami.util.State
import com.project.seorigami.view.activity.SignInActivity
import com.project.seorigami.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {
    private var binding: FragmentProfileBinding? = null
    private lateinit var viewModel: ProfileViewModel
    private lateinit var dialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        dialog = ProgressDialog(requireContext())
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]


        val dataUser = Prefs(requireActivity()).user!!
        binding?.textInputEditTextNama?.setText(dataUser.name)
        binding?.textInputEditTextEmail?.setText(dataUser.email)

        binding?.buttonSimpan?.setOnClickListener {
            if (validate()) {
                viewModel.updatePelanggan(
                    requireActivity(),
                    dataUser.id,
                    dataUser.email,
                    binding?.textInputEditTextNama?.text.toString(),
                    binding?.textInputEditTextPassword?.text.toString()
                )
            }
        }

        binding?.buttonKeluar?.setOnClickListener {
            viewModel.logout(
                it.context
            )
        }

        viewModel.stateLogout.observe(requireActivity()) {
            when (it) {
                State.COMPLETE -> {
                    dialog.dismiss()
                    Prefs(requireContext()).jwt = null
                    Prefs(requireContext()).user = null
                    val intent = Intent(context, SignInActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }

                State.LOADING -> {
                    showProgressDialog()
                }

                else -> {
                    dialog.dismiss()
                }
            }
        }

        viewModel.stateUpdatePelanggan.observe(requireActivity()) {
            when (it) {
                State.COMPLETE -> {
                    dialog.dismiss()
                    val newDataUser = UserDataModel(
                        `as` = dataUser.`as`,
                        created_at = dataUser.created_at,
                        email = dataUser.email,
                        id = dataUser.id,
                        id_cms_privileges = dataUser.id_cms_privileges,
                        name = binding?.textInputEditTextNama?.text.toString(),
                        os_token = dataUser.os_token,
                        photo = dataUser.photo,
                        status = dataUser.status,
                        updated_at = dataUser.updated_at,
                        pelangganMitraData = dataUser.pelangganMitraData
                    )

                    Prefs(requireActivity()).user = null
                    Prefs(requireActivity()).user = newDataUser

                    showAlertDialog("Data user berhasil diperbarui")
                }

                State.LOADING -> {
                    showProgressDialog()
                }

                else -> {
                    dialog.dismiss()
                }
            }
        }

        viewModel.errorMessage.observe(requireActivity()) {
            if (!it.isNullOrEmpty()) {
                showAlertDialog(it.toString())
            }
        }

        return binding?.root
    }

    private fun showProgressDialog() {
        //show dialog
        dialog.setMessage("Mohon tunggu...")
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun showAlertDialog(message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Pesan")
        builder.setMessage(message)
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun validate(): Boolean {
        var check = true

        if (binding?.textInputEditTextNama?.text.isNullOrEmpty()) {
            binding?.textInputEditTextNama?.error = "Mohon isi Nama anda"
            check = false
        }

        if (binding?.textInputEditTextPassword?.text.isNullOrEmpty()) {
            binding?.textInputEditTextPassword?.error = "Mohon isi Password anda"
            check = false
        }

        return check
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}