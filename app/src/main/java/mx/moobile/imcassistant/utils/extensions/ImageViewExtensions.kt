package mx.moobile.imcassistant.utils.extensions

import android.widget.ImageView
import androidx.annotation.AnyRes
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

/**
 * Extension functions for ImageView
 *
 * @author dD
 */
private fun requestOptions(skipCache: Boolean = false) = RequestOptions().apply {
    if (skipCache) {
        skipMemoryCache(skipCache)
        diskCacheStrategy(DiskCacheStrategy.RESOURCE)
    }
}

fun ImageView.setImageCenterCrop(uri: String?) {
    Glide.with(this.context).load(uri).placeholder(
        CircularProgressDrawable(this.context).apply {
            strokeWidth = 5f
            centerRadius = 30f
        }.also {
            it.start()
        }
    )
        .centerCrop()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

fun ImageView.setImageCenterCropRound(uri: String?) {
    Glide.with(this.context).load(uri).placeholder(
        CircularProgressDrawable(this.context).apply {
            strokeWidth = 5f
            centerRadius = 30f
        }.also {
            it.start()
        }
    )
        .centerCrop()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .apply(RequestOptions.circleCropTransform())
        .into(this)
}

/**
 * Load gif into imageView
 * @param[imageId] resource id of image
 */
fun ImageView.loadGif(@AnyRes imageId: Int) {
    Glide.with(context!!)
        .asGif()
        .placeholder(imageId)
        .fitCenter()
        .load(imageId)
        .apply(requestOptions())
        .into(this)
}

/**
 * Load bitmap into imageView
 * @param[imageId] resource id of image
 */
fun ImageView.loadBitmap(@AnyRes imageId: Int) {
    Glide.with(context!!)
        .load(imageId)
        .apply(requestOptions())
        .thumbnail(0.1f)
        .into(this)
}