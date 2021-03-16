package com.hutechnologies.pricereducedrealestate.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.hutechnologies.pricereducedrealestate.R
import com.hutechnologies.pricereducedrealestate.fragments.HomeFragment
import com.hutechnologies.pricereducedrealestate.sharedprefs.MySharedPrefs
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.drawer_header.view.*

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPrefs : SharedPreferences
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showNavigationDrawer()
        loadFragment(HomeFragment())
        val headerView : View = nav_view.getHeaderView(0)
        nav_view.getMenu().getItem(0).isChecked = true
        sharedPrefs = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val email = sharedPrefs.getString("email", " ")
        val image = sharedPrefs.getString("image", null)

        if(image == null){
            Glide.with(this)
                .load(R.drawable.person)
                .placeholder(R.drawable.person)
                .circleCrop()
                .into(headerView.imageView)
        }else{

        }
        if(email == null || email == " "){
//            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show()
            headerView.tvDrawerName.text = "Guest User"
            headerView.tvDrawerEmail.text = "guestuser@gmail.com"
        }else{
//            Toast.makeText(this, "$email", Toast.LENGTH_SHORT).show()
            val name  = sharedPrefs.getString("fName", " ") +" "+ sharedPrefs.getString("lName", " ")
            headerView.tvDrawerName.text = name
            headerView.tvDrawerEmail.text = "$email"
        }


        nav_view.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home ->{
                    loadFragment(HomeFragment())
                    closeDrawer()
                    true
                }
                R.id.nav_profile -> {
                    if(checkUserLoggedIn()){
                        startActivity(Intent(this, ProfileActivity::class.java))
//                        Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show()
                    }else{
                        startActivity(Intent(this, SignInActivity::class.java))
                    }
                    true
                }
                R.id.nav_myProperties->{
//                    startActivity(Intent(this, AddPropertyActivity::class.java))
                    if(checkUserLoggedIn()){
                        startActivity(Intent(this, MyPropertiesActivity::class.java))
                    }else{
//                        Toast.makeText(this, "My Properties", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, SignInActivity::class.java))
                    }
                    true
                }
                R.id.nav_userGuide -> {
                    startActivity(Intent(this, UserGuideActivity::class.java))
                    Toast.makeText(this, "User Guide", Toast.LENGTH_SHORT).show()
//                    closeDrawer()
                    true
                }
                R.id.nav_contactUs -> {
                    startActivity(Intent(this, ContactUsActivity::class.java))
                    Toast.makeText(this, "Contact Us", Toast.LENGTH_SHORT).show()
//                    closeDrawer()
                    true
                }
                R.id.nav_aboutUs -> {
                    startActivity(Intent(this, AboutUsActivity::class.java))
                    Toast.makeText(this, "About us", Toast.LENGTH_SHORT).show()
//                    closeDrawer()
                    true
                }
                R.id.nav_shareApp -> {
                    Toast.makeText(this, "Share App", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_logout-> {
//                    Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show()
                    showLogoutDialogue()
                    closeDrawer()
                    true
                }
                else->{
                    false
                }
            }
        }
    }

    private fun checkUserLoggedIn(): Boolean{
        val email = sharedPrefs.getString("email", " ")
        val image = sharedPrefs.getString("image", null)

        return !(email == null || email == " ")
    }

    private fun showLogoutDialogue(){
        MaterialAlertDialogBuilder(this)
            .setTitle("Warning!")
            .setMessage("Are you sure you want to logout.")
            .setPositiveButton("yes"){ dialog, which->
//                Toast.makeText(this, "yes", Toast.LENGTH_SHORT).show()
                sharedPrefs.edit().remove("email").apply()
                sharedPrefs.edit().remove("fName").apply()
                sharedPrefs.edit().remove("lName").apply()
                sharedPrefs.edit().remove("phone").apply()
                val intent = Intent(this, SignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                this.finish()

            }
            .setNegativeButton("no"){ dialog, which->
                dialog.dismiss()
//                Toast.makeText(this, "no", Toast.LENGTH_SHORT).show()

            }.show()
    }
    private fun closeDrawer(){
        drawerLayout.closeDrawer(GravityCompat.START)
    }
    private fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.home_fragment_container, fragment).commit()
    }
    private fun showNavigationDrawer(){
        setSupportActionBar(toolbar)
        val drawerToggle: androidx.appcompat.app.ActionBarDrawerToggle =
            object : androidx.appcompat.app.ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                (R.string.drawer_layout_open),
                (R.string.drawer_layout_close)
            ) {

            }
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
    }
    
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)

        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        val headerView : View = nav_view.getHeaderView(0)
        super.onResume()
    }
}