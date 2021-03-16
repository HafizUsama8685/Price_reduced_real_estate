package com.hutechnologies.pricereducedrealestate.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.hutechnologies.pricereducedrealestate.R
import com.hutechnologies.pricereducedrealestate.models.GetMyProperties
import com.wajahatkarim3.easyvalidation.core.collection_ktx.startWithNonNumberList
import kotlinx.android.synthetic.main.activity_property_detail.*
import kotlinx.android.synthetic.main.activity_update_property.*

class PropertyDetailActivity : AppCompatActivity() {
//    private var imagesList : ArrayList<String> = arrayListOf()
    private lateinit var property : GetMyProperties
    private val imageList = ArrayList<SlideModel>()
    private var index: Int = 0
    private  val myPropertyRef = FirebaseDatabase.getInstance().getReference("MyProperties")
    private val propertiesRef = FirebaseDatabase.getInstance().getReference("Properties")

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_detail)
        val toolbar = supportActionBar
        toolbar?.title="Property Details"
        toolbar?.setDisplayHomeAsUpEnabled(true)

         property = intent.getSerializableExtra("property") as GetMyProperties
        initArray()
        Toast.makeText(this, imageList.size.toString(), Toast.LENGTH_SHORT).show()
        image_slider.setImageList(imageList, ScaleTypes.FIT)

        propDetailDescription.text = property.description.toString()
        propDetailPropType.text = property.propertyType.toString()
        propDetailPropState.text = property.state.toString()
        propDetailPropAddress.text = property.address.toString()
        propDetailPropContact.text = property.contact.toString()
        propDetailPropArea.text = property.area.toString()+" "+ "Square feet"
        propDetailPropPriceWas.text = property.oldPrice.toString()+" " + "$"
        propDetailPropPriceReduced.text = property.reducedPrice.toString() + "$"


        btnEditProperty.setOnClickListener {
            val intent = Intent(this, UpdatePropertyActivity::class.java)
            intent.putExtra("property", property)
            startActivity(intent)
        }
        btnDeleteProperty.setOnClickListener {
            showDeletePropertyDialogue(property)
        }
    }

//    private fun initArray(){
//        property.images?.let {
//            if(it.image0 != null){
//                imageList.add(0, it.image0!!)
//                if(it.image1 != null){
//                    imagesList.add(1, it.image1!!)
//                    if(it.image2 != null){
//                        imagesList.add(2, it.image2!!)
//                        if(it.image3 != null){
//                            imagesList.add(3, it.image3!!)
//                            if(it.image4 != null){
//                                imagesList.add(4, it.image4!!)
//                                if(it.image5 != null){
//                                    imagesList.add(5, it.image5!!)
//                                    if(it.image6 != null){
//                                        imagesList.add(6, it.image6!!)
//                                        if(it.image7 != null){
//                                            imagesList.add(7, it.image7!!)
//                                            if(it.image8 != null){
//                                                imagesList.add(8, it.image8!!)
//                                                if(it.image9 != null){
//                                                    imagesList.add(9, it.image9!!)
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

    private fun deleteProperty(myProperties: GetMyProperties){
        val storgeref = FirebaseStorage.getInstance().getReference("images/").child("${FirebaseAuth.getInstance().currentUser!!.uid}/${myProperties.id}/")
        myPropertyRef.child(FirebaseAuth.getInstance().currentUser!!.uid).child(myProperties.id!!).removeValue().addOnCompleteListener {
            if(it.isSuccessful){
                propertiesRef.child(myProperties.id!!).removeValue().addOnCompleteListener {
                    if(it.isSuccessful){
                        storgeref.delete().addOnCompleteListener {
                            PropertyDetailProgressbar.visibility = View.VISIBLE
                            Toast.makeText(this, "Property Deleted SuccessFully!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MyPropertiesActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                            this.finish()
                        }
                    }
                }
            }else{
                UpdatePropertyProgressBar.visibility = View.GONE
                Toast.makeText(this, "Something went wrong please try again later!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDeletePropertyDialogue(myProperties: GetMyProperties){
        MaterialAlertDialogBuilder(this)
            .setTitle("Warning!")
            .setMessage("Are you sure you want delete the property.")
            .setPositiveButton("yes"){ dialog, which->
                PropertyDetailProgressbar.visibility = View.VISIBLE
                deleteProperty(myProperties)
                Toast.makeText(this, myProperties.id.toString(), Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("no"){ dialog, which->
                dialog.dismiss()
//                Toast.makeText(this, "no", Toast.LENGTH_SHORT).show()

            }.show()
    }



    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return super.onSupportNavigateUp()
    }
    private fun initArray(){
        property.images?.let {
            if(it.image0 != null){
                imageList.add(SlideModel(it.image0))
                if(it.image1 != null){
                    imageList.add(SlideModel(it.image1))
                    if(it.image2 != null){
                        imageList.add(SlideModel(it.image2))
                        if(it.image3 != null){
                            imageList.add(SlideModel(it.image3))
                            if(it.image4 != null){
                                imageList.add(SlideModel(it.image4))
                                if(it.image5 != null){
                                    imageList.add(SlideModel(it.image5))
                                    if(it.image6 != null){
                                        imageList.add(SlideModel(it.image6))
                                        if(it.image7 != null){
                                            imageList.add(SlideModel(it.image7))
                                            if(it.image8 != null){
                                                imageList.add(SlideModel(it.image8))
                                                if(it.image9 != null){
                                                    imageList.add(SlideModel(it.image9))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}