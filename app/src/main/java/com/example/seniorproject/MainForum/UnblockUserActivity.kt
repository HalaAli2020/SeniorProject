package com.example.seniorproject.MainForum

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.MainForum.Adapters.BlockedUsersAdapter
import com.example.seniorproject.MainForum.Adapters.PostAdapter
import com.example.seniorproject.R
import com.example.seniorproject.viewModels.CommunityPostViewModel
import com.example.seniorproject.viewModels.SettingsViewModel
import kotlinx.android.synthetic.main.activity_community_posts.*
import kotlinx.android.synthetic.main.activity_unblock_users.*
import javax.inject.Inject

class UnblockUserActivity: AppCompatActivity() {

    private lateinit var adapter: BlockedUsersAdapter
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: SettingsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unblock_users)


        val factory = InjectorUtils.provideSettingsViewModelFactory()
        myViewModel = ViewModelProvider(this, factory).get(SettingsViewModel::class.java)

        val linearLayoutManager = LinearLayoutManager(this)
        blocked_list_recyclerView.layoutManager = linearLayoutManager

        var fakelist: ArrayList<String> = arrayListOf()
        fakelist.add("Suzy")
        fakelist.add("Jake")
        fakelist.add("Ashley")
        adapter = BlockedUsersAdapter(this@UnblockUserActivity, fakelist)
        blocked_list_recyclerView.adapter = adapter

    }




}