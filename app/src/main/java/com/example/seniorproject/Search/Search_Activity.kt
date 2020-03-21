package com.example.seniorproject.Search

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.R
import com.example.seniorproject.viewModels.ChatLogViewModel
import com.example.seniorproject.viewModels.SearchViewModel
import javax.inject.Inject

class Search_Activity: AppCompatActivity()
{
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: SearchViewModel
     lateinit var searchview : SearchView
    lateinit var lit : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = InjectorUtils.provideSearchViewModelFactory()
        myViewModel = ViewModelProviders.of(this, factory).get(SearchViewModel::class.java)
        val binding: MActivitysearch = DataBindingUtil.setContentView(this, R.layout.activity_search)
        lit.adapter = SearchAdapter(this.applicationContext, myViewModel)


    }
}