package com.example.seniorproject.Messages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.Dagger.DaggerAppComponent
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

    private lateinit var searchview: SearchView
    lateinit var ada: NewMessageAdapter
    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        //Set title and add back button
        this.title = "New Message"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.m_activity_new_message)

        //inject dagger app component and initialize viewmodel
        DaggerAppComponent.create().inject(this)
        myViewModel = ViewModelProvider(this, factory).get(NewMessageViewModel::class.java)

        //Declare and set up the search box
        searchview = user_search
        setUpSearchView()

        //Set up RecyclerView with data from Firebase
        userList.layoutManager = LinearLayoutManager(this)
        myViewModel.fetchUsers()?.observe(this,
            Observer<List<User>> { articles ->
                ada = NewMessageAdapter(context, articles)
                userList.adapter = ada
            })
    }

    //Search view setup to allow searching of data
    private fun setUpSearchView() {
        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                ada.onfilter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
        })
    }

    //Allows back button to be pressed to previous activity
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
