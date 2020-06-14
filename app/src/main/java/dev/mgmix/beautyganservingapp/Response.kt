package dev.mgmix.beautyganservingapp

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("path")
    val path: String
)