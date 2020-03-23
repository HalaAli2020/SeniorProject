package com.example.seniorproject.Search

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.R
import com.example.seniorproject.viewModels.ChatLogViewModel
import com.example.seniorproject.viewModels.SearchViewModel
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

class Search_Activity: AppCompatActivity()
{
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: SearchViewModel
     lateinit var searchview : SearchView
   // lateinit var lit : RecyclerView
     lateinit var lt :RecyclerView
     lateinit var ada : SearchAdapter =findViewById(R.id.Search_L)
           //= findViewById(R.id.Search_L)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = InjectorUtils.provideSearchViewModelFactory()
        myViewModel = ViewModelProviders.of(this, factory).get(SearchViewModel::class.java)
        //val binding = DataBindingUtil.setContentView(this, R.layout.activity_search )
       ada = SearchAdapter(this.applicationContext, myViewModel)
        lt.adapter = ada





    }

    private fun setupsearchview()
    {
        searchview.setIconifiedByDefault(false)
        searchview.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                ada.filter
            }

            override fun onQueryTextSubmit(query: String?): Boolean {

            }
        })
        searchview.setSubmitButtonEnabled(true)
        searchview.setQueryHint("Search Here")
    }
    private fun loadListview(adapter: SearchAdapter)
    {
       var Clist =  myViewModel.getallclasses()
        for (x in Clist)
        {

        }
    }


}