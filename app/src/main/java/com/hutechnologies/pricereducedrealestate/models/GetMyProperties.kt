package com.hutechnologies.pricereducedrealestate.models

import java.io.Serializable

data class GetMyProperties(
    var id: String? = null,
    var images : MyImages? = null,
    val description: String? = null,
    val propertyType: String? = null,
    val state: String? = null,
    val address: String? = null,
    val contact: String?= null,
    val area: String?= null,
    val oldPrice: String? = null,
    val reducedPrice: String? = null
):Serializable

data class MyImages(
   var image0: String? = null,
   var image1: String? = null,
   var image2: String? = null,
   var image3: String? = null,
   var image4: String? = null,
   var image5: String? = null,
   var image6: String? = null,
   var image7: String? = null,
   var image8: String? = null,
   var image9: String? = null,
):Serializable
data class ImagesList(
    var imageId: String? = null,
    val image:String? = null
)