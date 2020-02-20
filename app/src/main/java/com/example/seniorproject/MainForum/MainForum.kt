package com.example.seniorproject.MainForum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.seniorproject.Authentication.LoginActivity
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.R
import com.example.seniorproject.databinding.SideNavHeaderBinding
import com.example.seniorproject.databinding.ActivityMainForumBinding
import com.example.seniorproject.viewModels.HomeFragmentViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main_forum.*
import javax.inject.Inject

private const val TAG = "MyLogTag"
class MainForum : AppCompatActivity()
     //FirebaseAuth.AuthStateListener
    {

       /* private val firebaseAuth: FirebaseAuth by lazy {
            FirebaseAuth.getInstance()
        }
        override fun onAuthStateChanged(p0: FirebaseAuth) {
            val currentUser = myViewModel.user
            if (currentUser != null) {
                myViewModel.fetchCurrentUserName()
            } else {
                Log.d(TAG, "authlistener returned null")
            }
        }*/

            @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: HomeFragmentViewModel
    private lateinit var mDrawerLayout: DrawerLayout

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {item->
        when(item.itemId){
            R.id.home -> {
                println("home pressed")
                replaceFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.newPost ->{
                println("new post pressed")
                replaceFragment(NewPostFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.list -> {
                println("list pressed")
                replaceFragment(ListFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerAppComponent.create().inject(this)
        myViewModel = ViewModelProviders.of(this,factory).get(HomeFragmentViewModel::class.java)
        val binding: ActivityMainForumBinding = DataBindingUtil.setContentView(this, R.layout.activity_main_forum)
        replaceFragment(HomeFragment())
        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        loginVerification()
        //here
        setSupportActionBar(findViewById(R.id.toolbar))
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        }


        mDrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val sideNavHeaderBinding:SideNavHeaderBinding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.side_nav_header,binding.navView,false)
        binding.navView.addHeaderView(sideNavHeaderBinding.root)
        //sideNavHeaderBinding.viewmodell = myViewModel
        //Log.d(TAG,myViewModel.rsomthing())

        //Log.d(TAG,myViewModel.user?.displayName ?: "the displayname in main activity")


        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            mDrawerLayout.closeDrawers()
            when (menuItem.itemId) {

                R.id.nav_profile -> {
                    Toast.makeText(this, "show user Profile", Toast.LENGTH_LONG).show()
                }
                R.id.nav_allClasses -> {
                    Toast.makeText(this, "show all classes", Toast.LENGTH_LONG).show()
                }
                R.id.nav_logout -> {
                    Toast.makeText(this, "User Logged out", Toast.LENGTH_LONG).show()
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }

            }
            true
        }
    }



    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }

    private fun loginVerification(){
        val uid = FirebaseAuth.getInstance().uid
        if(uid==null){
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            //comment so commit will work
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                mDrawerLayout.openDrawer(GravityCompat.START)
                true
            }

        }
        return super.onOptionsItemSelected(item)
    }
}

