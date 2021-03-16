package com.hutechnologies.pricereducedrealestate.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.activity_my_properties.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), MyPropertyClickListner {

    private  val myPropertyRef = FirebaseDatabase.getInstance().getReference("Properties")
    private lateinit var adapter: MyPropertiesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val manager = LinearLayoutManager(requireContext())
        rvHomeProperties.layoutManager = manager
        adapter = MyPropertiesAdapter(requireContext(), this)
        rvHomeProperties.adapter = adapter
        HomeProgressBar.visibility = View.VISIBLE
        getProperties()

    }

    private  fun getProperties(){

        myPropertyRef.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val myPropertiesList = mutableListOf<GetMyProperties>()
                        for (propertyList in snapshot.children) {
                            val property = propertyList.getValue(GetMyProperties::class.java)
                            HomeProgressBar.visibility = View.GONE
                            property?.let {
                                myPropertiesList.add(it)
                            }
                        }
                        adapter.setProperties(myPropertiesList)
                    } else {
                        HomeProgressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "No Property found!",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


    }

    override fun onMyPropertyClickListner(view: View, getMyProperties: GetMyProperties) {

    }
}