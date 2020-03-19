package com.example.seniorproject.MainForum.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.MainForum.Posts.CommunityPosts
import com.example.seniorproject.R
import com.example.seniorproject.data.models.CRN
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.viewModels.ListViewModel
import kotlinx.android.synthetic.main.rv_list.view.*
import kotlinx.android.synthetic.main.rv_post.view.*

class ListAdapter(
    context: Context,
    private val mViewModel: ListViewModel
) :
    RecyclerView.Adapter<ListHolder>() {
    private val classList: MutableLiveData<MutableList<CRN>>?
    val mContext: Context = context
    init {
       //mViewModel.combineSubs()
        //mViewModel.getSubs()
        //classList = mViewModel.returnClasses()
       // mViewModel.combineSubs()
        //classList = mViewModel.returnClasses()
        classList = mViewModel.Getcomnosubs()

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.rv_list, parent, false)
        //mViewModel.combineSubs()
        return ListHolder(cellForRow)
    }

    override fun getItemCount(): Int {

        return if (classList?.value != null)
            classList.value!!.size
        else
            0

    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
            holder.bind(classList?.value!![position], mContext, mViewModel)







    }
}

class ListHolder(v: View) : RecyclerView.ViewHolder(v)
{
    fun bind(crn: CRN, mContext : Context, mViewModel: ListViewModel)
    {
        val classes: String = crn.name
        itemView.communityName_TV.text = classes

        Log.d("subB", "ERROR: " + crn.Subscribed)

        if (crn.Subscribed) {
            Log.d("subB", "true")
            itemView.Subscibe_B.setBackgroundResource(R.drawable.sub_button)
            itemView.Subscibe_B.setTextColor(mContext.resources.getColor(R.color.white))
            itemView.Subscibe_B.text = mContext.resources.getString(R.string.Sub)
        } else {
            Log.d("subB", "false")
            itemView.Subscibe_B.setBackgroundResource(R.drawable.circle_border)
            itemView.Subscibe_B.setTextColor(mContext.resources.getColor(R.color.blue_theme))
            itemView.Subscibe_B.text = mContext.resources.getString(R.string.notSub)
        }

        itemView.Subscibe_B.setOnClickListener {
            Log.d("subB", "Button pressed")
            val sub = crn.Subscribed
            if (sub) {
                Log.d("subB", "else")
                mViewModel.removeSub( crn.name)
                itemView.Subscibe_B.setBackgroundResource(R.drawable.circle_border)
                itemView.Subscibe_B.setTextColor(mContext.resources.getColor(R.color.blue_theme))
                itemView.Subscibe_B.text = mContext.resources.getString(R.string.notSub)
                crn.Subscribed = false
            } else {
                Log.d("subB", "ifffffff")
                mViewModel.addSub(crn.name)
                itemView.Subscibe_B.setBackgroundResource(R.drawable.sub_button)
                itemView.Subscibe_B.setTextColor(mContext.resources.getColor(R.color.white))
                itemView.Subscibe_B.text = mContext.resources.getString(R.string.Sub)
               crn.Subscribed = true
            }

        }




        itemView.communityName_TV.setOnClickListener {
            val intent = Intent(mContext, CommunityPosts::class.java)
            intent.putExtra("ClassName", classes)
            mContext.startActivity(intent)
        }

    }




}













