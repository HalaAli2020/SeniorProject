package com.example.seniorproject.search


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.R
import com.example.seniorproject.data.models.CRN
import com.example.seniorproject.viewModels.SearchViewModel

/* Adapter for to create a searchable recyclerview to let the user search through all of the classes available
* this uses a custom filter to fliter through the classes in the list the replace the list with a new list of what the user is searching for  */
class SearchAdapter(context: Context, ViewModel: SearchViewModel, Clist : MutableLiveData<MutableList<CRN>>) :
    RecyclerView.Adapter<SearchViewHolder>() {
    private var classlist: MutableLiveData<MutableList<CRN>>? = Clist
    val mContext = context
    private val mViewModel = ViewModel


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.rv_list, parent, false)
        return SearchViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        if (!classlist!!.value.isNullOrEmpty())
            return classlist!!.value!!.size
        else
            Log.d("NULL", "Itsa null")
        return 0
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(classlist!!.value!![position], mContext, mViewModel)
        Log.d("In list", classlist!!.value!![position].name)
    }


    fun onfilter(query: String?) {
        if (query.isNullOrEmpty()) {
            classlist = mViewModel.sendlistf()
            notifyDataSetChanged()
        } else {
            //classlist!!.value!!.removeAt()
            classlist = mViewModel.sendlistf()
            val clist: MutableList<CRN> = mutableListOf()
            for ((index, x) in classlist!!.value!!.withIndex()) {
                // query.contains()
                if (x.name.contains(query, true)) {
                    clist.add(x)
                }
            }
            classlist!!.value = clist
            notifyDataSetChanged()
        }

    }
}
