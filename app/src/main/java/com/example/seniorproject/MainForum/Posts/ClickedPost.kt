package com.example.seniorproject.MainForum.Posts

import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.example.seniorproject.R
import com.example.seniorproject.databinding.ActivityClickedPostBinding
import com.example.seniorproject.viewModels.ClickedPostViewModel
import kotlinx.android.synthetic.main.activity_clicked_post.*
import javax.inject.Inject

class ClickedPost : AppCompatActivity() {

    private lateinit var adapter: CommentsAdapter
    var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#FF0000"))
    lateinit var deleteIcon: Drawable

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


        refreshView.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.blue_theme))
        refreshView.setColorSchemeColors(ContextCompat.getColor(this, R.color.white))

        refreshView.setOnRefreshListener {
            comment_RecyclerView.adapter = CommentsAdapter(this, myViewModel.getComments(), title, text, author, crn)
            refreshView.isRefreshing = false
        }


        deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_delete_24px)!!

        val itemTouchHelperCallback = object: ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT )
        {
            override fun onSwiped(viewHolders: RecyclerView.ViewHolder, position: Int) {
                //val commentkey: String? =adapter.removeItem(viewHolders as CommentsAdapter.CustomViewHoldersHeader, position)
                val commentkey: String? =adapter.removeItem(viewHolders as CommentsAdapter.CustomViewHolders, position)
              //  val commentkeyheader: String? =adapter.removeItemHead(viewHolders as , position)

                var builder = AlertDialog.Builder(this@ClickedPost, R.style.AppTheme_AlertDialog)

                //.getStringExtra("Classkey")
                //val postkey = intent.getStringExtra("author")
                //myViewModel.deletePost(postkey!!, className)
                //myViewModel.deletePost()
                builder.setTitle("Are you sure?")
                builder.setMessage("You cannot restore comments that have been deleted.")
                builder.setPositiveButton("DELETE",
                    { dialogInterface: DialogInterface?, i: Int ->
                        myViewModel.deleteComment(intent.getStringExtra("Classkey"), intent.getStringExtra("crn"),commentkey!!)
                    })
                builder.setNegativeButton("CANCEL",
                    { dialogInterface: DialogInterface?, i: Int ->
                        builder.setCancelable(true)
                    })

                val msgdialog: AlertDialog = builder.create()

                msgdialog.show()

                adapter.notifyItemRemoved(viewHolders.adapterPosition)
                adapter.notifyItemRangeChanged(viewHolders.adapterPosition, adapter.itemCount)
                comment_RecyclerView.adapter = adapter
                //adapter.removeItem(viewHolders as CustomViewHolders)
                //myViewModel.deleteComment(intent.getStringExtra("Classkey"), intent.getStringExtra("crn"),commentkey!!)
              //  myViewModel.deleteComment(intent.getStringExtra("Classkey"), intent.getStringExtra("crn"),commentkeyheader!!)

            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val iconMargin = (itemView.height - deleteIcon.intrinsicHeight)/2

                if(dX < 0) {
                    swipeBackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                    deleteIcon.setBounds(itemView.right - iconMargin - deleteIcon.intrinsicWidth, itemView.top + iconMargin,
                        itemView.right - iconMargin, itemView.bottom - iconMargin)
                }

                swipeBackground.draw(c)
                deleteIcon.draw(c)

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
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
