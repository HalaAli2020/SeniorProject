package com.example.seniorproject.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.R
import com.example.seniorproject.data.models.CRN
import com.example.seniorproject.databinding.ActivitySearchBinding
import com.example.seniorproject.viewModels.SearchViewModel
import com.example.seniorproject.viewModels.SearchViewModel_Factory
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

class SearchActivity: AppCompatActivity()
{
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: SearchViewModel
     lateinit var searchview : SearchView
   // lateinit var lit : RecyclerView
     lateinit var lt :RecyclerView
    lateinit var ada : SearchAdapter
    lateinit var mnu : MenuItem
    lateinit var binding : ActivitySearchBinding
    init {


    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_top_nav_menu, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
       searchview = (menu.findItem(R.id.seach_men).actionView as SearchView)
        setupsearchview()
        return true
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        factory = InjectorUtils.provideSearchViewModelFactory()
        myViewModel = ViewModelProviders.of(this, factory).get(SearchViewModel::class.java)
        binding  = DataBindingUtil.setContentView(this, R.layout.activity_search )
        myViewModel.getallclasses()
       ada = SearchAdapter(this.baseContext, myViewModel, myViewModel.sendlistf())
        t
        //searchview = binding.SearchR
        lt = binding.SearchL
        var obse = Observer<MutableList<CRN>> {
                swap(lt)
        }

        var lin : LinearLayoutManager = LinearLayoutManager(this)
        lt.layoutManager = lin
        lt.adapter = ada

       // lt = findViewById<RecyclerView>(R.id.Search_L)
        //setupsearchview()
        //searchview =findViewById<SearchView>(R.id.Search_R)
        myViewModel.Fullist.observe(this, obse)



    }
    private fun swap(lt : RecyclerView)
    {
        ada = SearchAdapter(this.baseContext, myViewModel, myViewModel.sendlistf())
         lt.swapAdapter(ada, true)
    }

    private fun setupsearchview()
    {
        searchview.setIconifiedByDefault(false)
        searchview.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {

                ada.onfilter(newText)

                return false

            }

            override fun onQueryTextSubmit(query: String?): Boolean {
               // ada.onfilter(query)
                return true
            }
        })
        searchview.setSubmitButtonEnabled(true)
        searchview.setQueryHint("Search Here")
    }

}