package com.example.myapplication.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.myapplication.R

@BindingAdapter("stationImageUrl")
fun bindStationImage(imgView: ImageView, imgUrl: String?) {
    val imageUrl = when {
        imgUrl.isNullOrEmpty() -> R.drawable.ic_no_image_128
        else -> imgUrl
    }
    Glide.with(imgView)
        .load(imageUrl)
        .into(imgView)
}