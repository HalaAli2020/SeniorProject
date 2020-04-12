package com.example.seniorproject.MainForum

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.MainForum.Adapters.BlockedUsersAdapter
import com.example.seniorproject.MainForum.Adapters.PostAdapter
import com.example.seniorproject.R
import com.example.seniorproject.viewModels.BlockedUserListCallback
import com.example.seniorproject.viewModels.CommunityPostViewModel
import com.example.seniorproject.viewModels.SettingsViewModel
import kotlinx.android.synthetic.main.activity_community_posts.*
import kotlinx.android.synthetic.main.activity_unblock_users.*
import kotlinx.android.synthetic.main.rv_unblock_users.*
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

        myViewModel.getBlockedUsersList(object: BlockedUserListCallback{
            override fun onList(list: List<String>) {
               /* refresh doesn't work here
                refreshView.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(applicationContext, R.color.blue_theme))
                refreshView.setColorSchemeColors(ContextCompat.getColor(applicationContext, R.color.white))

                refreshView.setOnRefreshListener {
                    refreshView.isRefreshing = false
                    adapter = BlockedUsersAdapter(applicationContext, list)
                    blocked_list_recyclerView.adapter = adapter
                }*/

                for(item in list){
                    Log.d("soupv", "blocked users are $item")
                }
                adapter = BlockedUsersAdapter(applicationContext, list)
                blocked_list_recyclerView.adapter = adapter
                blocked_list_recyclerView.layoutManager = LinearLayoutManager(applicationContext)

            }
        })
    }




}