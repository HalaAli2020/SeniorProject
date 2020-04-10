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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.MainForum.Adapters.CommentsListAdapter
import com.example.seniorproject.R
import com.example.seniorproject.Utils.ButtonClickListener
import com.example.seniorproject.Utils.ProfileButton
import com.example.seniorproject.Utils.SwipeHelper
import com.example.seniorproject.data.models.Comment
import com.example.seniorproject.data.repositories.PostRepository
import com.example.seniorproject.databinding.ActivityClickedPostBinding
import com.example.seniorproject.viewModels.ClickedPostViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_clicked_post.*
import javax.inject.Inject

class ClickedPost : AppCompatActivity() {

    private lateinit var nocommadapter: CommentsListAdapter
    private lateinit var adapter: CommentsListAdapter

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: ClickedPostViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clicked_post)

        //initializing dagger app component and binding variable
        DaggerAppComponent.create().inject(this)
        myViewModel = ViewModelProviders.of(this, factory).get(ClickedPostViewModel::class.java)
        //binded varibles and function can be found in the activity_clicked_post xml file
        val binding: ActivityClickedPostBinding = DataBindingUtil.setContentView(this, R.layout.activity_clicked_post)

        //getting information from the previous activity and viewmodel to fill in information
        val title: String = intent.getStringExtra("Title") ?: "no title"
        val text: String = intent.getStringExtra("Text") ?: "no text"
        val crn: String = intent.getStringExtra("subject") ?: "no subject"
        val author: String = intent.getStringExtra("Author") ?: "no author"
        val uri : String = intent.getStringExtra("uri") ?: "null"
        val ptime: String = intent.getStringExtra("Ptime") ?: "no time"
        val uid: String = intent.getStringExtra("UserID") ?: "null"
        myViewModel.pKey = intent.getStringExtra("Pkey") ?: " "
        myViewModel.classkey = intent.getStringExtra("Classkey") ?: " "
        myViewModel.userID = uid
        myViewModel.title = title
        myViewModel.text = text
        myViewModel.crn = crn


        //checking for comments and adding a no comments message when there are no comments
        var checkForComments = myViewModel.noCommentsCheckForCommPosts(object:
            PostRepository.FirebaseCallbackNoComments{
            override fun onEmpty(nocomlist: Boolean) {
                Log.d("soupview", "welcome to inside noCommentChecker")
                if(nocomlist == true){
                    Log.d("soupview", "comments don't exist")
                    var comment = Comment("no comment", "", "", "", "")
                    val Comments = MutableList(1) { index -> comment}
                    nocommadapter = CommentsListAdapter(this@ClickedPost, Comments, title, text, author, crn,
                       uid, ptime, uri)
                    comment_RecyclerView.adapter = nocommadapter
                    comment_RecyclerView.layoutManager = LinearLayoutManager(this@ClickedPost)
                }
            }

            override fun onFull(comlistpresent: Boolean) {
                if(comlistpresent == false){
                    var commlist = myViewModel.getClassComments(object: ClickedPostViewModel.CommentListFromFlow{
                        override fun onList(list: List<Comment>) {
                            for(item in list){
                                val getext= item.text
                                Log.d("soupview", "comm text is $getext")
                            }

                            adapter = CommentsListAdapter(this@ClickedPost, list, title, text, author, crn,
                                intent.getStringExtra("UserID").toString(), ptime, uri)
                            comment_RecyclerView.adapter = adapter
                            comment_RecyclerView.layoutManager = LinearLayoutManager(this@ClickedPost)

                            //setting up refreshview UI
                            refreshView.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this@ClickedPost, R.color.blue_theme))
                            refreshView.setColorSchemeColors(ContextCompat.getColor(this@ClickedPost, R.color.white))

                            //created refreshview
                            refreshView.setOnRefreshListener {
                                comment_RecyclerView.adapter = CommentsListAdapter(this@ClickedPost, list, title, text, author, crn,uid, ptime, uri)
                                refreshView.isRefreshing = false
                            }


//on swipe a user can block or report another user
                            object : SwipeHelper(applicationContext, comment_RecyclerView, 200) {
                                override fun initButton(
                                    viewHolders: RecyclerView.ViewHolder,
                                    buffer: MutableList<ProfileButton>
                                ) {
                                    val userk: String? = adapter.getUserKey(viewHolders)
                                    if (FirebaseAuth.getInstance().currentUser?.uid == userk){
                                        //a user cannot block or report themselves
                                    }
                                    else {
                                        buffer.add(
                                            ProfileButton(applicationContext, "Block User", 30, 0, Color.parseColor
                                                ("#FF0000"), object : ButtonClickListener {
                                                override fun onClick(pos: Int) {
                                                    Log.d("soupv", "pos is $pos")
                                                    //userkey is collected from the recyclerview for the block user functionality
                                                     val userkey: String? =
                                                         adapter.getUserKey(viewHolders)

                                                    val builder = AlertDialog.Builder(
                                                        this@ClickedPost,
                                                        R.style.AppTheme_AlertDialog
                                                    )
                                                    //building the dialog box to stop users from blocking people by mistake
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
                                                    /*the following information is collected from the recyclerview for the report post
                                                    functionality
                                                     */
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
                                                    //user can chose to report a user for these reasons
                                                    //creating report user dialog box
                                                    builder.setTitle("Report Post")
                                                    builder.setSingleChoiceItems(
                                                        listreason,
                                                        0
                                                    ) { dialogInterface, i ->
                                                        var complaint = listreason[i]
                                                    }
                                                    builder.setPositiveButton("SUBMIT"
                                                    ) { _: DialogInterface?, _: Int ->
                                                        //letting user know that the report was successfully sent
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
                    })
                }
            }
        })

        binding.clickedViewModel = myViewModel
        binding.lifecycleOwner = this@ClickedPost
    }
}