package com.project.seorigami.view.fragment

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
    private var bahanMitraAdapter = BahanJasaMitraAdapter(listenerHapus)
    private var jasaMitraAdapter = BahanJasaMitraAdapter(listenerHapus)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeMitraBinding.inflate(inflater, container, false)

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

        viewModel.layanan(requireActivity())
        viewModel.dataLayanan.observe(requireActivity()) {
            bahanMitraAdapter.data = it.bahan.toMutableList()
            jasaMitraAdapter.data = it.jasa.toMutableList()
            bahanMitraAdapter.notifyDataSetChanged()
            jasaMitraAdapter.notifyDataSetChanged()
        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}