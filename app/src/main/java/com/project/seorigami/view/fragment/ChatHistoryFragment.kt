package com.project.seorigami.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.project.seorigami.R
import com.project.seorigami.adapter.HistoryChatAdapter
import com.project.seorigami.databinding.FragmentChatHistoryBinding
import com.project.seorigami.databinding.FragmentHomeBinding
import com.project.seorigami.model.ChatModel
import com.project.seorigami.model.HistoryChatModel
import com.project.seorigami.util.ItemClickListener
import com.project.seorigami.util.KeyIntent
import com.project.seorigami.util.Prefs
import com.project.seorigami.view.activity.ChatWindowActivity

class ChatHistoryFragment : Fragment() {
    private val listener = object : ItemClickListener<HistoryChatModel> {
        override fun onClickItem(item: HistoryChatModel) {
            val intent = Intent(requireActivity(), ChatWindowActivity::class.java)
            intent.putExtra(KeyIntent.MITRA_ID.name, item.id)
            intent.putExtra(KeyIntent.MITRA_NAME.name, item.name)
            startActivity(intent)
        }
    }

    private var binding: FragmentChatHistoryBinding? = null
    private var historyChatAdapter = HistoryChatAdapter(listener)
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatHistoryBinding.inflate(inflater, container, false)

        binding?.recyclerViewHistoryChat?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = historyChatAdapter
        }

        database = FirebaseDatabase.getInstance("https://ta-penjahit-app-default-rtdb.asia-southeast1.firebasedatabase.app")
        reference = database.getReference("chat")


//        val postListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // Get Post object and use the values to update the UI
//                val historyChat = mutableListOf<HistoryChatModel>()
//                for (child: DataSnapshot in dataSnapshot.children) {
//                    val splitIdAndName = child.key.toString().split("+")
//                    val splitId = splitIdAndName[0].split("_")
//                    val splitName = splitIdAndName[1].split("_")
//                    if (splitId[0] == Prefs(requireActivity()).user?.id.toString() || splitId[1] == Prefs(requireActivity()).user?.id.toString()) {
//                        historyChat.add(
//                            HistoryChatModel(
//                                id = if (Prefs(requireActivity()).user?.`as`.equals("pelanggan",true)) {splitId[0].toInt()} else {splitId[1].toInt()},
//                                name = if (Prefs(requireActivity()).user?.`as`.equals("pelanggan",true)) {splitName[0]} else {splitName[1]},
//                                lastMessage = child.children.last().getValue<ChatModel>()?.message.toString()
//                            )
//                        )
//                    }
////                    val chatModel = child.getValue<ChatModel>()
////                    if (chatModel != null) {
////                        messages.add(chatModel)
////                    }
//                }
//                historyChatAdapter.data = historyChat
////                sizeData = messages.size
//                historyChatAdapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Getting Post failed, log a message
//                Log.w("postListener", "loadPost:onCancelled", databaseError.toException())
//            }
//        }
//        reference.addValueEventListener(postListener)

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onResume() {
        super.onResume()
        reference.get().addOnSuccessListener {
            val historyChat = mutableListOf<HistoryChatModel>()
            for (child: DataSnapshot in it.children) {
                val splitIdAndName = child.key.toString().split("+")
                val splitId = splitIdAndName[0].split("_")
                val splitName = splitIdAndName[1].split("_")
                if (splitId[0] == Prefs(requireActivity()).user?.id.toString() || splitId[1] == Prefs(requireActivity()).user?.id.toString()) {
                    historyChat.add(
                        HistoryChatModel(
                            id = if (Prefs(requireActivity()).user?.`as`.equals("pelanggan",true)) {splitId[0].toInt()} else {splitId[1].toInt()},
                            name = if (Prefs(requireActivity()).user?.`as`.equals("pelanggan",true)) {splitName[0]} else {splitName[1]},
                            lastMessage = child.children.last().getValue<ChatModel>()?.message.toString()
                        )
                    )
                } else {
                    Log.e("firebase", "tidak tercatat")
                }
//                    val chatModel = child.getValue<ChatModel>()
//                    if (chatModel != null) {
//                        messages.add(chatModel)
//                    }
            }
            historyChatAdapter.data = historyChat
//                sizeData = messages.size
            historyChatAdapter.notifyDataSetChanged()

            if (historyChat.isNullOrEmpty()) {
                binding?.recyclerViewHistoryChat?.visibility = View.GONE
                binding?.linearLayoutKosong?.visibility = View.VISIBLE
            } else {
                binding?.recyclerViewHistoryChat?.visibility = View.VISIBLE
                binding?.linearLayoutKosong?.visibility = View.GONE
            }
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }
}