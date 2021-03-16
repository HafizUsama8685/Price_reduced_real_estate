package com.hutechnologies.pricereducedrealestate.adapters

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.hutechnologies.pricereducedrealestate.R
import kotlinx.android.synthetic.main.image_picker_item_layout.view.*

class ImagePickerAdapter(val context : Context): RecyclerView.Adapter<ImagePickerAdapter.ImagePickerViewHolder>() {
    class ImagePickerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }


    private var imageList : List<Uri> = listOf()
    fun setImages(list : List<Uri>){
        imageList = list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagePickerViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.image_picker_item_layout, parent, false)
        return  ImagePickerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImagePickerViewHolder, position: Int) {
        val images  = imageList[position]
        val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, images)
        holder.itemView.imgPicker.setImageBitmap(bitmap)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}