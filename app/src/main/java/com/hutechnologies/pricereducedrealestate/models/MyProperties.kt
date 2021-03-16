package com.hutechnologies.pricereducedrealestate.models

data class MyProperties(
    var id: String? = null,
    val description: String? = null,
    val propertyType: String? = null,
    val state: String? = null,
    val address: String? = null,
    val contact: String?= null,
//    val area: String?= null,
    val oldPrice: String? = null,
    val reducedPrice: String? = null
)

data class Images(
    var propId :String? = null,
    var imageId: String? = null,
    val image:String? = null
)
