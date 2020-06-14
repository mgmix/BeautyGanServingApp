package dev.mgmix.beautyganservingapp

import io.reactivex.Single
import okhttp3.Call
import okhttp3.MultipartBody
import retrofit2.http.*

interface Api  {

    @Multipart
    @POST("/upload")
    fun uploadImage(@Part srcImage: MultipartBody.Part, @Part refImage: MultipartBody.Part) : Single<Response>

//    @GET("/image/{name}")
////    fun getImage(@Path("name") name: String): Single<String>

}