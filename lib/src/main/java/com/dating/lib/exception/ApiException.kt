package com.dating.lib.exception

import java.io.IOException

open class ApiException(val code: Int, message: String?, val data: String?) : IOException(message, null)

class TokenExpiresException(code: Int, message: String?, data: String?) : ApiException(code, message, data)



