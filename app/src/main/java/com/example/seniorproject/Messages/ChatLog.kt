package com.example.seniorproject.Messages

import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.MainForum.Adapters.ChatLogAdapter
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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.m_activity_chat_log)

        val username = intent.getStringExtra("USERNAME")
        val toID = intent.getStringExtra("USER_KEY")
        //val profileURI = intent.getStringExtra("USER_PROF")

        title = username

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        DaggerAppComponent.create().inject(this)

        myViewModel = ViewModelProvider(this, factory).get(ChatLogViewModel::class.java)
        val binding: MActivityChatLogBinding = DataBindingUtil.setContentView(this,R.layout.m_activity_chat_log)

        myViewModel.toID = toID
        myViewModel.username = username ?: "no username"
        //myViewModel.profileURI = profileURI

        recycler_view_chatLog.layoutManager = LinearLayoutManager(context)


        myViewModel.getChatMessages()?.observe(this, object : Observer<List<ChatMessage>> {
            override
            fun onChanged(@Nullable messages: List<ChatMessage>) {
                recycler_view_chatLog.adapter  = ChatLogAdapter(context, messages)
                recycler_view_chatLog.scrollToPosition(recycler_view_chatLog.adapter!!.itemCount-1)
            }
        })



        binding.chatViewModel = myViewModel
        binding.lifecycleOwner = this

        btn_SendChatLog.setOnClickListener{
            myViewModel.sendMessage()
            editText_chatLog.text.clear()
        }


        //userList.adapter = myViewModel.fetchUsers()?.let { NewMessageAdapter(this, it) }


    }
}
