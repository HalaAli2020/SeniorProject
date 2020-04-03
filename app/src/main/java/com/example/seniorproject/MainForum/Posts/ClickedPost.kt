package com.example.seniorproject.MainForum.Posts

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.MainForum.Adapters.CommentsAdapter
import com.example.seniorproject.R
import com.example.seniorproject.Utils.ButtonClickListener
import com.example.seniorproject.Utils.ProfileButton
import com.example.seniorproject.Utils.SwipeHelper
import com.example.seniorproject.databinding.ActivityClickedPostBinding
import com.example.seniorproject.viewModels.ClickedPostViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_clicked_post.*
import javax.inject.Inject

class ClickedPost : AppCompatActivity() {

    private lateinit var adapter: CommentsAdapter

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: ClickedPostViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clicked_post)

        //initilaization of dagger app componen
        DaggerAppComponent.create().inject(this)
        //initializing viewmodel using factory
        myViewModel = ViewModelProviders.of(this, factory).get(ClickedPostViewModel::class.java)
        //binding variable to bind viewmodel to activity clicked post xml file
        val binding: ActivityClickedPostBinding = DataBindingUtil.setContentView(this, R.layout.activity_clicked_post)

        //getting post information via intent
        val title: String = intent.getStringExtra("Title") ?: "no title"
        val text: String = intent.getStringExtra("Text") ?: "no text"
        val crn: String = intent.getStringExtra("subject") ?: "no subject"
        val author: String = intent.getStringExtra("Author") ?: "no author"
        val uri : String = intent.getStringExtra("uri") ?: " "
        val ptime: String = intent.getStringExtra("Ptime") ?: "no time"
        val uid: String = intent.getStringExtra("UserID") ?: "null"
        myViewModel.pKey = intent.getStringExtra("Pkey")
        myViewModel.classkey = intent.getStringExtra("Classkey")
        myViewModel.userID = uid
        myViewModel.title = title
        myViewModel.text = text
        myViewModel.crn = crn

        //add userid and send
        //observer for user comments
        myViewModel.commentsLiveList.observe(this, Observer {
            Log.d("Swap", "Swapping")
            swap(binding, title, text, author, crn, ptime, uri)
        })

        //setting adapter equal to the comments adapter
        adapter = CommentsAdapter(this, myViewModel.getComments(), title, text, author, crn,intent.getStringExtra("UserID").toString(), ptime, uri)
        comment_RecyclerView.adapter = adapter
        comment_RecyclerView.layoutManager = LinearLayoutManager(this)


        //updating viewmodel
        binding.clickedViewModel = myViewModel
        //setting lifecyler owner witlh current context
        binding.lifecycleOwner = this

        //setting refresh UI
        refreshView.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.blue_theme))
        refreshView.setColorSchemeColors(ContextCompat.getColor(this, R.color.white))

        //calling adapter again when the page is refreshed
        refreshView.setOnRefreshListener {
            comment_RecyclerView.adapter = CommentsAdapter(this, myViewModel.getComments(), title, text, author, crn,uid, ptime, uri)
            refreshView.isRefreshing = false
        }


           object : SwipeHelper(applicationContext, comment_RecyclerView, 200) {
                override fun initButton(
                    viewHolders: RecyclerView.ViewHolder,
                    buffer: MutableList<ProfileButton>
                ) {
                    val userk: String? = adapter.getUserKey(viewHolders)
                    if (FirebaseAuth.getInstance().currentUser?.uid != userk){
                        buffer.add(
                            ProfileButton(applicationContext, "Block User", 30, 0, Color.parseColor
                                ("#FF0000"), object : ButtonClickListener {
                                override fun onClick(pos: Int) {

                                    val userkey: String? =
                                        adapter.getUserKey(viewHolders)

                                    val builder = AlertDialog.Builder(
                                        this@ClickedPost,
                                        R.style.AppTheme_AlertDialog
                                    )

                                    builder.setTitle("Are you sure?")
                                    builder.setMessage("You won't see posts or comments from this user.")
                                    builder.setPositiveButton("BLOCK"
                                    ) { _: DialogInterface?, _: Int ->
                                        myViewModel.blockUser(userkey!!)
                                        val toast = Toast.makeText(
                                            this@ClickedPost,
                                            "This user has been blocked",
                                            Toast.LENGTH_SHORT
                                        )
                                        toast.show()
                                    }
                                    builder.setNegativeButton("CANCEL"
                                    ) { _: DialogInterface?, _: Int ->
                                        builder.setCancelable(true)
                                    }

                                    val msgdialog: AlertDialog = builder.create()

                                    msgdialog.window!!.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL)

                                    msgdialog.show()
                                }

                            })
                        )

                        buffer.add(
                            ProfileButton(applicationContext, "Report Post", 30, 0, Color.parseColor
                                ("#D3D3D3"), object : ButtonClickListener {
                                override fun onClick(pos: Int) {
                                    val comkey: String? =
                                        adapter.removeItem(viewHolders)

                                    val postkey: String? =
                                        adapter.getPostKey(viewHolders)

                                    val userkey: String? =
                                        adapter.getUserKey(viewHolders)

                                    val crnkey: String? =
                                        adapter.getCrn(viewHolders)

                                    val textkey: String? = adapter.getText(viewHolders)

                                    val builder = AlertDialog.Builder(
                                        this@ClickedPost,
                                        R.style.AppTheme_AlertDialog
                                    )

                                    val listreason = arrayOf(
                                        "This is spam",
                                        "This is abusive or harassing",
                                        "Other issues"
                                    )
                                    builder.setTitle("Report Post")
                                    builder.setSingleChoiceItems(
                                        listreason,
                                        0
                                    ) { dialogInterface, i ->
                                        //var complaint = listreason[i]
                                    }
                                    builder.setPositiveButton("SUBMIT"
                                    ) { _: DialogInterface?, _: Int ->

                                        val toast = Toast.makeText(
                                            this@ClickedPost,
                                            "We've received your report.",
                                            Toast.LENGTH_SHORT
                                        )
                                        toast.show()
                                        myViewModel.reportUserComment(
                                            userkey!!,
                                            textkey!!,
                                            crnkey!!,
                                            postkey!!, comkey!!
                                        )

                                    }
                                    builder.setNegativeButton("CANCEL"
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
            }
        }
    private fun swap(binding : ActivityClickedPostBinding, title : String, text : String, author : String, crn: String, time: String, uri : String)
    {
        val ada =  CommentsAdapter(this, myViewModel.getComments(), title, text, author, crn,intent.getStringExtra("UserID").toString(), time, uri)
        binding.commentRecyclerView.swapAdapter(ada, false)
    }


    }

