package com.example.seniorproject.search

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.R
import com.example.seniorproject.viewModels.SearchViewModel
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject


class SearchActivity : AppCompatActivity() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: SearchViewModel
    private lateinit var searchview: SearchView
    lateinit var adapter: SearchAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        //Change title bar text, add back button, and enable keyboard
        this.title = "Search"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        //Set up layout, dagger dependency, and viewmodel
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        DaggerAppComponent.create().inject(this)
        myViewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)
        myViewModel.getallclasses()

        //Set up search view
        searchview = search_in
        setupsearchview()

        //Set up RecyclerView and its adapter
        Search_L.layoutManager = LinearLayoutManager(this)
        adapter = SearchAdapter(this.baseContext, myViewModel, myViewModel.sendlistf())
        Search_L.adapter = adapter
    }

    //Search View is being set up to search data
    private fun setupsearchview() {
        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.onfilter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
        })
    }

    //Set up back button on title bar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}