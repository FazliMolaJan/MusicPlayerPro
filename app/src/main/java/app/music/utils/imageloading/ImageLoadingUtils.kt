package app.music.utils.imageloading

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.text.TextUtils
import android.widget.ImageView
import app.music.R
import app.music.model.BaseMusik
import app.music.model.OnlineMusik
import app.music.utils.BitmapUtils
import app.music.utils.ConstantUtil
import app.music.utils.blur.BlurImageUtils
import coil.api.load
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

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
