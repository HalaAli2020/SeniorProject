package com.example.seniorproject.MainForum.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.MainForum.Posts.CommunityPosts
import com.example.seniorproject.R
import kotlinx.android.synthetic.main.rv_list.view.communityName_TV
import kotlinx.android.synthetic.main.rv_subs.view.*

class SubsriptionAdapter(context: Context, private val classList: MutableList<String>?) : RecyclerView.Adapter<ListHolder12>() {

    val mContext: Context = context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder12 {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.rv_subs, parent, false)

        return ListHolder12(cellForRow)
    }

    override fun getItemCount(): Int {
        return if(!classList.isNullOrEmpty())
            classList.size
        else {
            0
        }
    }

    override fun onBindViewHolder(holder: ListHolder12, position: Int) {

        val classes: String = classList!![position]
        holder.itemView.communityName_TV.text = classes


        holder.itemView.subsListCV.setOnClickListener {
            val intent = Intent(mContext, CommunityPosts::class.java)
            intent.putExtra("ClassName", classes)
            mContext.startActivity(intent)
        }

    }
}

class ListHolder12(v: View) : RecyclerView.ViewHolder(v)












