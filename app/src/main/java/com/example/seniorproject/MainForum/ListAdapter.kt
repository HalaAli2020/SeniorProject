package com.example.seniorproject.MainForum

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.R
import kotlinx.android.synthetic.main.list_rv.view.*

class ListAdapter(context: Context, private val classList: MutableLiveData<List<String>>) :
    RecyclerView.Adapter<ListHolder>() {

    val mContext: Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.list_rv, parent, false)

        return ListHolder(cellForRow)
    }

    override fun getItemCount(): Int {

        return if (classList.value != null)
            classList.value!!.size
        else
            0

    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {

        val classes: String = classList.value!![position]
        holder.itemView.communityName_TV.text = classes


        holder.itemView.setOnClickListener{
            val intent = Intent(mContext, CommunityPosts::class.java)
            intent.putExtra("ClassName", classes)
            mContext.startActivity(intent)
        }

    }
}

class ListHolder(v: View) : RecyclerView.ViewHolder(v)












