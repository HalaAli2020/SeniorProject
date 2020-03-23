package com.example.seniorproject.MainForum.Posts

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.MainForum.Adapters.CommentsAdapter
import com.example.seniorproject.MainForum.Adapters.CustomViewHolders
import com.example.seniorproject.R
import com.example.seniorproject.Utils.ButtonClickListener
import com.example.seniorproject.Utils.ProfileButton
import com.example.seniorproject.Utils.SwipeHelper
import com.example.seniorproject.databinding.ActivityClickedPostBinding
import com.example.seniorproject.viewModels.ClickedPostViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_clicked_post.*
import kotlinx.android.synthetic.main.activity_clicked_post.refreshView
import kotlinx.android.synthetic.main.activity_community_posts.*
import javax.inject.Inject

class ClickedPost : AppCompatActivity() {

    private lateinit var adapter: CommentsAdapter

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: ClickedPostViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clicked_post)

        DaggerAppComponent.create().inject(this)
        myViewModel = ViewModelProviders.of(this, factory).get(ClickedPostViewModel::class.java)
        val binding: ActivityClickedPostBinding = DataBindingUtil.setContentView(this, R.layout.activity_clicked_post)

        val title: String = intent.getStringExtra("Title")
        val text: String = intent.getStringExtra("Text")
        val crn: String = intent.getStringExtra("subject")
        val author: String = intent.getStringExtra("Author")
        val uri : String = intent.getStringExtra("uri") ?: ""
        val time: String = intent.getStringExtra("Ptime")

        myViewModel.PKey = intent.getStringExtra("Pkey")
        myViewModel.Classkey = intent.getStringExtra("Classkey")
        myViewModel.UserID = intent.getStringExtra("UserID")
        myViewModel.title = title
        myViewModel.text = text
        myViewModel.crn = crn

        //add userid and send

        adapter = CommentsAdapter(this, myViewModel.getComments(), title, text, author, crn,intent.getStringExtra("UserID").toString(), time, uri)
        comment_RecyclerView.adapter = adapter
        comment_RecyclerView.layoutManager = LinearLayoutManager(this)


        binding.clickedViewModel = myViewModel
        binding.lifecycleOwner = this

        refreshView.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.blue_theme))
        refreshView.setColorSchemeColors(ContextCompat.getColor(this, R.color.white))

        refreshView.setOnRefreshListener {
            comment_RecyclerView.adapter = CommentsAdapter(this, myViewModel.getComments(), title, text, author, crn,intent.getStringExtra("UserID").toString(), time, uri)
            refreshView.isRefreshing = false
        }

        fun showToast(){
            var toast= Toast.makeText(this@ClickedPost, "We've received your report.", Toast.LENGTH_SHORT)
            toast.show()
        }

            val swipe = object : SwipeHelper(applicationContext, comment_RecyclerView, 200) {
                override fun initButton(
                    viewHolders: RecyclerView.ViewHolder,
                    buffer: MutableList<ProfileButton>
                ) {
                    val userk: String? = adapter.getUserKey(viewHolders)
                    if (FirebaseAuth.getInstance().currentUser?.uid == userk){
                        val swipe = null
                    }
                    else {
                        buffer.add(
                            ProfileButton(applicationContext, "Report Post", 30, 0, Color.parseColor
                                ("#FF0000"), object : ButtonClickListener {
                                override fun onClick(pos: Int) {
                                    val comkey: String? =
                                        adapter.removeItem(viewHolders)

                                    val postkey: String? =
                                        adapter.getPostKey(viewHolders)

                                    val userkey: String? =
                                        adapter.getUserKey(viewHolders)

                                    val crnkey: String? =
                                        adapter.getCrn(viewHolders, pos)

                                    val textkey: String? = adapter.getText(viewHolders)

                                    var builder = AlertDialog.Builder(
                                        this@ClickedPost,
                                        R.style.AppTheme_AlertDialog
                                    )

                                    var listreason = arrayOf(
                                        "This is spam",
                                        "This is abusive or harassing",
                                        "Other issues"
                                    )
                                    //.getStringExtra("Classkey")
                                    //val postkey = intent.getStringExtra("author")
                                    //myViewModel.deletePost(postkey!!, className)
                                    //myViewModel.deletePost()
                                    builder.setTitle("Report Post")
                                    builder.setSingleChoiceItems(
                                        listreason,
                                        0
                                    ) { dialogInterface, i ->
                                        var complaint = listreason[i]
                                    }
                                    builder.setPositiveButton("SUBMIT",
                                        { dialogInterface: DialogInterface?, i: Int ->
                                            /*if(listreason[i] ==null){
                                            var toast= Toast.makeText(this@CommunityPosts, "Please"+pos,Toast.LENGTH_SHORT)
                                            toast.show()
                                        }*/
                                            var toast = Toast.makeText(
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

                                        })
                                    builder.setNegativeButton("CANCEL",
                                        { dialogInterface: DialogInterface?, i: Int ->
                                            builder.setCancelable(true)
                                        })

                                    val msgdialog: AlertDialog = builder.create()

                                    msgdialog.getWindow()!!.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL)

                                    msgdialog.show()
                                }

                            })
                        )

                        buffer.add(
                            ProfileButton(applicationContext, "Block User", 30, 0, Color.parseColor
                                ("#D3D3D3"), object : ButtonClickListener {
                                override fun onClick(pos: Int) {
                                    val postkey: String? =
                                        adapter.removeItem(viewHolders)

                                    val userkey: String? =
                                        adapter.getUserKey(viewHolders)

                                    val crnkey: String? =
                                        adapter.getCrn(viewHolders, pos)

                                    //var builder = AlertDialog.Builder(activity!!.baseContext, R.style.AppTheme_AlertDialog)
                                    var builder = AlertDialog.Builder(
                                        this@ClickedPost,
                                        R.style.AppTheme_AlertDialog
                                    )

                                    //.getStringExtra("Classkey")
                                    //val postkey = intent.getStringExtra("author")
                                    //myViewModel.deletePost(postkey!!, className)
                                    //myViewModel.deletePost()
                                    builder.setTitle("Are you sure?")
                                    builder.setMessage("You won't see posts or comments from this user.")
                                    builder.setPositiveButton("BLOCK",
                                        { dialogInterface: DialogInterface?, i: Int ->
                                            //myViewModel.blockPost(userkey!!, crnkey!!, postkey!!)
                                            var toast = Toast.makeText(
                                                this@ClickedPost,
                                                "This user has been blocked",
                                                Toast.LENGTH_SHORT
                                            )
                                            toast.show()
                                        })
                                    builder.setNegativeButton("CANCEL",
                                        { dialogInterface: DialogInterface?, i: Int ->
                                            builder.setCancelable(true)
                                        })

                                    val msgdialog: AlertDialog = builder.create()

                                    msgdialog.getWindow()!!.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL)

                                    msgdialog.show()
                                }

                            })
                        )

                    }

                }
            }
        }



    }

    /* coroutines attempt init {
         lifecycleScope.launch{
             myViewModel.getComments()
         }
     }*/
    /*Log.d("postkey", intent?.getStringExtra("Pkey"))
    Log.d("Pkey", myViewModel.PKey!!)
    click_post_title.text = intent.getStringExtra("Title")
    click_post_text.text = intent.getStringExtra("Text")
    CKEY is for class key
    Comments = myViewModel.getComments(bundle?.getString("CKey")!!)*/
