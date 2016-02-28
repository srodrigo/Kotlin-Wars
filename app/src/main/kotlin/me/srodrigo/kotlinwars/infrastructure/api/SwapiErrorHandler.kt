package me.srodrigo.kotlinwars.infrastructure.api

import android.util.Log
import me.srodrigo.kotlinwars.infrastructure.ApiException
import me.srodrigo.kotlinwars.infrastructure.ApiNetworkUnavailableException
import retrofit.ErrorHandler
import retrofit.RetrofitError

class SwapiErrorHandler : ErrorHandler {

	override fun handleError(cause: RetrofitError?): Throwable? {
		if (cause != null) {
			Log.d(SwapiErrorHandler::class.java.simpleName,
					"RetrofitError. Cause: " + cause.kind + " , " + cause.message)
			if (RetrofitError.Kind.NETWORK == cause.kind) {
				return ApiNetworkUnavailableException(cause = cause)
			}

			if (cause.response != null) {
				val response = cause.response
				return ApiException(response.status, response.reason, cause)
			}
		}

		return ApiException(cause = cause)
	}
}
