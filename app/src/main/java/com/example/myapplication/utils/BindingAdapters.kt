package com.example.myapplication.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("stationImageUrl")
fun bindStationImage(imgView: ImageView, imgUrl:String) {
    imgUrl.let { posterPath ->
        Glide
            .with(imgView)
            .load(imgUrl)
            .into(imgView)
    }
}