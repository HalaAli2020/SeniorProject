package com.example.seniorproject.MainForum

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.MainForum.Adapters.*
import com.example.seniorproject.MainForum.Posts.UpdatePost
import com.example.seniorproject.R
import com.example.seniorproject.Utils.ButtonClickListener
import com.example.seniorproject.Utils.EmailCallback
import com.example.seniorproject.Utils.ProfileButton
import com.example.seniorproject.Utils.SwipeHelper
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.viewModels.BlockedUserListCallback
import com.example.seniorproject.viewModels.CommunityPostViewModel
import com.example.seniorproject.viewModels.SettingsViewModel
import kotlinx.android.synthetic.main.activity_community_posts.*
import kotlinx.android.synthetic.main.activity_unblock_users.*
import kotlinx.android.synthetic.main.fragment_profile__post.view.*
import kotlinx.android.synthetic.main.rv_unblock_users.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class UnblockUserActivity: AppCompatActivity() {

    private lateinit var adapter: BlockedUsersAdapter
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: SettingsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unblock_users)

        DaggerAppComponent.create().inject(this)
        myViewModel = ViewModelProvider(this, factory).get(SettingsViewModel::class.java)

        myViewModel.getBlockedUsersList(object: BlockedUserListCallback{
            override fun onList(list: List<String>) {
                if (list.isEmpty()) {
                    val toast = Toast.makeText(applicationContext, "You have no blocked users", Toast.LENGTH_SHORT)
                    toast.show()
                } else {
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


                    object : SwipeHelper(this@UnblockUserActivity, blocked_list_recyclerview, 200) {
                            override fun initButton(
                                viewHolders: RecyclerView.ViewHolder,
                                buffer: MutableList<ProfileButton>
                            ) {
                                buffer.add(
                                    ProfileButton(this@UnblockUserActivity, "Unblock", 30, 0, Color.parseColor
                                        ("#FF0000"), object : ButtonClickListener {
                                        override fun onClick(pos: Int) {
                                            var username = " "
                                            //if statement to cover image post case
                                            if (adapter.getItemViewType(pos) == 1) {
                                                username = adapter.removeItem(viewHolders as PostImageViewHolders)
                                            } else if (adapter.getItemViewType(pos) == 0) {

                                                username = adapter.removeItem(viewHolders as CustomViewHolders)
                                            }

                                            //alert dialog setup
                                            val builder = AlertDialog.Builder(
                                                this@UnblockUserActivity,
                                                R.style.AppTheme_AlertDialog
                                            )
                                            //creating message to stop user from deleting posts on accident
                                            builder.setTitle("Are you sure?")
                                            builder.setMessage("this user will be unblocked")
                                            builder.setPositiveButton(
                                                "Unblock"
                                            ) { _: DialogInterface?, _: Int ->
                                                //call function
                                                myViewModel.unblockUser(username , object : EmailCallback {
                                                    override fun getEmail(string: String) {
                                                        finish()
                                                        intent = Intent(this@UnblockUserActivity, UnblockUserActivity::class.java)
                                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                                        startActivity(intent)
                                                        val toast = Toast.makeText(this@UnblockUserActivity, "This user is removed from your blocked list", Toast.LENGTH_SHORT)
                                                        toast.show()
                                                    }
                                                })
                                                /*GlobalScope.launch {
                                                   //put all of this in onsuccess
                                                    delay(200)

                                                }*/
                                            }
                                            builder.setNegativeButton(
                                                "CANCEL"
                                            ) { _: DialogInterface?, _: Int ->
                                                builder.setCancelable(true)
                                            }

                                            val msgdialog: AlertDialog = builder.create()

                                            msgdialog.window!!.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL)

                                            msgdialog.show()

                                        }

                                    })
                                )
                            }
                    }
                }}})
    }}