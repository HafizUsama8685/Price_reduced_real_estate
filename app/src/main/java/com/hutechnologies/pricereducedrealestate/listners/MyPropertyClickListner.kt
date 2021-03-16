package com.hutechnologies.pricereducedrealestate.listners

import android.view.View
import com.hutechnologies.pricereducedrealestate.models.GetMyProperties

interface MyPropertyClickListner {
    fun onMyPropertyClickListner(view: View, getMyProperties: GetMyProperties)
}