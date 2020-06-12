package dev.mgmix.beautyganservingapp

import io.reactivex.Single
import okhttp3.Call
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Api  {

    @POST
    fun uploadImage(@Part srcImage: MultipartBody.Part, @Part refImage: MultipartBody.Part) : Call<Response>

}