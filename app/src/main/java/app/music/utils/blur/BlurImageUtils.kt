package app.music.utils.blur

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import app.music.R
import app.music.utils.BitmapUtils
import app.music.utils.color.AttributeColorUtils
import app.music.utils.imageloading.ImageLoadingUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jp.wasabeef.blurry.Blurry

object BlurImageUtils {

    private var mBlurImageDisposable: Disposable? = null

    fun blurImage(activity: Activity, compositeDisposable: CompositeDisposable,
                  imageBytes: ByteArray, imageView: ImageView) {
        mBlurImageDisposable?.dispose()
        mBlurImageDisposable = Observable.defer { Observable.just(imageBytes) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map { imageByteArray ->
                    BitmapUtils.getCompressedBitmap(imageByteArray)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { imageBitmap ->
                            imageBitmap?.let {
                                blurBitmap(activity, imageBitmap, imageView)
                            }
                        },
                        onError = { it.printStackTrace() },
                        onComplete = {
                            mBlurImageDisposable?.dispose()
                            mBlurImageDisposable = null
                        }
                )
        compositeDisposable.add(mBlurImageDisposable!!)
    }

    fun blurImage(activity: Activity, compositeDisposable: CompositeDisposable, imageLink: String,
                  requestOptions: RequestOptions, imageView: ImageView) {
        Glide.with(activity)
                .asBitmap()
                .load(imageLink)
                .apply(requestOptions)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        mBlurImageDisposable?.dispose()
                        mBlurImageDisposable = Observable.defer { Observable.just(resource) }
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .map { imageBitmap ->
                                    BitmapUtils.getResizedBitmap(imageBitmap, 32)
                                }
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeBy(
                                        onNext = { imageBitmap ->
                                            imageBitmap?.let {
                                                blurBitmap(activity, imageBitmap, imageView)
                                            }
                                        },
                                        onError = { it.printStackTrace() },
                                        onComplete = {
                                            mBlurImageDisposable?.dispose()
                                            mBlurImageDisposable = null
                                        }
                                )
                        compositeDisposable.add(mBlurImageDisposable!!)
                    }
                }
                )
    }

    fun loadAndBlurImageUsingCoil(
            blurBackground: ImageView, coverArt: ImageView, imageLink: String,
            requestOptions: RequestOptions, activity: Activity,
            compositeDisposable: CompositeDisposable) {
        Glide.with(coverArt)
                .asBitmap()
                .load(imageLink)
                .apply(requestOptions)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        ImageLoadingUtils.loadImage(coverArt, resource, requestOptions)
                        mBlurImageDisposable?.dispose()
                        mBlurImageDisposable = Observable.defer { Observable.just(resource) }
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .map { imageBitmap ->
                                    BitmapUtils.getResizedBitmap(imageBitmap, 32)
                                }
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeBy(
                                        onNext = { imageBitmap ->
                                            imageBitmap?.let {
                                                blurBitmap(activity, imageBitmap, blurBackground)
                                            }
                                        },
                                        onError = { it.printStackTrace() },
                                        onComplete = {
                                            mBlurImageDisposable?.dispose()
                                            mBlurImageDisposable = null
                                        }
                                )
                        compositeDisposable.add(mBlurImageDisposable!!)
                    }
                }
                )
    }

    private fun blurBitmap(activity: Activity, imageBitmap: Bitmap, imageView: ImageView) {
        if (activity.isFinishing) return
        Blurry.with(activity)
                .radius(5)
                .async()
                .color(AttributeColorUtils.getColor(activity, R.attr.blur_overlay_color))
                .from(imageBitmap)
                .into(imageView)
    }
}
