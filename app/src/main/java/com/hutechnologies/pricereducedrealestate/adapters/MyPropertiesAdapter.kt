package com.hutechnologies.pricereducedrealestate.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hutechnologies.pricereducedrealestate.R
import com.hutechnologies.pricereducedrealestate.listners.MyPropertyClickListner
import com.hutechnologies.pricereducedrealestate.models.GetMyProperties
import com.hutechnologies.pricereducedrealestate.models.Images
import kotlinx.android.synthetic.main.properties_item_layout.view.*

class MyPropertiesAdapter(val context: Context, val propertiesClickListner: MyPropertyClickListner) : RecyclerView.Adapter<MyPropertiesAdapter.MyPropertiesViewholder>() {
    class MyPropertiesViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    private var myPropertiesList: MutableList<GetMyProperties> = arrayListOf()
    fun setProperties(properties: MutableList<GetMyProperties>) {
        myPropertiesList = properties
        notifyDataSetChanged()
    }

    private var myImagesList: ArrayList<Images> = arrayListOf()
    fun setImages(image: ArrayList<Images>) {
        myImagesList = image
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPropertiesViewholder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.properties_item_layout, parent, false)
        return MyPropertiesViewholder(view)
    }

    override fun onBindViewHolder(holder: MyPropertiesViewholder, position: Int) {
        val properties = myPropertiesList[position]
//        val images = myImagesList[position]
//        if(myImagesList.isEmpty()){
//            Toast.makeText(holder.itemView.context, "Empty List", Toast.LENGTH_SHORT).show()
//        }
//        else{
//            val images = myImagesList[0]
//            Toast.makeText(holder.itemView.context, myImagesList.size.toString(), Toast.LENGTH_SHORT).show()
////            Toast.makeText(holder.itemView.context, images.image.toString(), Toast.LENGTH_SHORT).show()
////           if(properties.id == images.propId){
//               Glide.with(context)
//                   .load(images.image)
//                   .centerInside()
//                   .into(holder.itemView.property_Item_image)
////           }
//
//        }
        holder.itemView.tv_Property_Item_Description.text = properties.description.toString()
        holder.itemView.tv_Property_Item_Area.text = properties.area.toString() + " Sq.feet,"
        holder.itemView.tv_Property_Item_state.text = "State: " + properties.state.toString()
//        if(properties.images!!.image0 != null){
////            Glide.with(context)
////                .load(properties.images!!.image0)
////                .centerInside()
////                .into(holder.itemView.property_Item_image)
//        }

        properties.images?.image0?.let {
            Glide.with(context)
                .load(it)
                .into(holder.itemView.property_Item_image)
        }


        holder.itemView.setOnClickListener {
            propertiesClickListner.onMyPropertyClickListner(it, properties)
        }
    }

    override fun getItemCount(): Int {
        return myPropertiesList.size
    }
}