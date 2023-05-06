package com.project.seorigami.view.fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.seorigami.R
import com.project.seorigami.adapter.KategoriAdapter
import com.project.seorigami.adapter.MitraAdapter
import com.project.seorigami.databinding.FragmentHomeBinding
import com.project.seorigami.model.response.MitraDataModel
import com.project.seorigami.util.GridItemDecoration
import com.project.seorigami.util.ItemClickListener
import com.project.seorigami.util.KeyIntent
import com.project.seorigami.util.PixelHelper
import com.project.seorigami.view.activity.DetailLayananActivity
import com.project.seorigami.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private val listenerMitra = object : ItemClickListener<MitraDataModel> {
        override fun onClickItem(item: MitraDataModel) {
            val intent = Intent(requireActivity(), DetailLayananActivity::class.java)
            intent.putExtra(KeyIntent.MITRA_ID.name, item.id)
            startActivity(intent)
        }
    }

    private val listenerMapsMitra = object : ItemClickListener<String> {
        override fun onClickItem(item: String) {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://maps.google.com/?q=${item}")
            )
            startActivity(intent)
        }
    }

    private val listenerKategori = object : ItemClickListener<Int> {
        override fun onClickItem(item: Int) {
            viewModel.mitra(requireActivity(), "Malang", item)
        }
    }

    private var binding: FragmentHomeBinding? = null
    private lateinit var viewModel: HomeViewModel
    private lateinit var dialog: ProgressDialog
    private var kategoriAdapter = KategoriAdapter(listenerKategori)
    private var mitraAdapter = MitraAdapter(listenerMitra, listenerMapsMitra)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        dialog = ProgressDialog(requireActivity())
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        binding?.recyclerViewKategori?.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = this@HomeFragment.kategoriAdapter
        }

        binding?.recyclerViewPenjahitTerdekat?.apply {
            layoutManager = GridLayoutManager(requireActivity(), resources.getInteger(R.integer.grid_count))
            itemAnimator = null
            val marginDecoration = resources.getDimension(R.dimen.space_half).toInt()
            val marginDp = PixelHelper.convertDpToPx(marginDecoration, resources)
            addItemDecoration(
                GridItemDecoration(
                    resources.getInteger(R.integer.grid_count),
                    marginDp,
                    true
                )
            )
            adapter = mitraAdapter
        }

        viewModel.kategori(requireActivity())
        viewModel.dataKategori.observe(requireActivity()) {
            kategoriAdapter.data = it ?: emptyList()
            kategoriAdapter.notifyDataSetChanged()

            if (it.isNotEmpty()) {
                viewModel.mitra(requireActivity(), "Malang", it.first().id)
                kategoriAdapter.selected = it.first()
            }
        }

        viewModel.dataMitra.observe(requireActivity()) {
            mitraAdapter.data = it ?: emptyList()
            mitraAdapter.notifyDataSetChanged()
        }

//        viewModel.stateKategori.observe(requireActivity()) {
//            when (it) {
//                State.COMPLETE -> {
//                    dialog.dismiss()
//                }
//
//                State.LOADING -> {
//                    showProgressDialog()
//                }
//
//                else -> {
//                    dialog.dismiss()
//                }
//            }
//        }
//
//        viewModel.stateMitra.observe(requireActivity()) {
//            when (it) {
//                State.COMPLETE -> {
//                    dialog.dismiss()
//                }
//
//                State.LOADING -> {
//                    showProgressDialog()
//                }
//
//                else -> {
//                    dialog.dismiss()
//                }
//            }
//        }

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
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Pesan")
        builder.setMessage(message)
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}