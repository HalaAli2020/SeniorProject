package com.example.seniorproject.MainForum

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.R
import kotlinx.android.synthetic.main.list_rv.view.*

class ListAdapter(val classList: MutableLiveData<List<String>>): RecyclerView.Adapter<ListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.list_rv, parent, false)
        return ListHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        classList.let {
            Log.d("BIGMOODS", "||:" + classList.value?.size)
            return classList.value?.size!!
        }

        return 0
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {

        Log.d("BIGMOODS", "||:" + classList.value!![position])
        val classes: String = classList.value!![position]
        holder.itemView.communityName_TV.text = classes

    }


}

class ListHolder(v: View): RecyclerView.ViewHolder(v){
}