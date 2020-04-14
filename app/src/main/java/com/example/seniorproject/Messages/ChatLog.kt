package com.example.seniorproject.Messages

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.MainForum.Adapters.ChatLogAdapter
import com.example.seniorproject.MainForum.MainForum
import com.example.seniorproject.R
import com.example.seniorproject.data.models.ChatMessage
import com.example.seniorproject.databinding.MActivityChatLogBinding
import com.example.seniorproject.viewModels.ChatLogViewModel
import kotlinx.android.synthetic.main.m_activity_chat_log.*
import javax.inject.Inject


class ChatLog : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: ChatLogViewModel
    val context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        //Set up layout and add a back button
        super.onCreate(savedInstanceState)
        setContentView(R.layout.m_activity_chat_log)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        val layoutChangeListener = View.OnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (oldBottom != 0) {
                //when softkeyboard opens, the height of layout get's small, and when softkeyboa
                //-rd closes the height grows back(gets larger).We can find the change of height
                // by doing oldBotton - bottom, and the result of subtraction is how much we nee
                //-d to scroll. Change of height is positive if keyboard is opened, and negative
                //if it's closed.
                val pixelsToScrollVertically = oldBottom - bottom
                recycler_view_chatLog.scrollBy(0, pixelsToScrollVertically)
            }
        }

        //Grab information sent from intent to set up chat log and set title bar title equal to the username
        val username = intent.getStringExtra("USERNAME")
        val toID = intent.getStringExtra("USER_KEY")
        val profileImageUrl = intent.getStringExtra("USER_PROF")
        title = username

        //inject dagger app component, initialize viewmodel, and set up binding
        DaggerAppComponent.create().inject(this)
        myViewModel = ViewModelProvider(this, factory).get(ChatLogViewModel::class.java)
        val binding: MActivityChatLogBinding = DataBindingUtil.setContentView(this, R.layout.m_activity_chat_log)

        //Send data to viewmodel about the user
        myViewModel.toID = toID
        myViewModel.username = username ?: "no username"
        myViewModel.profileImageUrl = profileImageUrl

        recycler_view_chatLog.setHasFixedSize(true)
        recycler_view_chatLog.layoutManager = LinearLayoutManager(context)

        //Grab all messages between two users
        myViewModel.getChatMessages()?.observe(this, object : Observer<List<ChatMessage>> {
            override
            fun onChanged(@Nullable messages: List<ChatMessage>) {
                recycler_view_chatLog.adapter = ChatLogAdapter(context, messages)
                recycler_view_chatLog.scrollToPosition(recycler_view_chatLog.adapter!!.itemCount - 1)
            }
        })

        //Enable databinding
        binding.chatViewModel = myViewModel
        binding.lifecycleOwner = this

        //Set the on click listener for sending a message
        btn_SendChatLog.setOnClickListener {
            myViewModel.sendMessage()
            editText_chatLog.text.clear()
        }

        recycler_view_chatLog.addOnLayoutChangeListener(layoutChangeListener)

    }

    //Allows back button to be pressed to previous activity
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
