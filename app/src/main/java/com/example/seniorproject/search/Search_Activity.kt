package com.example.seniorproject.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.MainForum.MainForum
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
        this.title = "Search"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        factory = InjectorUtils.provideSearchViewModelFactory()
        myViewModel = ViewModelProviders.of(this, factory).get(SearchViewModel::class.java)
        myViewModel.getallclasses()

        searchview = search_in
        setupsearchview()
        Search_L.layoutManager = LinearLayoutManager(this)
        adapter = SearchAdapter(this.baseContext, myViewModel, myViewModel.sendlistf())
        Search_L.adapter = adapter


    }


    private fun setupsearchview() {
        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.onfilter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                // ada.onfilter(query)
                return true
            }
        })
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        val intent = Intent(this, MainForum::class.java)
        startActivity(intent)
        return true
    }

}