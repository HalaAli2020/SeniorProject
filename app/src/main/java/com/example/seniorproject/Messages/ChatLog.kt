package com.example.seniorproject.Messages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.R
import com.example.seniorproject.databinding.MActivityChatLogBinding
import com.example.seniorproject.viewModels.ChatLogViewModel
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

        title = username

        val factory = InjectorUtils.provideChatLogViewModelFactory()
        myViewModel = ViewModelProviders.of(this, factory).get(ChatLogViewModel::class.java)
        val binding: MActivityChatLogBinding = DataBindingUtil.setContentView(this,R.layout.m_activity_chat_log)

        binding.chatViewModel = myViewModel
        binding.lifecycleOwner = this


        myViewModel.toID = toID

        //userList.adapter = myViewModel.fetchUsers()?.let { NewMessageAdapter(this, it) }


    }
}
