package com.gmail.orlandroyd.testepratico

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        drawerLayout = binding.drawerLayout
        navView = binding.navView

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_my_products, R.id.nav_config, R.id.nav_profile
            ), drawerLayout
        )

        navController = findNavController(R.id.nav_host_fragment_content_main)

        // Navigation
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)

        // Setup login screen
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.nav_login) {
                supportActionBar!!.hide()
                drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
            } else {
                supportActionBar!!.show()
                drawerLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED)
            }
        }

        // NavHeader
        setupNavHeader()

    }

    private fun setupNavHeader() {
        val navigationView = binding.navView.getHeaderView(0)
        val tvUserName = navigationView.findViewById<TextView>(R.id.tv_username)
        // get from preferences
        tvUserName.text = "Admin"
        // onClick profile image
        val imgAvatar = navigationView.findViewById<ImageView>(R.id.img_avatar)
        imgAvatar.setOnClickListener {
            navController.navigate(R.id.nav_profile)
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

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

    // TODO: CHECK
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isChecked = true
        drawerLayout.closeDrawers()
        when (item.itemId) {
            R.id.nav_my_products -> {
                navController.navigate(R.id.nav_my_products);
            }
            R.id.nav_config -> {
                navController.navigate(R.id.nav_config);
            }
            R.id.nav_logout -> {
                Toast.makeText(applicationContext, "LOGOUT", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }
}