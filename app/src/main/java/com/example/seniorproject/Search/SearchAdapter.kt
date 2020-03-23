package com.example.seniorproject.Search

import android.content.Context
import android.database.DataSetObserver
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.MainForum.Adapters.ListHolder
import com.example.seniorproject.R
import com.example.seniorproject.data.models.CRN
import com.example.seniorproject.data.models.PostViewHolder
import com.example.seniorproject.databinding.ActivityClickedPostBinding.inflate
import com.example.seniorproject.generated.callback.OnClickListener
import com.example.seniorproject.viewModels.HomeFragmentViewModel
import com.example.seniorproject.viewModels.SearchViewModel
import kotlinx.android.synthetic.main.row.view.*

class SearchAdapter(context: Context, ViewModel: SearchViewModel) :
  RecyclerView.Adapter<SearchViewHolder>(), Filterable {
   var classlist : MutableLiveData<MutableList<CRN>>? = null
    val mContext = context
    val mViewModel =ViewModel
    init {
       classlist = mViewModel.getallclasses()
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        // val binding : ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        val cellForRow = layoutInflater.inflate(R.layout.rv_list, parent, false)
        return SearchViewHolder(cellForRow)
    }
    override fun getItemCount(): Int {
        if (classlist!!.value!!.isNotEmpty())
            return classlist!!.value!!.size
        else
            Log.d("NULL", "Isa null")
        return 0
    }
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(classlist!!.value!![position], mContext)
         Log.d("In list" , classlist!!.value!![position].name)
    }

    override fun getFilter(): Filter {
        fun fitlerResults(query : CharSequence?)
        {
            var Flist : MutableList<CRN> = mutableListOf()
            if (classlist!!.value!!.isNullOrEmpty())
            {
                classlist = Flist
            }
            if(query.isNullOrEmpty())
            {
                if(!classlist!!.value!!.isNullOrEmpty())
                {
                    for (x in classlist!!.value!!.iterator())
                    {
                        if(x.name.contains(query!!))
                        {
                            Flist.add(x)
                        }
                    }
                }
            }
        }


    }
    fun onfilter(query : String)
    {
        //classlist!!.value!!.removeAt()
        for ((index, x) in classlist!!.value!!.iterator().withIndex())
        {
            if(x.name.contentEquals(query))
            {
                removeat(index)
            }
        }
    }
    fun removeat(position: Int)
    {
        classlist!!.value!!.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, classlist!!.value!!.size)
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


/* override fun getFilter(): Filter {
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
         return filt

 }

 override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
     lateinit var view : View
     if(convertView == null)
     {
        view = View.inflate(mContext,R.layout.row, parent)
         var holder = SearchViewHolder(view)
         view.CRN_NAME.text = classlist!![position].name
         view.setOnClickListener{
             Toast.makeText(mContext,"CLicked", Toast.LENGTH_SHORT).show()
         }

     }
     return view
 }

 override fun getItem(position: Int): Any {
     return classlist!![position]
 }

 override fun getItemId(position: Int): Long {
     return position.toLong()

 }

 override fun getCount(): Int {
     return classlist!!.size
 }
*/