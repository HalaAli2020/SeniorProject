package com.example.seniorproject.MainForum.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.MainForum.Posts.CommunityPosts
import com.example.seniorproject.R
import com.example.seniorproject.data.models.CRN
import com.example.seniorproject.viewModels.ListViewModel
import kotlinx.android.synthetic.main.rv_list.view.*

class ListAdapter(
    context: Context,
    private val classList: MutableList<CRN>,
    private val mViewModel: ListViewModel
) :
    RecyclerView.Adapter<ListHolder>() {

    val mContext: Context = context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.rv_list, parent, false)

        return ListHolder(cellForRow)
    }

    override fun getItemCount(): Int {

            return classList.size

    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {

        val classes: String = classList[position].name
        holder.itemView.communityName_TV.text = classes

        Log.d("subB", "ERROR: " + classList[position].subscribed)

        if (classList[position].subscribed) {
            Log.d("subB", "true")
            holder.itemView.Subscibe_B.setBackgroundResource(R.drawable.sub_button)
            holder.itemView.Subscibe_B.setTextColor(ContextCompat.getColor(mContext,R.color.white))
            holder.itemView.Subscibe_B.text = mContext.resources.getString(R.string.Sub)
        } else {
            Log.d("subB", "false")
            holder.itemView.Subscibe_B.setBackgroundResource(R.drawable.circle_border)
            holder.itemView.Subscibe_B.setTextColor(ContextCompat.getColor(mContext,R.color.blue_theme))
            holder.itemView.Subscibe_B.text = mContext.resources.getString(R.string.notSub)
        }

        holder.itemView.Subscibe_B.setOnClickListener {
            Log.d("subB", "Button pressed")
            val sub = classList[position].subscribed
            if (sub) {
                Log.d("subB", "else")
                mViewModel.removeSub(classList[position].name)
                holder.itemView.Subscibe_B.setBackgroundResource(R.drawable.circle_border)
                holder.itemView.Subscibe_B.setTextColor(ContextCompat.getColor(mContext,R.color.blue_theme))
                holder.itemView.Subscibe_B.text = mContext.resources.getString(R.string.notSub)
                classList[position].subscribed = false
            } else {
                Log.d("subB", "ifffffff")
                mViewModel.addSub(classList[position].name)
                holder.itemView.Subscibe_B.setBackgroundResource(R.drawable.sub_button)
                holder.itemView.Subscibe_B.setTextColor(ContextCompat.getColor(mContext,R.color.white))
                holder.itemView.Subscibe_B.text = mContext.resources.getString(R.string.Sub)
                classList[position].subscribed = true
            }

        }




        holder.itemView.communityName_TV.setOnClickListener {
            val intent = Intent(mContext, CommunityPosts::class.java)
            intent.putExtra("ClassName", classes)
            mContext.startActivity(intent)
        }

    }
}

class ListHolder(v: View) : RecyclerView.ViewHolder(v)












