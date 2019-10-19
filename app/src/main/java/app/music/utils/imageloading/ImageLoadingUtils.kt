package app.music.utils.imageloading

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.text.TextUtils
import android.widget.ImageView
import app.music.R
import app.music.model.entity.BaseMusik
import app.music.model.entity.OnlineMusik
import app.music.utils.ConstantUtil
import coil.api.load
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

object ImageLoadingUtils {

    fun loadImage(imageView: ImageView, imageData: ByteArray?, requestOptions: RequestOptions) {
        Glide.with(imageView)
                .load(imageData)
                .apply(requestOptions)
                .centerCrop()
                .into(imageView)
    }

    fun loadImage(imageView: ImageView, imageData: Bitmap, requestOptions: RequestOptions) {
        Glide.with(imageView)
                .load(imageData)
                .apply(requestOptions)
                .into(imageView)
    }

    fun loadImage(imageView: ImageView, imageLink: String, requestOptions: RequestOptions) {
        Glide.with(imageView)
                .load(imageLink)
                .apply(requestOptions)
                .into(imageView)
    }

    fun loadImageUsingCoil(imageView: ImageView, imageLink: String, requestOptions: RequestOptions) {
        imageView.load(imageLink) {
            crossfade(true)
            error(requestOptions.errorPlaceholder)
            if (requestOptions.overrideWidth > 0) {
                size(requestOptions.overrideWidth, requestOptions.overrideHeight)
            }
        }
    }

    fun loadImage(imageView: ImageView, resourceId: Int?) {
        Glide.with(imageView)
                .load(resourceId)
                .into(imageView)
    }

    fun loadMusicImage(baseMusik: BaseMusik, metadataRetriever: MediaMetadataRetriever,
                       requestOptions: RequestOptions, imageView: ImageView) {
        val musicType = baseMusik.type
        if (TextUtils.isEmpty(musicType)) return
        when (musicType) {
            ConstantUtil.OFFLINE_MUSIC -> {
                metadataRetriever.setDataSource(baseMusik.location)
                loadImage(imageView, metadataRetriever.embeddedPicture, requestOptions)
            }
            ConstantUtil.ONLINE_MUSIC -> {
                val musicLocation = (baseMusik as OnlineMusik).coverArt
                if (!TextUtils.isEmpty(musicLocation)) {
                    loadImageUsingCoil(imageView, musicLocation, requestOptions)
                }
            }
        }
    }

    fun getCoverArtRequestOption(): RequestOptions {
        return RequestOptions().error(R.drawable.ic_disc_56dp)
    }
}
