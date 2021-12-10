package io.github.pengdst.salescashier.data.remote.responses


import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody

data class ErrorResponse(
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("success")
    val success: Boolean? = null
) {

    companion object {
        val gson = Gson()

        fun fromErrorBody(body: ResponseBody?) = gson.fromJson(body?.charStream(), ErrorResponse::class.java)
    }
}