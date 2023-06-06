package com.project.seorigami.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.project.seorigami.R
import com.project.seorigami.databinding.ActivityChatWindowBinding
import com.project.seorigami.model.ChatModel
import com.project.seorigami.util.KeyIntent
import com.project.seorigami.util.Prefs

class ChatWindowActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatWindowBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private var mitraName: String = ""
    private var mitraId: Int = 0
    private var sizeData: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatWindowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mitraName = intent.getStringExtra(KeyIntent.MITRA_NAME.name).toString()
        mitraId = intent.getIntExtra(KeyIntent.MITRA_ID.name, 0)

        val path = if (Prefs(this).user?.`as`.equals("pelanggan",true)) {
            "${mitraId}_${Prefs(this).user?.id}"
        } else {
            "${Prefs(this).user?.id}_${mitraId}"
        }

        database = FirebaseDatabase.getInstance("https://ta-penjahit-app-default-rtdb.asia-southeast1.firebasedatabase.app")
        reference = database.getReference("chat")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                for (child: DataSnapshot in dataSnapshot.children) {
                    sizeData++
                    val chatModel = child.getValue<ChatModel>()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("postListener", "loadPost:onCancelled", databaseError.toException())
            }
        }
        reference.child(path).addValueEventListener(postListener)

        binding.imageViewSend.setOnClickListener {
            if (!binding.textInputEditTextChat.text.isNullOrEmpty()) {
                val chat = ChatModel(
                    message = binding.textInputEditTextChat.text.toString(),
                    sender = Prefs(this).user?.id.toString().toInt()
                )
                reference.child(path).child(sizeData.toString()).setValue(chat)
            }
        }
    }
}