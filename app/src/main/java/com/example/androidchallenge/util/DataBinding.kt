package com.example.androidchallenge.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.androidchallenge.R

/**
 * Glide binding
 */
@BindingAdapter("glide")
fun setImage(view: ImageView, glide: String?) {
    glide?.apply {
        Glide.with(view)
            .load(glide)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.color.design_default_color_secondary)
            .into(view)
    }
}


/**
 * View's visibility
 */
@BindingAdapter("visibility")
fun setVisibility(view: View, visibility: Int) {
    view.visibility = visibility
}

/**
 * Html binding
 */
@BindingAdapter("html")
fun setText(view: TextView, html: String?) {
    html?.apply {
        view.text = HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT).toString()
            .replace("\n", "")
    }
}
