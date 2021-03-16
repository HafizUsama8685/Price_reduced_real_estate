package com.hutechnologies.pricereducedrealestate.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.ViewAnimator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.hutechnologies.pricereducedrealestate.R
import com.hutechnologies.pricereducedrealestate.adapters.ImagePickerAdapter
import com.hutechnologies.pricereducedrealestate.models.Images
import com.hutechnologies.pricereducedrealestate.models.MyProperties
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import gun0912.tedbottompicker.TedBottomPicker
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_add_property.*
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class AddPropertyActivity: AppCompatActivity(){
    private  val myPropertyRef = FirebaseDatabase.getInstance().getReference("MyProperties")
    private val propertiesRef = FirebaseDatabase.getInstance().getReference("Properties")
    private val downloadUri : ArrayList<String> = arrayListOf()


    private var compressedImageList: ArrayList<Uri> = arrayListOf()
    private lateinit var adapter : ImagePickerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_property)
        setStatesSpinnerAdapter()
        setPropertyTypeSpinnerAdapter()
        val toolbar = supportActionBar
        toolbar?.title = "Add Property"
        toolbar?.setDisplayHomeAsUpEnabled(true)

        val gridLayoutManager = GridLayoutManager(this, 3)
        rv_Images.layoutManager = gridLayoutManager
        adapter = ImagePickerAdapter(this)
        rv_Images.adapter = adapter
        btnSelectImages.setOnClickListener {
            requestPermission()
        }
        btnAddDone.setOnClickListener{
            if(checkValidation()){
                if(adapter.itemCount != 0){
                    AddPropertyProgressBar.visibility = ViewAnimator.VISIBLE
                    val property = MyProperties(
                        " ",
                        edtAddDescription.text.toString(),
                        edtAddProperty.text.toString(),
                        edtAddStates.text.toString(),
                        edtAddAddress.text.toString(),
                        edtAddContact.text.toString(),
//                        edtAddArea.text.toString(),
                        edtOldPrice.text.toString(),
                        edtPriceReduced.text.toString()
                    )
                    saveMyProperties(property)
//                    Toast.makeText(this, "Property Added Successfully", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Please select Images", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun addPropertyToListing(myProperties: MyProperties, proId: String){
        myProperties.id = proId
        propertiesRef.child(proId).setValue(
            myProperties
        ).addOnCompleteListener {
            if(it.isSuccessful){
                myPropertyRef.child(FirebaseAuth.getInstance().currentUser!!.uid).child(myProperties.id!!).setValue(
                    myProperties
                ).addOnCompleteListener {
                    if(it.isSuccessful){
                        AddPropertyProgressBar.visibility = ViewAnimator.GONE
                        Toast.makeText(this, "Property Added Successfully", Toast.LENGTH_SHORT).show()
//                        addPropertyToListing(myProperties, myProperties.id!!)
//                AddPropertyProgressBar.visibility = ViewAnimator.GONE
//                Toast.makeText(this, "Property Added Successfully", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
//                AddPropertyProgressBar.visibility = ViewAnimator.GONE
//                Toast.makeText(this, "Property Added Successfully", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun saveMyProperties(myProperties: MyProperties){
        myProperties.id = myPropertyRef.push().key!!
        uploadImages(myProperties.id!!, myProperties)
//        myPropertyRef.child(FirebaseAuth.getInstance().currentUser!!.uid).child(myProperties.id!!).setValue(
//            myProperties
//        ).addOnCompleteListener {
//            if(it.isSuccessful){
//                addPropertyToListing(myProperties, myProperties.id!!)
////                AddPropertyProgressBar.visibility = ViewAnimator.GONE
////                Toast.makeText(this, "Property Added Successfully", Toast.LENGTH_SHORT).show()
//            }else{
//                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

    private fun uploadImages(proId: String, myProperties: MyProperties){
//        val uploadCount = 0
        val muImagesRef = FirebaseDatabase.getInstance().getReference("Images")
        val imageRef = FirebaseStorage.getInstance().reference.child(
            "images/${FirebaseAuth.getInstance().currentUser!!.uid}/${proId}")
//        addPropertyToListing(myProperties, proId)
        for(uploadCount in 0 until  compressedImageList.size){
            val individualImage  = compressedImageList.get(uploadCount)
            val imageupload = imageRef.child( individualImage.lastPathSegment!!)
            imageupload.putFile(individualImage).addOnCompleteListener{
                imageupload.downloadUrl.addOnCompleteListener {task->
                    val downUri = task.result.toString()
//                    Toast.makeText(this, downUri, Toast.LENGTH_SHORT).show()
                    var imageHashMap = HashMap<String, Any>()
                    imageHashMap["image"+uploadCount] = downUri
                    val images = Images()
//                    val images = Images(proId, "", downUri)
                    images.imageId = myPropertyRef.push().key
//                    muImagesRef.child(FirebaseAuth.getInstance().currentUser!!.uid).child(images.imageId!!).setValue(images)
                    myPropertyRef.child(FirebaseAuth.getInstance().currentUser!!.uid).child(proId).child("images").updateChildren(imageHashMap).addOnCompleteListener {
                        if(it.isSuccessful){
                                propertiesRef.child(proId).child("images").updateChildren(imageHashMap).addOnCompleteListener {
                                    if(it.isSuccessful){

                                    }
                                }
//                            AddPropertyProgressBar.visibility = ViewAnimator.GONE
//                            Toast.makeText(this, "Uploaded Successfully", Toast.LENGTH_SHORT).show()
//                            val intent = Intent(this, MyProperties::class.java )
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                            startActivity(intent)
//                            this.finish()
                        }else{
                            Toast.makeText(this, "Something went wrong Please try again later!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        addPropertyToListing(myProperties, proId)
//        AddPropertyProgressBar.visibility = View.GONE
//        Toast.makeText(this, "Uploaded Successfully", Toast.LENGTH_SHORT).show()
    }




    private fun requestPermission() {
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                    openBottomPicker()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(
                    this@AddPropertyActivity,
                    "Permission Denied\n$deniedPermissions",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .check()
    }

    private fun openBottomPicker() {
        TedBottomPicker.with(this)
            .showTitle(false)
            .setCompleteButtonText("Done")
            .setEmptySelectionText("No Image Selected")
            .showMultiImage {
                // here is selected image uri list
                if(it.size <5){
                    Toast.makeText(this, "Please select at least 5 images", Toast.LENGTH_SHORT).show()
                }
                else if (it.size >=10 ){
                    Toast.makeText(this, "You can select maximum 10 images", Toast.LENGTH_SHORT).show()
                }else{
                    for(i in it){
                        lifecycleScope.launch {
                            val compressedImage: File = Compressor.compress(
                                this@AddPropertyActivity, File(
                                    i.path
                                )
                            )
                            val compressedImageUri = Uri.fromFile(compressedImage)
                            compressedImageList.add(compressedImageUri)
                            adapter.setImages(compressedImageList)
//                            Toast.makeText(this@AddPropertyActivity, compressedImageList.size.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
//                    adapter.setImages(it)
//                    Toast.makeText(this@AddPropertyActivity, compressedImageList.size.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun setStatesSpinnerAdapter(){
        val type = arrayOf("Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut","Delaware", "Florida", "Georgia", "Hawaii","Idaho", "Illinois","Indiana", "Iowa",
                            "Kansas","Kentucky", "Louisiana","Maine","Maryland","Massachusetts","Michigan","Minnesota","Mississippi", "Missouri","Montana","Nebraska", "Nevada","New Hampshire",
                            "New Jersey", "New Mexico","New York", "North Carolina","North Dakota","Ohio","Oklahoma","Oregon","Pennsylvania","Rhode Island","South Carolina", "South Dakota",
                            "Tennessee", "Texas","Utah","Vermont", "Virginia","Washington","West Virginia", "Wisconsin", "Wyoming")
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            type
        )
        edtAddStates.setAdapter(adapter)
    }

    private fun setPropertyTypeSpinnerAdapter(){
        val type = arrayOf("Residential", "Commercial", "Industrial", "Raw Land", "Special Use")
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            type
        )
        edtAddProperty.setAdapter(adapter)
    }

    private fun checkValidation(): Boolean{
        if(edtAddDescription.nonEmpty()){
            if(tilAddPropertyType.editText!!.text.toString().nonEmpty()) {
                if (tilAddStates.editText!!.text.toString().nonEmpty()) {
                    if (edtAddDescription.text!!.length > 10) {
                        if(edtAddAddress.nonEmpty()) {
                            if(edtAddContact.nonEmpty()) {
//                                if (edtAddArea.nonEmpty()) {
                                    if (edtOldPrice.nonEmpty()) {
                                        if (edtPriceReduced.nonEmpty()) {
                                            return true
                                        } else {
                                            edtPriceReduced.error = "Please enter price reduced"
                                            edtPriceReduced.requestFocus()
                                        }
                                    } else {
                                        edtOldPrice.error = "Please enter price was!"
                                        edtOldPrice.requestFocus()
                                    }
//                                } else {
//                                    edtAddArea.error = "Please enter area in square feats!"
//                                    edtAddArea.requestFocus()
//                                }
                            }else{
                                edtAddContact.error = "Please Add Contact Number"
                                edtAddContact.requestFocus()
                            }
                        }else{
                            edtAddAddress.error = "Please Enter Address!"
                            edtAddAddress.requestFocus()
                        }
                    } else {
                        edtAddDescription.error = "Please enter at least 100 words!"
                        edtAddDescription.requestFocus()
                    }
                } else {
                    edtAddStates.error = "Please Select State"
                    //                edtAddStates.requestFocus()
                }
            }else{
                edtAddProperty.error = "Please Select Property Type"
//                edtAddProperty.requestFocus()
            }
        }else{
            edtAddDescription.error = "Please enter description!"
            edtAddDescription.requestFocus()
        }
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
//        val intent = Intent(this, MyProperties::class.java )
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//        startActivity(intent)
        this.finish()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
//        val intent = Intent(this, MyProperties::class.java )
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//        startActivity(intent)
//        this.finish()
        super.onBackPressed()
    }
}