package com.gmail.orlandroyd.testepratico

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.gmail.orlandroyd.testepratico.databinding.ActivityMainBinding
import com.gmail.orlandroyd.testepratico.util.toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    // Nav
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var listener: NavController.OnDestinationChangedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        drawerLayout = binding.drawerLayout
        navView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_my_products, R.id.nav_config, R.id.nav_profile
            ), drawerLayout
        )

        // Navigation
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)

        // Setup login screen
        listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_login -> {
                    supportActionBar!!.hide()
                    drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
                }
                R.id.nav_detail -> {
                    drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
                }
                else -> {
                    supportActionBar!!.show()
                    drawerLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED)
                    navController.clearBackStack(R.id.nav_my_products)
                }
            }

        }

        // NavHeader
        setupNavHeader()
    }

    private fun setupNavHeader() {
        val navigationView = binding.navView.getHeaderView(0)
        val tvUserName = navigationView.findViewById<TextView>(R.id.tv_username)
        // TODO: get from preferences
        tvUserName.text = "Admin"
        // onClick profile image
        val imgAvatar = navigationView.findViewById<ImageView>(R.id.img_avatar)
        imgAvatar.setOnClickListener {
            navController.popBackStack(R.id.nav_my_products, false)
            navController.navigate(R.id.nav_profile)
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    private fun isValidDestination(destination: Int) =
        destination != navController.currentDestination!!.id

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isChecked = true
        drawerLayout.closeDrawers()
        if (isValidDestination(item.itemId)) {
            when (item.itemId) {
                R.id.nav_my_products -> {
                    navController.popBackStack(R.id.nav_my_products, false)
                }
                R.id.nav_config -> {
                    navController.popBackStack(R.id.nav_my_products, false)
                    navController.navigate(R.id.nav_config)
                }
                R.id.nav_logout -> {
                    logout()
                }
            }
        }
        return true
    }

    private fun logout() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(binding.root.context, gso)
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(binding.root.context)
        if (account != null) {
            mGoogleSignInClient.signOut()
                .addOnCompleteListener {
                    toast("Successfully Signed Out (Google)")
                    navController.popBackStack()
                    navController.navigate(R.id.nav_login)
                }
        } else {
            toast("Successfully Signed Out (email)")
            navController.popBackStack()
            navController.navigate(R.id.nav_login)
        }
    }

    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(listener)
    }

    override fun onPause() {
        super.onPause()
        navController.removeOnDestinationChangedListener(listener)
    }

}