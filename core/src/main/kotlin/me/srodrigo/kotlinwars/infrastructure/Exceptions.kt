package me.srodrigo.kotlinwars.infrastructure

open class ApiException(val statusCode: Int? = null, val reason: String? = null, cause: Throwable? = null) :
		RuntimeException(cause)

class ApiNetworkUnavailableException(cause: Throwable? = null) :
		ApiException(null, null, cause)
