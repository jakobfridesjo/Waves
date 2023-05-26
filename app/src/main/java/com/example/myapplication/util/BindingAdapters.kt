package com.example.myapplication.util

import android.annotation.SuppressLint
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

/**
 * Fetch station images, handles the normal raster formats such as jpg and png as well as svg
 * vector formats that aren't too complicated, or else it just loads a place holder image
 */
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

/**
 * Bind image to the settings list image view
 */
@SuppressLint("DiscouragedApi")
@BindingAdapter("settingImage")
fun bindSettingImage(imgView: ImageView, resourceName: String?) {
    val resourceId = when {
        !resourceName.isNullOrEmpty() -> {
            val resources = imgView.context.resources
            resources.getIdentifier(resourceName, "drawable", imgView.context.packageName)
        }
        else -> R.drawable.ic_no_image_128
    }
    Glide.with(imgView)
        .load(resourceId)
        .transform(FitCenter())
        .into(imgView)
}

/**
 * Convert country code to country name to keep the text view locale aware
 */
@BindingAdapter("countryCode")
fun bindCountryCode(textView: TextView, countryCode: String) {
    val locale = Locale.getDefault()
    val country = try {
        Locale("", countryCode).getDisplayCountry(locale)
    } catch (e: Exception) {
        // World Wide placeholder
        "WW"
    }
    textView.text = country
}

/**
 * Display length of recording in a human readable time format of hh:mm:ss
 */
@BindingAdapter("recordingLength")
fun bindRecordingLength(textView: TextView, length: Long) {
    val hours = length / 3600
    val minutes = (length % 3600) / 60
    val seconds = length % 60

    val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    textView.text = formattedTime
}