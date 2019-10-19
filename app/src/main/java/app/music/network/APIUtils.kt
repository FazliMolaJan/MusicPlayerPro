package app.music.network

import app.music.model.entity.OnlineMusik
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Retrofit

object APIUtils {

    public const val sBaseUrl = "https://andreydjandrey.000webhostapp.com/Server/"

    private fun getService(): IEndPoint {
        return APIRetrofitClient.getClient(sBaseUrl).create(IEndPoint::class.java)
    }

    fun getAllMusicCall(): Call<List<OnlineMusik>> = getService().getAllMusic()

    fun getAllMusicCall(retrofit: Retrofit): Call<List<OnlineMusik>> {
        return retrofit.create(IEndPoint::class.java).getAllMusic()
    }

    fun getTestAllOnlineMusicCall(): Observable<List<OnlineMusik>> = getService().testAllOnlineMusic()
}
