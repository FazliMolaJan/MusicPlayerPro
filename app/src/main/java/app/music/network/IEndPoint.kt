package app.music.network

import app.music.model.OnlineMusik
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET

interface IEndPoint {

//    @get:GET("query_all_song.php")
//    val allMusic: Call<List<OnlineMusik>>

    @GET("query_all_song.php")
    fun testAllOnlineMusic(): Observable<List<OnlineMusik>>


    @GET("query_all_song.php")
    fun getAllMusic(): Call<List<OnlineMusik>>

}
