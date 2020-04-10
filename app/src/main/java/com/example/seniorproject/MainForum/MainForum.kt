package com.example.seniorproject.MainForum

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.seniorproject.Authentication.LoginActivity
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.MainForum.Fragments.FragmentHome
import com.example.seniorproject.MainForum.Fragments.FragmentSubscriptions
import com.example.seniorproject.MainForum.NewPost.NewPost
import com.example.seniorproject.Messages.FragmentLatestMessages
import com.example.seniorproject.R
import com.example.seniorproject.data.models.User
import com.example.seniorproject.databinding.ActivityMainForumBinding
import com.example.seniorproject.databinding.SideNavHeaderBinding
import com.example.seniorproject.search.SearchActivity
import com.example.seniorproject.viewModels.HomeFragmentViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main_forum.*
import kotlinx.android.synthetic.main.side_nav_header.*
import javax.inject.Inject

private const val TAG = "MyLogTag"
private const val TAGG = "username"

class MainForum : AppCompatActivity(),
    FirebaseAuth.AuthStateListener {


    private var obse: Observer<User>? = null
    override fun onAuthStateChanged(p0: FirebaseAuth) {
        val currentUser = myViewModel.user
        if (currentUser != null) {
            myViewModel.fetchCurrentUserName()
        } else {
            Log.d(TAG, "authlistener returned null")
        }
    }

    private val sharedPrefs by lazy {  getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: HomeFragmentViewModel
    private lateinit var mDrawerLayout: DrawerLayout


    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    FAB.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.TextColor))
                    FAB.setImageResource(R.drawable.ic_create_black_24dp)
                    replaceFragment(FragmentHome())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.subscriptions -> {
                    FAB.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.TextColor))
                    FAB.setImageResource(R.drawable.ic_create_black_24dp)
                    replaceFragment(FragmentSubscriptions())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.newPost -> {
                    FAB.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.blue_theme))
                    FAB.setImageResource(R.drawable.ic_create_blue_24dp)
                    val intent = Intent(this, NewPost::class.java)
                    startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.list -> {
                    FAB.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.TextColor))
                    FAB.setImageResource(R.drawable.ic_create_black_24dp)
                    val intent = Intent(this, SearchActivity::class.java)
                    startActivity(intent)

                }
                R.id.messages -> {
                    FAB.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.TextColor))
                    FAB.setImageResource(R.drawable.ic_create_black_24dp)
                    //replaceFragment(FragmentLatestMessages())
                    replaceFragment(FragmentLatestMessages())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        loginVerification()
        super.onCreate(savedInstanceState)
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        setTheme()

        DaggerAppComponent.create().inject(this)
        myViewModel = ViewModelProviders.of(this, factory).get(HomeFragmentViewModel::class.java)
        val binding: ActivityMainForumBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main_forum)
        bottom_navigation.onNavigationItemSelectedListener = mOnNavigationItemSelectedListener


        if (!checkNetworkState(applicationContext)) noInternetAlertDialog()


        setSupportActionBar(findViewById(R.id.toolbar))
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.unitlogopropernav)
        }

        mDrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val sideNavHeaderBinding: SideNavHeaderBinding = DataBindingUtil.inflate(layoutInflater, R.layout.side_nav_header, binding.navView, false)
        binding.navView.addHeaderView(sideNavHeaderBinding.root)
        sideNavHeaderBinding.viewmodell = myViewModel
        obse = Observer {
            this.email_display.text = it.email
            this.username_display.text = it.username
        }



        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            mDrawerLayout.closeDrawers()
            when (menuItem.itemId) {

                R.id.nav_profile -> {
                    val intent = Intent(this, UserProfileActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_allClasses -> {
                    Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, Settings::class.java)
                    startActivity(intent)
                }
                R.id.nav_logout -> {
                    Toast.makeText(this, "User is Logged out.", Toast.LENGTH_LONG).show()
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }

            }
            true
        }

        val headerview = navigationView.getHeaderView(0)
        val imageView: ImageView = headerview.findViewById(R.id.profile_image)



        Glide.with(this) //1
            .load(FirebaseAuth.getInstance().currentUser?.photoUrl)
            .placeholder(R.drawable.ic_account_circle_blue_24dp)
            .error(R.drawable.ic_account_circle_blue_24dp)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop().fitCenter()
            .into(imageView)

        replaceFragment(FragmentHome())

    }


    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }

    private fun loginVerification() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkNetworkState(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

    private fun noInternetAlertDialog() {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage("There is no internet!")
            .setCancelable(false)
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle("AlertDialogExample")
        alert.show()
    }


    private fun setTheme(){
        when (sharedPrefs.getInt(KEY_THEME, THEME_LIGHT)) {
            THEME_SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            THEME_DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }


}