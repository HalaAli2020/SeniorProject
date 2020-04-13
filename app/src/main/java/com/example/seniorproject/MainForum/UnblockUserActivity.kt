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
import com.example.seniorproject.MainForum.Adapters.CommentsListAdapter
import com.example.seniorproject.MainForum.Adapters.PostAdapter
import com.example.seniorproject.R
import com.example.seniorproject.data.models.Post
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

                blocked_list_recyclerview.layoutManager = LinearLayoutManager(applicationContext)
                adapter = BlockedUsersAdapter(applicationContext, list)
                blocked_list_recyclerview.adapter = adapter

                refreshViewBlockedUsers.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(applicationContext, R.color.blue_theme))
                refreshViewBlockedUsers.setColorSchemeColors(ContextCompat.getColor(applicationContext, R.color.white))

                refreshViewBlockedUsers.setOnRefreshListener {
                    refreshViewBlockedUsers.isRefreshing = false
                    adapter = BlockedUsersAdapter(applicationContext, list)
                    blocked_list_recyclerview.adapter = adapter
                }
            }
        })
    }
}