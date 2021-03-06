package com.example.seniorproject.MainForum.Posts

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.MainForum.Adapters.CommentsListAdapter
import com.example.seniorproject.R
import com.example.seniorproject.Utils.ButtonClickListener
import com.example.seniorproject.Utils.CheckCallback
import com.example.seniorproject.Utils.ProfileButton
import com.example.seniorproject.Utils.SwipeHelper
import com.example.seniorproject.data.interfaces.CommentListFromFlow
import com.example.seniorproject.data.interfaces.FirebaseCallbackNoComments
import com.example.seniorproject.data.models.Comment
import com.example.seniorproject.databinding.ActivityClickedPostBinding
import com.example.seniorproject.viewModels.ClickedPostViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_clicked_post.*
import javax.inject.Inject

class ClickedPost : AppCompatActivity() {

    private lateinit var nocommadapter: CommentsListAdapter
    private lateinit var adapter: CommentsListAdapter

    var clickedscreen=1
    var isLoading = false
    val limit =10

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: ClickedPostViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clicked_post)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //initializing dagger app component and binding variable
        DaggerAppComponent.create().inject(this)

        val binding: ActivityClickedPostBinding = DataBindingUtil.setContentView(this, R.layout.activity_clicked_post)
        myViewModel = ViewModelProvider(this, factory).get(ClickedPostViewModel::class.java)
        //binded varibles and function can be found in the activity_clicked_post xml file
        //currently there are no binded variables in use by this activity however we may introduce them later so we have left the binding variable

        //getting information from the previous activity and viewmodel to fill in information
        val title: String = intent.getStringExtra("Title") ?: "no title"
        val text: String = intent.getStringExtra("Text") ?: "no text"
        val crn: String = intent.getStringExtra("subject") ?: "no subject"
        val author: String = intent.getStringExtra("Author") ?: "no author"
        val uri: String = intent.getStringExtra("uri") ?: "null"
        val ptime: String = intent.getStringExtra("Ptime") ?: "no time"
        val uid: String = intent.getStringExtra("UserID") ?: "null"
        myViewModel.pKey = intent.getStringExtra("Pkey") ?: " "
        myViewModel.classkey = intent.getStringExtra("Classkey") ?: " "
        myViewModel.userID = uid
        myViewModel.title = title
        myViewModel.text = text
        myViewModel.crn = crn

        var endList:Boolean = false

        //checks to see if there are no comments in a new post that was created. If no comments then enter code in onEmpty
        var checkForComments = myViewModel.noCommentsCheckForCommPosts(object :
            FirebaseCallbackNoComments {
            override fun onEmpty(nocomlist: Boolean) {
                if(nocomlist){
                    //load adapter with empty comment for recyclerview layout to load
                    val comment = Comment("no comment", "", "", "", "")
                    val comments: ArrayList<Comment> = arrayListOf()
                    comments.add(comment)
                    nocommadapter = CommentsListAdapter(
                        this@ClickedPost, comments, title, text, author, crn,
                        intent.getStringExtra("UserID").toString(), ptime, uri
                    )
                    comment_RecyclerView.adapter = nocommadapter
                    comment_RecyclerView.layoutManager = LinearLayoutManager(this@ClickedPost)
                    Comment_button.setOnClickListener {
                        myViewModel.checkSubscriptions(crn, object : CheckCallback {
                            override fun check(chk: Boolean) {
                                /*check varible checks is user is subscribed, the logic for this can be found in the viewmodel
                                and the database query can be found in the corresponding checksubscription firebase function
                                 */
                                if (Comment_textbox.text.isNotBlank() && chk) {
                                    myViewModel.newComment(Comment_textbox.text.toString())
                                    Toast.makeText(this@ClickedPost, "Your comment has been successfully posted!", Toast.LENGTH_LONG).show()
                                    Comment_textbox.text.clear()
                                    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                    inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
                                    myViewModel.getClassComments(object :
                                        CommentListFromFlow {
                                        override fun onList(list: List<Comment>) {
                                            for (item in list) {
                                                val getext = item.text
                                                Log.d("soupview", "comm text is $getext")
                                            }
                                            adapter = CommentsListAdapter(
                                                this@ClickedPost, list, title, text, author, crn,
                                                intent.getStringExtra("UserID").toString(), ptime, uri
                                            )
                                            comment_RecyclerView.adapter = adapter
                                            comment_RecyclerView.layoutManager = LinearLayoutManager(this@ClickedPost)

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
                                                            ProfileButton(applicationContext, "Block", 45, 0, Color.parseColor
                                                                ("#FF0000"), object : ButtonClickListener {
                                                                override fun onClick(pos: Int) {
                                                                    Log.d("soupv", "pos is $pos")
                                                                    //userkey is collected from the recyclerview for the block user functionality
                                                                    val authorkey: String? = adapter.getAuth(viewHolders)
                                                                    val userkey: String? = adapter.getUserKey(viewHolders)
                                                                    val builder = AlertDialog.Builder(
                                                                        this@ClickedPost,
                                                                        R.style.AppTheme_AlertDialog
                                                                    )
                                                                    //building the dialog box to stop users from blocking people by mistake
                                                                    builder.setTitle("Are you sure?")
                                                                    builder.setMessage("You won't see posts or comments from this user.")
                                                                    builder.setPositiveButton("BLOCK"
                                                                    ) { _: DialogInterface?, _: Int ->
                                                                        myViewModel.blockUser(authorkey!!)
                                                                        //comment uid is still being stored, not comments author?
                                                                        val toast = Toast.makeText(
                                                                            this@ClickedPost,
                                                                            "This user has been blocked",
                                                                            Toast.LENGTH_SHORT
                                                                        )
                                                                        toast.show()
                                                                        onBackPressed(crn)
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
                                                            ProfileButton(applicationContext, "Report Comment", 45, 0, Color.parseColor
                                                                ("#2b99fd"), object : ButtonClickListener {
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
                                                                    builder.setPositiveButton(
                                                                        "SUBMIT"
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
                                            }
                                        }

                                    })
                                } else if (Comment_textbox.text.isNullOrBlank()) {
                                    Toast.makeText(this@ClickedPost, "you cannot post an empty comment", Toast.LENGTH_LONG).show()
                                } else if (Comment_textbox.text.isNotBlank() && !chk) {
                                    Toast.makeText(this@ClickedPost, "Subscribe to $crn in order to create a post", Toast.LENGTH_SHORT).show()
                                }
                            }
                        })


                    }
                }
                else{
                    //this means that there are comments already present in the post. don't have to load with an empty adapter to recyclerview
                    myViewModel.getClassComments(object: CommentListFromFlow{
                        override fun onList(list: List<Comment>) {

                            if(list.size<=11){ //load functionality not enabled because list of comments is too small
                                adapter = CommentsListAdapter(
                                    this@ClickedPost,
                                    list,
                                    title,
                                    text,
                                    author,
                                    crn,
                                    intent.getStringExtra("UserID").toString(),
                                    ptime,
                                    uri
                                )
                                val linearLayoutManager = LinearLayoutManager(this@ClickedPost)
                                comment_RecyclerView.adapter = adapter
                                comment_RecyclerView.layoutManager = linearLayoutManager
                                load.visibility=View.GONE
                            }
                            else{ //load functionality begins here
                                var lastpositionviewed=11


                                //if a post has over 10 comments, only show the latest 10 comments. make user hit load button to load more comments
                                adapter = CommentsListAdapter(this@ClickedPost, list.subList(0,lastpositionviewed), title, text, author, crn,
                                    intent.getStringExtra("UserID").toString(), ptime, uri)
                                val linearLayoutManager = LinearLayoutManager(this@ClickedPost)
                                comment_RecyclerView.adapter = adapter
                                comment_RecyclerView.layoutManager = linearLayoutManager



                                comment_RecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){

                                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                                        if (dy < 0)
                                            load.visibility=View.GONE

                                        else if(linearLayoutManager.findLastVisibleItemPosition() == linearLayoutManager.itemCount-1 && !endList)
                                            load.visibility=View.VISIBLE


                                        super.onScrolled(recyclerView, dx, dy)
                                    }
                                })

                                if((list.size%10<10 || list.size%10==0) && list.size>10){ //if list is 26 or list is 50, all applies
                                    var lastload=list.size%10 //returns modulus: ex 26%10=6
                                    var count= list.size/10
                                    Log.d("soupv", "load button clicked $count times")
                                    for(i in 0..count){
                                        load.setOnClickListener {
                                            if(list.size < (lastpositionviewed+10)){ //if list is in teen values it can't be loading 10 comments,
                                                //so just display the remainder of the list
                                                adapter = CommentsListAdapter(
                                                    this@ClickedPost, list, title, text, author, crn,
                                                    intent.getStringExtra("UserID").toString(), ptime, uri
                                                )
                                                val linearLayoutManager = LinearLayoutManager(this@ClickedPost)
                                                comment_RecyclerView.adapter = adapter
                                                comment_RecyclerView.layoutManager = linearLayoutManager
                                                comment_RecyclerView.scrollToPosition(list.size-1) //scroll to bottom of list
                                                count--
                                            }else{ //if list is in the 20s 50s 90s 1000s, load 10 comments at a time
                                                adapter = CommentsListAdapter(
                                                    this@ClickedPost, list.subList(0,lastpositionviewed+10), title, text, author, crn,
                                                    intent.getStringExtra("UserID").toString(), ptime, uri
                                                )
                                                lastpositionviewed += 10
                                                val linearLayoutManager = LinearLayoutManager(this@ClickedPost)
                                                comment_RecyclerView.adapter = adapter
                                                comment_RecyclerView.layoutManager = linearLayoutManager
                                                comment_RecyclerView.scrollToPosition(list.size/2) //scroll to center of list
                                                count--
                                            }

                                            if (count == 0) {
                                                //lets user know that no more comments can be viewed because they are all present
                                                Toast.makeText(this@ClickedPost, "All comments loaded.", Toast.LENGTH_LONG).show()
                                                load.visibility=View.GONE
                                                endList = true
                                            }
                                        }
                                    }

                                }
                            }



                            Comment_button.setOnClickListener {
                                myViewModel.checkSubscriptions(crn, object : CheckCallback {
                                    override fun check(chk: Boolean) {
                                        /*check varible checks is user is subscribed, the logic for this can be found in the viewmodel
                                        and the database query can be found in the corresponding checksubscription firebase function
                                         */
                                        if (Comment_textbox.text.isNotBlank() && chk) {
                                            myViewModel.newComment(Comment_textbox.text.toString())
                                            Toast.makeText(this@ClickedPost, "Your comment has been successfully posted!", Toast.LENGTH_LONG)
                                                .show()
                                            Comment_textbox.text.clear()
                                            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
                                        } else if (Comment_textbox.text.isNullOrBlank()) {
                                            Toast.makeText(this@ClickedPost, "you cannot post an empty comment", Toast.LENGTH_SHORT).show()
                                        } else if (Comment_textbox.text.isNotBlank() && !chk) {
                                            Toast.makeText(this@ClickedPost, "Subscribe to $crn in order to create a post", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                    }
                                })


                            }

                            //on swipe a user can block or report another user
                            object : SwipeHelper(applicationContext, comment_RecyclerView, 200) {
                                override fun initButton(
                                    viewHolders: RecyclerView.ViewHolder,
                                    buffer: MutableList<ProfileButton>
                                ) {
                                    val userk: String? = adapter.getUserKey(viewHolders)
                                    if (FirebaseAuth.getInstance().currentUser?.uid == userk) {
                                        //a user cannot block or report themselves
                                    } else {
                                        buffer.add(
                                            ProfileButton(applicationContext, "Block", 45, 0, Color.parseColor
                                                ("#FF0000"), object : ButtonClickListener {
                                                override fun onClick(pos: Int) {
                                                    //userkey is collected from the recyclerview for the block user functionality
                                                    val userkey: String? =
                                                        adapter.getAuth(viewHolders)

                                                    val builder = AlertDialog.Builder(
                                                        this@ClickedPost,
                                                        R.style.AppTheme_AlertDialog
                                                    )
                                                    //building the dialog box to stop users from blocking people by mistake
                                                    builder.setTitle("Are you sure?")
                                                    builder.setMessage("You won't see posts or comments from this user.")
                                                    builder.setPositiveButton(
                                                        "BLOCK"
                                                    ) { _: DialogInterface?, _: Int ->
                                                        myViewModel.blockUser(userkey!!)
                                                        val toast = Toast.makeText(
                                                            this@ClickedPost,
                                                            "This user has been blocked",
                                                            Toast.LENGTH_SHORT
                                                        )
                                                        //takes user to an updated community posts page and displays successful bloakc toast message
                                                        toast.show()
                                                        onBackPressed(crn)
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

                                        buffer.add(
                                            ProfileButton(applicationContext, "Report", 45, 0, Color.parseColor
                                                ("#2b99fd"), object : ButtonClickListener {
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
                                                    builder.setPositiveButton(
                                                        "SUBMIT"
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
                                                        //report comment and show toast message
                                                    }
                                                    builder.setNegativeButton(
                                                        "CANCEL"
                                                    ) { _: DialogInterface?, _: Int ->
                                                        builder.setCancelable(true)
                                                        //close dialog if user chooses cancel
                                                    }

                                                    //create and show dialog box
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

        //sets data binding variable in xml to this view model
        binding.clickedViewModel = myViewModel
        binding.lifecycleOwner = this@ClickedPost
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun onBackPressed(string: String) {
        val intent = Intent(this@ClickedPost, CommunityPosts::class.java)
        intent.putExtra("ClassName", string)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }


}