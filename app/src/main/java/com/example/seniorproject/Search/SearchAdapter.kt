package com.example.seniorproject.Search

import android.content.Context
import android.database.DataSetObserver
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.R
import com.example.seniorproject.data.models.CRN
import com.example.seniorproject.data.models.PostViewHolder
import com.example.seniorproject.viewModels.HomeFragmentViewModel
import com.example.seniorproject.viewModels.SearchViewModel

class SearchAdapter(context: Context, ViewModel: SearchViewModel) :
   RecyclerView.Adapter<SearchViewHolder>(), Filterable {
   var classlist : List<CRN>? = null
    val mContext = context



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        // val binding : ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        val cellForRow = layoutInflater.inflate(R.layout.rv_list, parent, false)
        return SearchViewHolder(cellForRow)
    }
    override fun getItemCount(): Int {
        if (classlist!!.isNullOrEmpty())
            return classlist!!.size
        else
            Log.d("NULL", "Isa null")
        return 0
    }
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(classlist!![position], mContext)
        // Log.d("In list" , savedPosts!!.value!![position].title)

    }

    override fun getFilter(): Filter {
        var filt  = object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                lateinit var results: FilterResults
                var Flist: MutableList<CRN> = mutableListOf()
                if (constraint.isNullOrEmpty()) {
                    if(!classlist!!.isNullOrEmpty())
                    {
                        results.values = classlist
                        results.count = classlist!!.size
                    }
                    else
                    {
                        results.values = null
                        results.count = 0
                    }
                } else {
                   if(!classlist!!.isNullOrEmpty())
                   {
                       for (x in classlist!!.iterator()) {
                           if (x.name.contentEquals(constraint)) {
                               Flist.add(x)
                           }
                       }
                       results.values = Flist
                       results.count = Flist.size
                   }
                }


                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                var Flist: List<CRN> = results?.values as List<CRN>
                notifyDataSetChanged()
            }


        }


    }








}

/* override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
     val layoutInflater = LayoutInflater.from(parent.context)
     // val binding : ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
     val cellForRow = layoutInflater.inflate(R.layout.rv_list, parent, false)
     return SearchViewHolder(cellForRow)
 }
 override fun getItemCount(): Int {
     if (classlist.isNotEmpty())
         return classlist.size
     else
         Log.d("NULL", "Isa null")
     return 0
 }
 override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
     //holder.bind(savedPosts!!.value!![position], mContext)
    // Log.d("In list" , savedPosts!!.value!![position].title)

 }

 override fun getFilter(): Filter {
      fun fitlerResults(query : CharSequence?)
     {
         var Flist : MutableList<CRN> = mutableListOf()
         if (classlist.isNullOrEmpty())
         {
             classlist = Flist
         }
         if(query.isNullOrEmpty())
         {
             if(!classlist.isNullOrEmpty())
             {
                 for (x in classlist!!.iterator())
                 {
                     if(x.name.contains(query!!))
                     {
                         Flist.add(x)
                     }
                 }
             }
         }
     }


 }*/