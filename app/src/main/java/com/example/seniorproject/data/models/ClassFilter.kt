package com.example.seniorproject.data.models

import android.widget.Filter


class ClassFilter(Clist : List<CRN>) : Filter() {
    var C = Clist
    override fun performFiltering(constraint: CharSequence?): FilterResults {
       lateinit var results : FilterResults
        var Flist : MutableList<CRN> = mutableListOf()
        if (constraint.isNullOrEmpty())
        {
            results.values = C
            results.count = C.size
        }
        else
        {
            for(x in C)
            {
                if(x.name.contentEquals(constraint))
                {
                    Flist.add(x)
                }
            }
            results.values = Flist
            results.count = Flist.size
        }


        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        var Flist : List<CRN> = results?.values as List<CRN>
        //notifyDataSetChanged()
    }


}