package com.mobile.aa.survey.form.ApiService

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.QueryMap
import retrofit2.http.Url
import java.util.concurrent.TimeUnit

class ApiRequest(project: String) {
    companion object{
        val API_GET_QUESTION = "api/form/question"
        val API_POST_QUESTION = "api/form/createQuestion"
        val API_POST_ANSWER = "api/form/answer"
        val API_POST_FILE = "api/form/file"

        val PARAMS_AUTH_TOKEN = "davyOAuthToken"
        val PARAMS_DEVICE_ID = "davyDeviceId"
        val PARAMS_USER_ID = "davyUserId"
        val PARAMS_USERNAME = "davyUsername"
        val PARAMS_PASSWORD = "davyPassword"
        val PARAMS_UPASSWORD = "davyUPassTime"
        val PARAMS_PASSWORD_OLD = "davyPasswordOld"
        val PARAMS_PASSWORD_NEW = "davyPasswordNew"
        val PARAMS_AUTH_CASE = "davyAuthCase"
        val PARAMS_GROUP_ID = "davyGroupId"

        val REST_STATUS = "status"
        val REST_MESSAGE = "message"
        val REST_STATUS_SUCCESS = "Success"
    }

    val client = OkHttpClient().newBuilder()
//        .addInterceptor(HttpLoggingInterceptor().apply {
//            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
//        })
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    val retrofit = Retrofit.Builder()
        .baseUrl("https://davysystem.com/$project/")
//        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun service(): ApiInterface{
        return retrofit.create(ApiInterface::class.java)
    }
}