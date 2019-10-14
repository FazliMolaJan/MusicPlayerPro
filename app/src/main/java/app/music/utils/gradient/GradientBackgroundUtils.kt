package app.music.utils.gradient

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.GradientDrawable
import androidx.palette.graphics.Palette
import android.view.View
import app.music.R
import app.music.utils.BitmapUtils
import app.music.utils.color.AttributeColorUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

object GradientBackgroundUtils {

    private var mGradientBackgroundDisposable: Disposable? = null

    fun createGradientBackground(activity: Activity, compositeDisposable: CompositeDisposable,
                                 imageBytes: ByteArray, view: View) {

        mGradientBackgroundDisposable?.dispose()
        mGradientBackgroundDisposable = Observable.defer { Observable.just(imageBytes) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map { imageBytesArray ->
                    BitmapUtils.getCompressedBitmap(imageBytesArray)
                }
                .map { imageBitmap ->
                    createGradientDrawable(activity as Context, imageBitmap)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { gradientDrawable ->
                            if (!activity.isFinishing) {
                                view.background = gradientDrawable
                            }
                        },
                        onError = { it.printStackTrace() },
                        onComplete = {
                            mGradientBackgroundDisposable?.dispose()
                            mGradientBackgroundDisposable = null
                        }
                )
        compositeDisposable.add(mGradientBackgroundDisposable!!)
    }

    fun createGradientBackground(activity: Activity, compositeDisposable: CompositeDisposable,
                                 imageLink: String, view: View) {

        mGradientBackgroundDisposable?.dispose()
        mGradientBackgroundDisposable = Observable.defer { Observable.just(imageLink) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map { link ->
                    BitmapUtils.getBitmapFutureTarget(activity, link).get()
                }
                .map { imageBitmap ->
                    BitmapUtils.getResizedBitmap(imageBitmap, 32)
                }
                .map { resizedBitmap ->
                    createGradientDrawable(activity, resizedBitmap)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { gradientDrawable ->
                            if (!activity.isFinishing) {
                                view.background = gradientDrawable
                            }
                        },
                        onError = { it.printStackTrace() },
                        onComplete = {
                            mGradientBackgroundDisposable?.dispose()
                            mGradientBackgroundDisposable = null
                        }
                )
        compositeDisposable.add(mGradientBackgroundDisposable!!)
    }

    private fun createGradientDrawable(context: Context, bitmap: Bitmap): GradientDrawable {
        val mainColor by lazy {
            AttributeColorUtils.getColor(context, R.attr.main_background_color)
        }
        with(Palette.from(bitmap).generate()) {
            return GradientDrawable(
                    GradientDrawable.Orientation.TL_BR,
                    intArrayOf(
                            getDarkVibrantColor(getDarkMutedColor(mainColor)),
                            getVibrantColor(getMutedColor(mainColor)),
                            getLightVibrantColor(getLightMutedColor(mainColor))
                    )
            )
        }
    }
}