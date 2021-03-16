package com.hutechnologies.pricereducedrealestate.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hutechnologies.pricereducedrealestate.R

class UserGuideActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_guide)
        val toolbar = supportActionBar
        toolbar?.title = "User Guide"
        toolbar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return super.onSupportNavigateUp()
    }
}