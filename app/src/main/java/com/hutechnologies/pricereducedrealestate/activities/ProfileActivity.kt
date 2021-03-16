package com.hutechnologies.pricereducedrealestate.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.hutechnologies.pricereducedrealestate.R
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var sharedPrefs : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val toolbar = supportActionBar
        toolbar?.title = "Profile"
        toolbar?.setDisplayHomeAsUpEnabled(true)
        sharedPrefs = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val name  = sharedPrefs.getString("fName", " ") +" "+ sharedPrefs.getString("lName", " ")
        val phone = sharedPrefs.getString("phone", " ")
        val email = sharedPrefs.getString("email", " ")

        Glide.with(this)
            .load(R.drawable.person)
            .circleCrop()
            .into(imgProfile)

        tvProfileName.text = name
        tvProfilePhone.text = phone
        tvProfileEmail.text = email

        imgProfile.setOnClickListener {
            Toast.makeText(this, "Add Profile", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return super.onSupportNavigateUp()
    }
}