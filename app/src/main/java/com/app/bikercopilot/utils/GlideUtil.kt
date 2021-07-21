package com.app.bikercopilot.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.app.bikercopilot.R
import com.app.bikercopilot.androidutils.GlideApp
import com.app.bikercopilot.androidutils.GlideRequest
import com.bumptech.glide.load.engine.DiskCacheStrategy

private fun createRequest(context: Context, url: String) =
    GlideApp.with(context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .skipMemoryCache(false)


fun ImageView.displayImageBitmap(
    url: String?,
    @DrawableRes placeholder: Int = R.drawable.ic_biker
) {
    val glideRequest: GlideRequest<Drawable> = url?.let {
        createRequest(this.context, url)
            .placeholder(placeholder)
            .skipMemoryCache(false)

    } ?: GlideApp.with(this.context).load(placeholder)
    glideRequest.into(this)
}


fun ImageView.displayImageBitmapCircle(
    url: String?,
    @DrawableRes placeholder: Int = R.drawable.ic_biker
) {
    val glideRequest: GlideRequest<Drawable> = url?.let {
        createRequest(this.context, url)
            .placeholder(placeholder)
            .skipMemoryCache(false)

    } ?: GlideApp.with(this.context).load(placeholder)
    glideRequest.circleCrop().into(this)
}