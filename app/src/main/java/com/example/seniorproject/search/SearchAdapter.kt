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

class SearchAdapter(context: Context, ViewModel: SearchViewModel, Clist : MutableLiveData<MutableList<CRN>>) :
    RecyclerView.Adapter<SearchViewHolder>(){
    var classlist : MutableLiveData<MutableList<CRN>>? = Clist
    val mContext = context
    val mViewModel = ViewModel
    init {
        //mViewModel.getallclasses()
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        // val binding : ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
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
        Log.d("In list" , classlist!!.value!![position].name)
    }

    fun getFilter(){
        fun fitlerResults(query : CharSequence?)
        {
            var Flist : MutableLiveData<MutableList<CRN>> = MutableLiveData()
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
                            Flist.value!!.add(x)
                        }
                    }
                }
            }
        }

    }
    fun onfilter(query : String?)
    {
        if(query.isNullOrEmpty())
        {
            classlist = mViewModel.sendlistf()
            notifyDataSetChanged()
        }
        else
        {
            //classlist!!.value!!.removeAt()
            classlist = mViewModel.sendlistf()
            var Clist: MutableList<CRN> = mutableListOf()
            for ((index, x) in classlist!!.value!!.withIndex())
            {
                // query.contains()
                if(x.name.contains(query, true))
                {
                    Clist.add(x)
                }
            }
            classlist!!.value = Clist
            notifyDataSetChanged()
        }

    }
    fun removeat(position: Int)
    {
        //notifyItemRemoved(position)
        //notifyItemRangeChanged(position, classlist!!.value!!.size)
        classlist!!.value!!.removeAt(position)

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