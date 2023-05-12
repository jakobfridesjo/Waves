package com.example.myapplication.utils

import android.graphics.drawable.PictureDrawable
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.caverock.androidsvg.SVG
import com.example.myapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.lang.Exception
import java.util.Locale

@BindingAdapter("stationImageUrl")
fun bindStationImage(imgView: ImageView, imgUrl: String?) {
    if (imgUrl.isNullOrEmpty()) {
        Glide.with(imgView)
            .load(R.drawable.ic_no_image_128)
            .transform(FitCenter(), RoundedCorners(10))
            .into(imgView)
    } else {
        val regex = ".*\\.svg$".toRegex()
        if (regex.matches(imgUrl)) {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val client = OkHttpClient()
                    val request = Request.Builder()
                        .url(imgUrl)
                        .build()
                    val response = withContext(Dispatchers.IO) {
                        client.newCall(request).execute()
                    }
                    if (!response.isSuccessful) throw IOException("ERROR: $response")
                    val inputStream = response.body?.byteStream()
                    val svg = SVG.getFromInputStream(inputStream)
                    val drawable = PictureDrawable(svg.renderToPicture())

                    Glide.with(imgView)
                        .load(drawable)
                        .transform(FitCenter(), RoundedCorners(10))
                        .into(imgView)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            Glide.with(imgView)
                .load(imgUrl)
                .transform(FitCenter(), RoundedCorners(10))
                .into(imgView)
        }
    }
}

@BindingAdapter("countryCode")
fun bindCountryCode(textView: TextView, countryCode: String) {
    val locale = Locale.getDefault()
    val country = try {
        Locale("", countryCode).getDisplayCountry(locale)
    } catch (e: Exception) {
        "WW"
    }
    textView.text = country
}