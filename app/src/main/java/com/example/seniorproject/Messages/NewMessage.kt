package com.example.seniorproject.Messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.MainForum.Adapters.NewMessageAdapter
import com.example.seniorproject.R
import com.example.seniorproject.data.models.User
import com.example.seniorproject.viewModels.NewMessageViewModel
import kotlinx.android.synthetic.main.m_activity_new_message.*
import javax.inject.Inject



class NewMessage : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: NewMessageViewModel
    val context = this
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.m_activity_new_message)

        actionBar?.title = "New Message"
        val factory = InjectorUtils.provideNewMessageViewModelFactory()
        myViewModel = ViewModelProviders.of(this, factory).get(NewMessageViewModel::class.java)
        userList.layoutManager = LinearLayoutManager(this)

        myViewModel.fetchUsers()?.observe(this, object : Observer<List<User>> {
            override
            fun onChanged(@Nullable articles: List<User>) {
                userList.adapter  = NewMessageAdapter(context, articles)
            }
        })







        //userList.adapter = myViewModel.fetchUsers()?.let { NewMessageAdapter(this, it) }


    }
}
