package com.project.seorigami.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.project.seorigami.view.activity.DetailLayananActivity
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
    private var progressOrderAdapter = HistoryOrderAdapter(listenerIsDone)
    private var completeOrderAdapter = HistoryOrderAdapter(listenerIsDone)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderBinding.inflate(inflater, container, false)

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
        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}