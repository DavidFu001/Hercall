package com.dating.lib.bean

import com.google.gson.annotations.SerializedName

data class ApiResult<T>(
   @SerializedName("msg")
   val msg: String = "",
   @SerializedName("p")
   val code: Int = 0,
   @SerializedName("bcmt")
   val data : T? = null
)
