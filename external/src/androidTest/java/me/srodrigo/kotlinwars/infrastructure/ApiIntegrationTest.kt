package me.srodrigo.kotlinwars.infrastructure

import android.test.AndroidTestCase
import android.test.suitebuilder.annotation.LargeTest
import com.squareup.okhttp.OkHttpClient
import retrofit.RestAdapter
import retrofit.client.OkClient

@LargeTest
class ApiIntegrationTest : AndroidTestCase() {

	fun testWhenApiThrowsError_shouldReturnException() {
		val restAdapter = RestAdapter.Builder()
				.setEndpoint("http://invalid.api.for.test.co")
				.setLogLevel(RestAdapter.LogLevel.NONE)
				.setClient(OkClient(OkHttpClient()))
				.setErrorHandler(SwapiErrorHandler())
				.build();
		val swapiService: SwapiService = restAdapter.create(SwapiService::class.java)

		try {
			swapiService.getPeople()
			fail()
		} catch (e: ApiException) {
			// Test ok
		}
	}
}
