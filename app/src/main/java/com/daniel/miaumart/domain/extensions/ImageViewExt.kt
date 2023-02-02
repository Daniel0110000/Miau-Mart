package com.daniel.miaumart.domain.extensions

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.squareup.picasso.Picasso

fun ImageView.load(url: String) {
    Picasso.get()
        .load(url)
        .into(this)
}

fun ImageView.loadWithGlide(context: Context, url: String) {
    Glide.get(context).clearMemory()
    Glide.with(context)
        .load(url)
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .into(this)
}