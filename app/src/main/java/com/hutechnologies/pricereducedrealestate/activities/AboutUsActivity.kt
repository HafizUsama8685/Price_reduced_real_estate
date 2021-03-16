package com.hutechnologies.pricereducedrealestate.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hutechnologies.pricereducedrealestate.R

class AboutUsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
        val toolbar = supportActionBar
        toolbar?.title = "About Us"
        toolbar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return super.onSupportNavigateUp()
    }
}