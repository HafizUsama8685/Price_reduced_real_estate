package com.hutechnologies.pricereducedrealestate.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hutechnologies.pricereducedrealestate.R

class ContactUsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)
        val toolbar = supportActionBar
        toolbar?.title = "Contact Us"
        toolbar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return super.onSupportNavigateUp()
    }

}