package com.example.seniorproject.MainForum

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.R
import com.example.seniorproject.Utils.PostListener
import com.example.seniorproject.data.models.CommentLive
import com.example.seniorproject.MainForum.CommentsAdapter.CustomViewHoldersHeader
import com.example.seniorproject.databinding.ActivityClickedPostBinding
import com.example.seniorproject.databinding.ActivityLoginBinding
import com.example.seniorproject.databinding.FragmentHomeBinding
import com.example.seniorproject.viewModels.AuthenticationViewModel
import com.example.seniorproject.viewModels.ClickedPostViewModel
import com.example.seniorproject.viewModels.HomeFragmentViewModel
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_clicked_post.*
import kotlinx.android.synthetic.main.activity_clicked_post.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
        val crn: String = intent.getStringExtra("crn")
        val author: String = intent.getStringExtra("Author")

        myViewModel.PKey = intent.getStringExtra("Pkey")
        myViewModel.Classkey = intent.getStringExtra("Classkey")
        myViewModel.UserID = intent.getStringExtra("UserID")
        myViewModel.title = title
        myViewModel.text = text
        myViewModel.crn = crn

        adapter = CommentsAdapter(this, myViewModel.getComments(), title, text, author, crn)
        comment_RecyclerView.adapter = adapter
        comment_RecyclerView.layoutManager = LinearLayoutManager(this)

        binding.clickedViewModel = myViewModel
        binding.lifecycleOwner = this

        val itemTouchHelperCallback = object: ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT )
        {
            override fun onSwiped(viewHolders: RecyclerView.ViewHolder, position: Int) {
                //val commentkey: String? =adapter.removeItem(viewHolders as CommentsAdapter.CustomViewHoldersHeader, position)
                val commentkey: String? =adapter.removeItem(viewHolders as CommentsAdapter.CustomViewHolders, position)
              //  val commentkeyheader: String? =adapter.removeItemHead(viewHolders as , position)


                //adapter.removeItem(viewHolders as CustomViewHolders)
                myViewModel.deleteComment(intent.getStringExtra("Classkey"), intent.getStringExtra("crn"),commentkey!!)
              //  myViewModel.deleteComment(intent.getStringExtra("Classkey"), intent.getStringExtra("crn"),commentkeyheader!!)

            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(comment_RecyclerView)


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
}
