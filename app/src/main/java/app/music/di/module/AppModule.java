package app.music.di.module;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.view.animation.LinearInterpolator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import app.music.R;
import app.music.base.BaseApplication;
import app.music.network.APIUtils;
import app.music.utils.sharepreferences.SharedPrefConstantUtils;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    private final BaseApplication application;

    public AppModule(BaseApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    BaseApplication provideApplicationContext() {
        return application;
    }

//    @Provides
////    @Singleton
//    static Handler provideHandler() {
//        return new Handler();
//    }

    @Provides
//    @Singleton
    static MediaMetadataRetriever provideMediaMetadataRetriever() {
        return new MediaMetadataRetriever();
    }

    @Provides
    @Singleton
    static Retrofit provideRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(APIUtils.sBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @Singleton
    static OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .writeTimeout(0, TimeUnit.MILLISECONDS)
                .connectTimeout(0, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .protocols(Arrays.asList(Protocol.HTTP_1_1))
                .build();
    }

    @Provides
    @Singleton
    static Gson provideGson() {
        return new GsonBuilder().setLenient().create();
    }

    @Provides
    @Singleton
//    @RotationObjectAnimator
    static ObjectAnimator provideObjectAnimator(BaseApplication application) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(
                null, application.getString(R.string.rotation_property_name), 0, 360);
        objectAnimator.setDuration(5000);
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setRepeatMode(ObjectAnimator.RESTART);
        return objectAnimator;
    }

    @Provides
    @Singleton
    static SharedPreferences provideSharedPreferences(BaseApplication application) {
        return application.getSharedPreferences(
                SharedPrefConstantUtils.PREF_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
}
