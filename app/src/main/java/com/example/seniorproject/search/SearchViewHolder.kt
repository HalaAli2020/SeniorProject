package com.example.seniorproject.search



import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.MainForum.Posts.CommunityPosts
import com.example.seniorproject.R
import com.example.seniorproject.data.models.CRN
import com.example.seniorproject.viewModels.SearchViewModel
import kotlinx.android.synthetic.main.rv_list.view.*
/* custom viewholder class to help bind each item in the recycler view and add additional functionality
* in this view holder we add the functionality for the subscribe button  */
class SearchViewHolder(parent: View) : RecyclerView.ViewHolder(parent) {
    fun bind(crn: CRN, mContext : Context, mViewModel: SearchViewModel) {
        val classes: String = crn.name
        itemView.communityName_TV.text = classes
        itemView.visibility = View.VISIBLE

        Log.d("subB", "ERROR: " + crn.subscribed)

        if (crn.subscribed) {
            Log.d("subB", "true")
            itemView.Subscibe_B.setBackgroundResource(R.drawable.sub_button)
            itemView.Subscibe_B.setTextColor(ContextCompat.getColor(mContext,R.color.white))
            itemView.Subscibe_B.text = mContext.resources.getString(R.string.Sub)
        } else {
            Log.d("subB", "false")
            itemView.Subscibe_B.setBackgroundResource(R.drawable.circle_border)
            itemView.Subscibe_B.setTextColor(ContextCompat.getColor(mContext,R.color.blue_theme))
            itemView.Subscibe_B.text = mContext.resources.getString(R.string.notSub)
        }

        itemView.Subscibe_B.setOnClickListener {
            Log.d("subB", "Button pressed")
            val sub = crn.subscribed
            if (sub) {
                Log.d("subB", "else")
                mViewModel.removeSub(crn.name)
                itemView.Subscibe_B.setBackgroundResource(R.drawable.circle_border)
                itemView.Subscibe_B.setTextColor(ContextCompat.getColor(mContext,R.color.blue_theme))
                itemView.Subscibe_B.text = mContext.resources.getString(R.string.notSub)
                crn.subscribed = false
            } else {
                Log.d("subB", "ifffffff")
                mViewModel.addSub(crn.name)
                itemView.Subscibe_B.setBackgroundResource(R.drawable.sub_button)
                itemView.Subscibe_B.setTextColor(ContextCompat.getColor(mContext,R.color.white))
                itemView.Subscibe_B.text = mContext.resources.getString(R.string.Sub)
                crn.subscribed = true
            }

        }




        itemView.communityName_TV.setOnClickListener {
            val intent = Intent(mContext, CommunityPosts::class.java)
            intent.putExtra("ClassName", classes)
            intent.flags = FLAG_ACTIVITY_NEW_TASK
            mContext.startActivity(intent)
        }
    }
}
