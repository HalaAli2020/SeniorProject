package com.example.seniorproject.MainForum.Posts

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.Dagger.AppComponent
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.R
import com.example.seniorproject.data.models.CRN
import com.example.seniorproject.search.SearchAdapter
import com.example.seniorproject.viewModels.SearchViewModel
import com.example.seniorproject.viewModels.SettingsViewModel
import javax.inject.Inject

class SettingsActivity : AppCompatActivity()
{


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        //binding  = DataBindingUtil.setContentView(this, R.layout.activity_settings)


    }
}