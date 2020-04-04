package com.example.seniorproject.Messages

import android.os.Bundle
import android.widget.SearchView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
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

    private lateinit var searchview : SearchView
    lateinit var ada : NewMessageAdapter
    //lateinit var binding : ActivityNewMessageBinding
    val context = this
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView( R.layout.m_activity_new_message)



        actionBar?.title = "New Message"
        val factory = InjectorUtils.provideNewMessageViewModelFactory()
        myViewModel = ViewModelProviders.of(this, factory).get(NewMessageViewModel::class.java)
        searchview = user_search
        setupsearchview()

        userList.layoutManager = LinearLayoutManager(this)

        myViewModel.fetchUsers()?.observe(this, object : Observer<List<User>> {
            override
            fun onChanged(@Nullable articles: List<User>) {
                ada  = NewMessageAdapter(context, articles)
                userList.adapter = ada
            }
        })







        //userList.adapter = myViewModel.fetchUsers()?.let { NewMessageAdapter(this, it) }


    }
    private fun setupsearchview()
    {
        searchview.isIconifiedByDefault = false
        searchview.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {

                ada.onfilter(newText)

                return false

            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                // ada.onfilter(query)
                return true
            }
        })
        searchview.isSubmitButtonEnabled = true
        searchview.queryHint = "Search Here"
    }
}
