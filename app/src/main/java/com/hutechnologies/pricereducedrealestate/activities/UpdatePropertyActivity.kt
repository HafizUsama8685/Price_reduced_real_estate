package com.hutechnologies.pricereducedrealestate.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.hutechnologies.pricereducedrealestate.R
import com.hutechnologies.pricereducedrealestate.models.GetMyProperties
import com.hutechnologies.pricereducedrealestate.models.MyProperties
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import kotlinx.android.synthetic.main.activity_add_property.*
import kotlinx.android.synthetic.main.activity_add_property.edtAddProperty
import kotlinx.android.synthetic.main.activity_update_property.*

class UpdatePropertyActivity : AppCompatActivity() {
    private  val myPropertyRef = FirebaseDatabase.getInstance().getReference("MyProperties")
    private val propertiesRef = FirebaseDatabase.getInstance().getReference("Properties")
    private lateinit var property : GetMyProperties

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_property)
        val toolbar = supportActionBar
        toolbar?.title="Update Property"
        toolbar?.setDisplayHomeAsUpEnabled(true)
        setStatesSpinnerAdapter()
        setPropertyTypeSpinnerAdapter()

        property = intent.getSerializableExtra("property") as GetMyProperties
        edtUpdateAddDescription.setText(property.description.toString())
        edtUpdateAddProperty.setText(property.propertyType.toString())
        edtUpdateAddStates.setText(property.state.toString())
        edtUpdateAddAddress.setText(property.address.toString())
        edtUpdateAddContact.setText(property.contact.toString())
//        edtUpdateAddArea.setText(property.area.toString())
        edtUpdateOldPrice.setText(property.oldPrice.toString())
        edtUpdatePriceReduced.setText(property.reducedPrice.toString())


        btnUpdateProperty.setOnClickListener {
            if(checkValidation()){
                UpdatePropertyProgressBar.visibility = View.VISIBLE
                val property = MyProperties(
                    property.id,
                    edtUpdateAddDescription.text.toString(),
                    edtUpdateAddProperty.text.toString(),
                    edtUpdateAddStates.text.toString(),
                    edtUpdateAddAddress.text.toString(),
                    edtUpdateAddContact.text.toString(),
//                    edtUpdateAddArea.text.toString(),
                    edtUpdateOldPrice.text.toString(),
                    edtUpdatePriceReduced.text.toString()
                )
                updateProperty(property)
//                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private  fun updateProperty(myProperties: MyProperties){
        var updatePropertyHashMap = HashMap<String, Any>()
        updatePropertyHashMap["description"] = myProperties.description!!
        updatePropertyHashMap["propertyType"] = myProperties.propertyType!!
        updatePropertyHashMap["state"] = myProperties.state!!
        updatePropertyHashMap["address"] = myProperties.address!!
        updatePropertyHashMap["contact"] = myProperties.contact!!
//        updatePropertyHashMap["area"] = myProperties.area!!
        updatePropertyHashMap["oldPrice"] = myProperties.oldPrice!!
        updatePropertyHashMap["reducedPrice"] = myProperties.reducedPrice!!


        myPropertyRef.child(FirebaseAuth.getInstance().currentUser!!.uid).child(myProperties.id!!).updateChildren(updatePropertyHashMap).addOnCompleteListener { 
            if(it.isSuccessful){
                propertiesRef.child(myProperties.id!!).updateChildren(updatePropertyHashMap).addOnCompleteListener {
                    if(it.isSuccessful){
                        UpdatePropertyProgressBar.visibility = View.GONE
                        Toast.makeText(this, "Property Updated Successfully!", Toast.LENGTH_SHORT).show()
                    }else{
                        UpdatePropertyProgressBar.visibility = View.GONE
                        Toast.makeText(this, "Something went wrong please try again later!", Toast.LENGTH_SHORT).show()
                    }
                }

            }else{
                UpdatePropertyProgressBar.visibility = View.GONE
                Toast.makeText(this, "Something went wrong please try again later!", Toast.LENGTH_SHORT).show()
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
        edtUpdateAddStates.setAdapter(adapter)
    }

    private fun setPropertyTypeSpinnerAdapter(){
        val type = arrayOf("Residential", "Commercial", "Industrial", "Raw Land", "Special Use")
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            type
        )
        edtUpdateAddProperty.setAdapter(adapter)
    }

    private fun checkValidation(): Boolean{
        if(edtUpdateAddDescription.nonEmpty()){
            if(tilUpdateAddPropertyType.editText!!.text.toString().nonEmpty()) {
                if (tilUpdateAddStates.editText!!.text.toString().nonEmpty()) {
                    if (edtUpdateAddDescription.text!!.length > 10) {
                        if(edtUpdateAddAddress.nonEmpty()) {
                            if(edtUpdateAddContact.nonEmpty()) {
//                                if (edtUpdateAddArea.nonEmpty()) {
                                    if (edtUpdateOldPrice.nonEmpty()) {
                                        if (edtUpdatePriceReduced.nonEmpty()) {
                                            return true
                                        } else {
                                            edtUpdatePriceReduced.error = "Please enter price reduced"
                                            edtUpdatePriceReduced.requestFocus()
                                        }
                                    } else {
                                        edtUpdateOldPrice.error = "Please enter price was!"
                                        edtUpdateOldPrice.requestFocus()
                                    }
//                                } else {
//                                    edtUpdateAddArea.error = "Please enter area in square feats!"
//                                    edtUpdateAddArea.requestFocus()
//                                }
                            }else{
                                edtUpdateAddContact.error = "Please Add Contact Number"
                                edtUpdateAddContact.requestFocus()
                            }
                        }else{
                            edtUpdateAddAddress.error = "Please Enter Address!"
                            edtUpdateAddAddress.requestFocus()
                        }
                    } else {
                        edtUpdateAddDescription.error = "Please enter at least 100 words!"
                        edtUpdateAddDescription.requestFocus()
                    }
                } else {
                    edtUpdateAddStates.error = "Please Select State"
                    edtUpdateAddStates.requestFocus()
                }
            }else{
                edtUpdateAddProperty.error = "Please Select Property Type"
                edtUpdateAddProperty.requestFocus()
            }
        }else{
            edtUpdateAddDescription.error = "Please enter description!"
            edtUpdateAddDescription.requestFocus()
        }
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return super.onSupportNavigateUp()
    }
}