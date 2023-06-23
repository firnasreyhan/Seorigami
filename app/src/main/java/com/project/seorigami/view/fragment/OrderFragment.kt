package com.project.seorigami.view.fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.seorigami.R
import com.project.seorigami.adapter.HistoryOrderAdapter
import com.project.seorigami.databinding.FragmentHomeBinding
import com.project.seorigami.databinding.FragmentOrderBinding
import com.project.seorigami.model.response.HistoryTransactionItemModel
import com.project.seorigami.model.response.MitraDataModel
import com.project.seorigami.util.ItemClickListener
import com.project.seorigami.util.KeyIntent
import com.project.seorigami.util.Prefs
import com.project.seorigami.util.State
import com.project.seorigami.view.activity.DetailLayananActivity
import com.project.seorigami.view.activity.SignInActivity
import com.project.seorigami.viewmodel.HomeViewModel
import com.project.seorigami.viewmodel.OrderViewModel

class OrderFragment : Fragment() {
    private val listenerIsDone = object : ItemClickListener<HistoryTransactionItemModel> {
        override fun onClickItem(item: HistoryTransactionItemModel) {
            viewModel.confirmOrder(requireActivity(), item.id)
        }
    }

    private var binding: FragmentOrderBinding? = null
    private lateinit var viewModel: OrderViewModel
    private lateinit var dialog: ProgressDialog
    private var progressOrderAdapter = HistoryOrderAdapter(listenerIsDone)
    private var completeOrderAdapter = HistoryOrderAdapter(listenerIsDone)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderBinding.inflate(inflater, container, false)

        dialog = ProgressDialog(requireActivity())
        viewModel = ViewModelProvider(this)[OrderViewModel::class.java]

        binding?.recyclerViewCompleteOrder?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = completeOrderAdapter
        }

        binding?.recyclerViewProgressOrder?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = progressOrderAdapter
        }

        progressOrderAdapter.isSeller = Prefs(requireActivity()).user?.`as`.equals("penjahit")
        completeOrderAdapter.isSeller = Prefs(requireActivity()).user?.`as`.equals("penjahit")

        binding?.cardViewProgress?.setOnClickListener {
            binding?.recyclerViewProgressOrder?.visibility = View.VISIBLE
            binding?.recyclerViewCompleteOrder?.visibility = View.GONE

            binding?.cardViewProgress?.setCardBackgroundColor(requireActivity().resources.getColor(R.color.white))
            binding?.cardViewComplete?.setCardBackgroundColor(requireActivity().resources.getColor(R.color.backgroundUnselectedCategory))
        }

        binding?.cardViewComplete?.setOnClickListener {
            binding?.recyclerViewProgressOrder?.visibility = View.GONE
            binding?.recyclerViewCompleteOrder?.visibility = View.VISIBLE

            binding?.cardViewProgress?.setCardBackgroundColor(requireActivity().resources.getColor(R.color.backgroundUnselectedCategory))
            binding?.cardViewComplete?.setCardBackgroundColor(requireActivity().resources.getColor(R.color.white))
        }

        viewModel.order(requireActivity())
        viewModel.orderData.observe(requireActivity()) {
            val completeData = mutableListOf<HistoryTransactionItemModel>()
            val progressData = mutableListOf<HistoryTransactionItemModel>()

            it.forEach {
                if (it.status == 0) {
                    progressData.add(it)
                } else {
                    completeData.add(it)
                }
            }

            completeOrderAdapter.data = completeData
            progressOrderAdapter.data = progressData

            completeOrderAdapter.notifyDataSetChanged()
            progressOrderAdapter.notifyDataSetChanged()

            if (completeData.isNullOrEmpty() && progressData.isNullOrEmpty()) {
                binding?.linearLayoutKosong?.visibility = View.VISIBLE
                binding?.linearLayoutKonten?.visibility = View.GONE
            } else {
                binding?.linearLayoutKosong?.visibility = View.GONE
                binding?.linearLayoutKonten?.visibility = View.VISIBLE
            }
        }

        viewModel.stateOrder.observe(requireActivity()) {
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

                else -> {dialog.dismiss()
                    Prefs(requireContext()).jwt = null
                    Prefs(requireContext()).user = null
                    val intent = Intent(context, SignInActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    Toast.makeText(requireActivity(), "Silahkan login kembali", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.stateConfirmOrder.observe(requireActivity()) {
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

                else -> {dialog.dismiss()
                    Prefs(requireContext()).jwt = null
                    Prefs(requireContext()).user = null
                    val intent = Intent(context, SignInActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    Toast.makeText(requireActivity(), "Silahkan login kembali", Toast.LENGTH_SHORT).show()
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