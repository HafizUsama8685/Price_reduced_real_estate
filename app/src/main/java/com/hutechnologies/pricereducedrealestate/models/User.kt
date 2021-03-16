package com.hutechnologies.pricereducedrealestate.models

data class User(
    var id : String? =null,
    val fname: String? = null,
    val lname: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val password: String? = null
)