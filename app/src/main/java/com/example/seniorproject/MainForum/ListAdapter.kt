package com.example.seniorproject.MainForum

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.R
import com.example.seniorproject.data.models.CRN
import com.example.seniorproject.viewModels.ListViewModel
import kotlinx.android.synthetic.main.list_rv.view.*

class ListAdapter(
    context: Context,
    private val classList: MutableList<CRN>,
    private val mViewModel: ListViewModel
) :
    RecyclerView.Adapter<ListHolder>() {

    val mContext: Context = context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.list_rv, parent, false)

        return ListHolder(cellForRow)
    }

    override fun getItemCount(): Int {

        return if (classList != null)
            classList.size
        else
            0

    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {

        val classes: String = classList[position].name
        holder.itemView.communityName_TV.text = classes

        Log.d("subB", "ERROR: " +  classList[position].Subscribed)

        if (classList[position].Subscribed) {
            Log.d("subB", "true")
            holder.itemView.Subscibe_B.setBackgroundColor(mContext.resources.getColor(R.color.Subscribed))
            holder.itemView.Subscibe_B.text = mContext.resources.getString(R.string.Sub)
        } else {
            Log.d("subB", "false")
            holder.itemView.Subscibe_B.setBackgroundColor(mContext.resources.getColor(R.color.notSub))
            holder.itemView.Subscibe_B.text = mContext.resources.getString(R.string.notSub)
        }

        holder.itemView.Subscibe_B.setOnClickListener {
            Log.d("subB", "Button pressed")
            val sub = classList[position].Subscribed
            if (sub) {
                Log.d("subB", "else")
                mViewModel.removeSub(classList[position].name)
                holder.itemView.Subscibe_B.setBackgroundColor(mContext.resources.getColor(R.color.notSub))
                holder.itemView.Subscibe_B.text = mContext.resources.getString(R.string.notSub)
                classList[position].Subscribed = false
            } else {
                Log.d("subB", "ifffffff")
                mViewModel.addSub(classList[position].name)
                holder.itemView.Subscibe_B.setBackgroundColor(mContext.resources.getColor(R.color.Subscribed))
                holder.itemView.Subscibe_B.text = mContext.resources.getString(R.string.Sub)
                classList[position].Subscribed = true
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












