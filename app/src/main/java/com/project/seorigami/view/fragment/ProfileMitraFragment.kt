package com.project.seorigami.view.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.project.seorigami.R
import com.project.seorigami.databinding.FragmentProfileMitraBinding
import com.project.seorigami.model.response.PelangganMitraDataModel
import com.project.seorigami.model.response.UserDataModel
import com.project.seorigami.util.KeyIntent
import com.project.seorigami.util.Prefs
import com.project.seorigami.util.State
import com.project.seorigami.util.Utils
import com.project.seorigami.view.activity.MapsActivity
import com.project.seorigami.view.activity.SignInActivity
import com.project.seorigami.viewmodel.ProfileViewModel

class ProfileMitraFragment : Fragment() {
    private var binding: FragmentProfileMitraBinding? = null
    private lateinit var viewModel: ProfileViewModel
    private lateinit var dialog: ProgressDialog
    private var fotoUri: Uri? = null

    private val LAT_LONG_REQUEST_CODE = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileMitraBinding.inflate(inflater, container, false)

        dialog = ProgressDialog(requireContext())
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        val dataUser = Prefs(requireActivity()).user!!
        binding?.textInputEditTextNama?.setText(dataUser.name)
        binding?.textInputEditTextEmail?.setText(dataUser.email)
        binding?.textInputEditTextDeskripsi?.setText(dataUser.pelangganMitraData.deskripsi)
        binding?.textInputEditTextPinPoint?.setText(dataUser.pelangganMitraData.pinpoint)
        binding?.textInputEditTextKabKota?.setText(dataUser.pelangganMitraData.kota)
        binding?.let {
            Glide.with(this)
                .load(Utils.reformatImageUrl(dataUser.pelangganMitraData.foto))
                .placeholder(R.drawable.ic_logo_seorigami)
                .error(R.drawable.ic_logo_seorigami)
                .into(it.imageViewFoto)
        }

        binding?.buttonGantiFoto?.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(256)
                .saveDir(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)
                .start()
        }

        binding?.buttonSimpan?.setOnClickListener {
            if (validate()) {
                viewModel.updateMitra(
                    requireActivity(),
                    dataUser.id,
                    dataUser.email,
                    binding?.textInputEditTextNama?.text.toString(),
                    binding?.textInputEditTextPassword?.text.toString(),
                    binding?.textInputEditTextPinPoint?.text.toString(),
                    binding?.textInputEditTextDeskripsi?.text.toString(),
                    binding?.textInputEditTextKabKota?.text.toString(),
                    fotoUri!!.toFile()
                )
            }
        }

        binding?.textInputEditTextPinPoint?.setOnClickListener {
            val intent = Intent(requireActivity(), MapsActivity::class.java)
            startActivityForResult(intent, LAT_LONG_REQUEST_CODE)
        }

        binding?.buttonKeluar?.setOnClickListener {
            viewModel.logout(
                it.context
            )
        }

        viewModel.stateUpdateMitra.observe(requireActivity()) {
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

        viewModel.stateUpdatePelanggan.observe(requireActivity()) {
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

        viewModel.stateUpdateMitra.observe(requireActivity()) {
            when (it) {
                State.COMPLETE -> {
                    dialog.dismiss()
                    val newDataPelangganMitra = PelangganMitraDataModel(
                        `as` = dataUser.pelangganMitraData.`as`,
                        created_at = dataUser.pelangganMitraData.created_at,
                        deskripsi = binding?.textInputEditTextDeskripsi?.text.toString(),
                        email = dataUser.pelangganMitraData.email,
                        foto = dataUser.pelangganMitraData.foto,
                        id = dataUser.pelangganMitraData.id,
                        kota = binding?.textInputEditTextKabKota?.text.toString(),
                        nama = dataUser.pelangganMitraData.nama,
                        password = dataUser.pelangganMitraData.password,
                        pinpoint = binding?.textInputEditTextPinPoint?.text.toString(),
                        updated_at = dataUser.pelangganMitraData.updated_at,
                        user_id = dataUser.pelangganMitraData.user_id
                    )

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
                        pelangganMitraData = newDataPelangganMitra
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

        if (binding?.textInputEditTextPinPoint?.text.isNullOrEmpty()) {
            binding?.textInputEditTextPinPoint?.error = "Mohon isi Pin Point anda"
            check = false
        }

        if (fotoUri == null) {
            Toast.makeText(requireActivity(), "Mohon tambahkan Foto anda", Toast.LENGTH_SHORT).show()
            check = false
        }

        return check
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                if (requestCode == LAT_LONG_REQUEST_CODE) {
                    Log.e("loaction A", "${data?.getStringExtra(KeyIntent.LAT_LONG_DATA.name)}")
                    Log.e("kota A", "${data?.getStringExtra(KeyIntent.KOTA_DATA.name)}")

                    binding?.textInputEditTextPinPoint?.setText("${data?.getStringExtra(KeyIntent.LAT_LONG_DATA.name)}")
                    binding?.textInputEditTextKabKota?.setText("${data?.getStringExtra(KeyIntent.KOTA_DATA.name)}")
                } else {
                    //Image Uri will not be null for RESULT_OK
                    val uri: Uri = data?.data!!

                    // Use Uri object instead of File to avoid storage permissions
                    binding?.let {
                        Glide.with(this)
                            .load(uri)
                            .placeholder(R.drawable.ic_logo_seorigami)
                            .error(R.drawable.ic_logo_seorigami)
                            .into(it.imageViewFoto)
                    }

                    fotoUri = uri
                }
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

}