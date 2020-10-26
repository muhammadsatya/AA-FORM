package com.mobile.aa.survey.form.ApiService

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.POST
import retrofit2.http.Multipart
import retrofit2.http.PartMap





interface ApiInterface {
    @GET
    fun get(@Url url: String, @QueryMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST
    fun post(@Url url: String, @FieldMap params: Map<String, String>): Call<ResponseBody>

    @Multipart
    @POST("api/form/file")
    fun uploadFileWithPartMap(
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part file: MultipartBody.Part
    ): Call<ResponseBody>
}