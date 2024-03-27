package com.dating.lib.net.api

import com.dating.lib.bean.ApiResult
import com.dating.lib.bean.UserBean
import com.dating.lib.ui.callback.Api
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.QueryMap

val apiService: ApiService get() = createApi(Api.API_HOST_Path)
const val PageSize = 20

interface ApiService {
    //找回用户
    @GET("/ldetmuho/rarnd/bemuyc/")
    suspend fun hasRegister(): ApiResult<UserBean>

    //用户注册
    @Multipart
    @POST("/ldetmuho/rarnd/o/")
    suspend fun userRegister(
        @QueryMap map: MutableMap<String, String>,
        @Part image: MultipartBody.Part? = null,
        @Part("file_type") fileType: Int = 2,
    ): ApiResult<UserBean>


}