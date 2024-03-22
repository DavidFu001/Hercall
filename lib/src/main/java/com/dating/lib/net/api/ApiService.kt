package com.dating.lib.net.api

import com.dating.lib.ui.callback.Api

val apiService: ApiService get() = createApi(Api.API_HOST_Path)
const val PageSize = 20

interface ApiService {

}