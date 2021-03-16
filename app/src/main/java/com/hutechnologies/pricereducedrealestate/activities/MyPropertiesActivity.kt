package com.hutechnologies.pricereducedrealestate.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hutechnologies.pricereducedrealestate.R
import com.hutechnologies.pricereducedrealestate.adapters.MyPropertiesAdapter
import com.hutechnologies.pricereducedrealestate.listners.MyPropertyClickListner
import com.hutechnologies.pricereducedrealestate.models.GetMyProperties
import com.hutechnologies.pricereducedrealestate.models.Images
import kotlinx.android.synthetic.main.activity_my_properties.*

class MyPropertiesActivity : AppCompatActivity(), MyPropertyClickListner {
    private  val myPropertyRef = FirebaseDatabase.getInstance().getReference("MyProperties")
    private lateinit var  adapter: MyPropertiesAdapter
//    private var myPropertiesList = mutableListOf<GetMyProperties>()
//    private var imageList  : ArrayList<Images> = arrayListOf()
    private var secondImageList  : ArrayList<Images> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_properties)
        val toolbar = supportActionBar
        toolbar?.title = "My Properties"
        toolbar?.setDisplayHomeAsUpEnabled(true)
        myPropertiesProgressBar.visibility = View.VISIBLE
        val manager  = LinearLayoutManager(this)
        rvmyProperties.layoutManager = manager
        adapter = MyPropertiesAdapter(this, this)
        rvmyProperties.adapter = adapter
         getProperties()
//        adapter.setImages(secondImageList)

        btnAddProperty.setOnClickListener{
            startActivity(Intent(this, AddPropertyActivity::class.java))
        }
    }
    val muImagesRef = FirebaseDatabase.getInstance().getReference("Images")
    private fun getImages(){
        muImagesRef.child(FirebaseAuth.getInstance().currentUser!!.uid).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                if(snapshot.exists()){
                    val imgList = mutableListOf<Images>()
                    for(images in snapshot.children){
                        val image = images.getValue(Images::class.java)
                        image?.let {
                            imgList.add(it)
                        }
                    }
                    adapter.setImages(imgList as ArrayList<Images>)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    private fun getProperties(){
        myPropertyRef.child(FirebaseAuth.getInstance().currentUser!!.uid).addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val myPropertiesList = mutableListOf<GetMyProperties>()
//                        getImages()
//                        imageList.clear()
                        for (propertyList in snapshot.children) {
                            val property = propertyList.getValue(GetMyProperties::class.java)
                            myPropertiesProgressBar.visibility = View.GONE

//                            if (property != null) {
//                                myPropertyRef.child(FirebaseAuth.getInstance().currentUser!!.uid)
//                                    .child(property.id!!).child("images").addValueEventListener(
//                                        object : ValueEventListener {
//                                            override fun onDataChange(imagesSnapshot: DataSnapshot) {
//                                                if (imagesSnapshot.exists()) {
//                                                    myPropertiesProgressBar.visibility = View.GONE
////                                                    imageList.clear()
//                                                    var imageList: ArrayList<Images> = arrayListOf()
//                                                    for (imgList in imagesSnapshot.children) {
//                                                        val image =
//                                                            imgList.getValue(Images::class.java)
//                                                        image?.let {
//                                                            imageList.add(it)
//                                                        }
////                                                        Toast.makeText(this@MyPropertiesActivity, image!!.image.toString(), Toast.LENGTH_SHORT)
////                                                            .show()
//                                                    }
//                                                    adapter.setImages(imageList)
////                                                    secondImageList = imageList
////                                                    Toast.makeText(this@MyPropertiesActivity, imageList.first().image.toString(), Toast.LENGTH_SHORT).show()
//                                                }
//                                            }
//
//                                            override fun onCancelled(error: DatabaseError) {
//
//                                            }
//
//                                        })
//                            }
                            property?.let {
                                myPropertiesList.add(it)
                            }
                        }
                        adapter.setProperties(myPropertiesList)
                    } else {
                        myPropertiesProgressBar.visibility = View.GONE
                        Toast.makeText(
                            this@MyPropertiesActivity,
                            "No Property found!",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return super.onSupportNavigateUp()
    }

    override fun onMyPropertyClickListner(view: View, getMyProperties: GetMyProperties) {
            val intent = Intent(this, PropertyDetailActivity::class.java)
            intent.putExtra("property", getMyProperties)
            startActivity(intent)
    }

//    override fun onResume() {
//        super.onResume()
//        getProperties()
//    }
}