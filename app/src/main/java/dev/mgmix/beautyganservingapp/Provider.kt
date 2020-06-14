package dev.mgmix.beautyganservingapp

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Provider {

    val baseUrl = "http://10.0.2.2:5000"

   private val provideApi by lazy {
       Retrofit.Builder()
           .baseUrl(baseUrl)
           .client(provideOkHttp.build())
           .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
           .addConverterFactory(GsonConverterFactory.create(
               GsonBuilder().create()
           ))
           .build()

   }

   private  val provideOkHttp
    = OkHttpClient.Builder()
       .addInterceptor(HttpLoggingInterceptor().apply {
           level = HttpLoggingInterceptor.Level.BODY
       })
       .connectTimeout(2, TimeUnit.MINUTES)
       .readTimeout(1, TimeUnit.MINUTES)
       .writeTimeout(1, TimeUnit.MINUTES)

    val api: Api by lazy {
        provideApi.create(Api::class.java)
    }
}