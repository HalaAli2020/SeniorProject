package com.example.seniorproject.MainForum.Posts

import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
//import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.MainForum.Adapters.CustomAdapter
import com.example.seniorproject.MainForum.Adapters.CustomViewHolders
import com.example.seniorproject.R
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.viewModels.CommunityPostViewModel
import kotlinx.android.synthetic.main.activity_community_posts.*
import javax.inject.Inject

class CommunityPosts : AppCompatActivity() {

    private lateinit var adapter: CustomAdapter

    var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#FF0000"))
    lateinit var deleteIcon: Drawable

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: CommunityPostViewModel
    lateinit var obse: Observer<in MutableList<Post>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_posts)

        val className = intent.getStringExtra("ClassName")

        post_list_community_name_TV.text = className

        val factory = InjectorUtils.provideCommunityPostViewModelFacotry()
        myViewModel = ViewModelProviders.of(this, factory).get(CommunityPostViewModel::class.java)


        classes_post_RV.layoutManager = LinearLayoutManager(this)

        adapter = CustomAdapter(this, myViewModel.returnClassPosts(className!!), 1)
        classes_post_RV.adapter = adapter


        refreshView.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.blue_theme))
        refreshView.setColorSchemeColors(ContextCompat.getColor(this, R.color.white))

        refreshView.setOnRefreshListener {
            refreshView.isRefreshing = false
            classes_post_RV.adapter = CustomAdapter(this, myViewModel.returnClassPosts(className!!), 1)
        }



        obse = Observer<MutableList<Post>> {
            Log.d("obser", " blah")

        }

        deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_delete_24px)!!


        val itemTouchHelperCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onSwiped(viewHolders: RecyclerView.ViewHolder, position: Int) {
                    val postkey: String? =
                        adapter.removeItem(viewHolders as CustomViewHolders, position)


                    var builder = AlertDialog.Builder(this@CommunityPosts, R.style.AppTheme_AlertDialog)

                    //.getStringExtra("Classkey")
                    //val postkey = intent.getStringExtra("author")
                    //myViewModel.deletePost(postkey!!, className)
                    //myViewModel.deletePost()
                    builder.setTitle("Are you sure?")
                    builder.setMessage("You cannot restore posts that have been deleted.")
                    builder.setPositiveButton("DELETE",
                        { dialogInterface: DialogInterface?, i: Int ->
                            myViewModel.deletePost(postkey!!, className)
                        })
                    builder.setNegativeButton("CANCEL",
                        { dialogInterface: DialogInterface?, i: Int ->
                            builder.setCancelable(true)
                        })

                    val msgdialog: AlertDialog = builder.create()

                    msgdialog.show()

                    //myViewModel.deletePost(postkey!!, className)
                    //classes_post_RV.removeViewAt(viewHolders.adapterPosition)
                    adapter.notifyItemRemoved(viewHolders.adapterPosition)
                    adapter.notifyItemRangeChanged(viewHolders.adapterPosition, adapter.itemCount)
                    classes_post_RV.adapter = adapter

                    //adapter.notifyDataSetChanged()
                    //adapter.notifyItemRangeRemoved(viewHolders.adapterPosition, 1)
                    /*adapter.notifyItemRangeChanged(viewHolders.adapterPosition, adapter.itemCount - viewHolders.adapterPosition+1)*/
                    //adapter.notifyDataSetChanged()

                    // adapter.notifyItemRangeRemoved(viewHolders.adapterPosition, 1)
                    // adapter.notifyItemRemoved(viewHolders.adapterPosition)
                    // adapter.notifyItemRangeChanged(viewHolders.adapterPosition, )
                    //  obse.onChanged(pos)

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

                /*override fun getMovementFlags(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ): Int {
                    return super.getMovementFlags(recyclerView, viewHolder)
                }*/

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)

        itemTouchHelper.attachToRecyclerView(classes_post_RV)

    }
    //myViewModel.listClasses?.observe(this, obse)


}





