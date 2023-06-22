package com.project.seorigami.view.fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.project.seorigami.R
import com.project.seorigami.adapter.BahanJasaMitraAdapter
import com.project.seorigami.databinding.FragmentHomeBinding
import com.project.seorigami.databinding.FragmentHomeMitraBinding
import com.project.seorigami.util.GridItemDecoration
import com.project.seorigami.util.ItemClickListener
import com.project.seorigami.util.PixelHelper
import com.project.seorigami.util.State
import com.project.seorigami.view.activity.AddMaterialAndServiceActivity
import com.project.seorigami.viewmodel.HomeMitraViewModel
import com.project.seorigami.viewmodel.HomeViewModel

class HomeMitraFragment : Fragment() {
    private val listenerHapus = object : ItemClickListener<Int> {
        override fun onClickItem(item: Int) {
            viewModel.deleteLayanan(requireActivity(), item)
        }
    }

    private var binding: FragmentHomeMitraBinding? = null
    private lateinit var viewModel: HomeMitraViewModel
    private lateinit var dialog: ProgressDialog
    private var bahanMitraAdapter = BahanJasaMitraAdapter(listenerHapus)
    private var jasaMitraAdapter = BahanJasaMitraAdapter(listenerHapus)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeMitraBinding.inflate(inflater, container, false)

        dialog = ProgressDialog(requireActivity())
        viewModel = ViewModelProvider(this)[HomeMitraViewModel::class.java]

        binding?.recyclerViewBahan?.apply {
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
            adapter = bahanMitraAdapter
        }

        binding?.recyclerViewJasa?.apply {
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
            adapter = jasaMitraAdapter
        }

        binding?.cardViewJasa?.setOnClickListener {
            binding?.recyclerViewJasa?.visibility = View.VISIBLE
            binding?.recyclerViewBahan?.visibility = View.GONE

            binding?.cardViewJasa?.setCardBackgroundColor(requireActivity().resources.getColor(R.color.white))
            binding?.cardViewBahan?.setCardBackgroundColor(requireActivity().resources.getColor(R.color.backgroundUnselectedCategory))
        }

        binding?.cardViewBahan?.setOnClickListener {
            binding?.recyclerViewJasa?.visibility = View.GONE
            binding?.recyclerViewBahan?.visibility = View.VISIBLE

            binding?.cardViewJasa?.setCardBackgroundColor(requireActivity().resources.getColor(R.color.backgroundUnselectedCategory))
            binding?.cardViewBahan?.setCardBackgroundColor(requireActivity().resources.getColor(R.color.white))
        }

        binding?.imageViewTambah?.setOnClickListener {
            startActivity(Intent(requireActivity(), AddMaterialAndServiceActivity::class.java))
        }

        viewModel.dataLayanan.observe(requireActivity()) {
            bahanMitraAdapter.data = it.bahan.toMutableList()
            jasaMitraAdapter.data = it.jasa.toMutableList()
            bahanMitraAdapter.notifyDataSetChanged()
            jasaMitraAdapter.notifyDataSetChanged()

            if (it.bahan.isNullOrEmpty() && it.jasa.isNullOrEmpty()) {
                binding?.linearLayoutKosong?.visibility = View.VISIBLE
                binding?.linearLayoutKonten?.visibility = View.GONE
            } else {
                binding?.linearLayoutKosong?.visibility = View.GONE
                binding?.linearLayoutKonten?.visibility = View.VISIBLE
            }
        }

        viewModel.stateLayanan.observe(requireActivity()) {
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

        viewModel.stateHapusLayanan.observe(requireActivity()) {
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

        viewModel.errorMessage.observe(requireActivity()) {
            if (!it.isNullOrEmpty()) {
                showAlertDialog(it.toString())
            }
        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.layanan(requireActivity())
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
}