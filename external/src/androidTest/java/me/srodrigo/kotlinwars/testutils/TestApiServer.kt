package me.srodrigo.kotlinwars.testutils

import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers
import org.junit.Assert

/**
 * This class should be reused in the app module.
 * The same thing happens when trying to reuse test assets for both integration
 * and acceptance tests.
 * Both modules should be merged into one, and then move this file into the correct folder.
 */
class TestApiServer {

	val server = MockWebServer()

	fun start() {
		server.start()
	}

	fun shutdown() {
		server.shutdown()
	}

	fun enqueueResponse(responseCode: Int, body: String) {
		val mockResponse = MockResponse()
		mockResponse.setResponseCode(responseCode)
		mockResponse.setBody(body)
		server.enqueue(mockResponse)
	}

	fun assertRequest(path: String, method: String) {
		val recordedRequest = server.takeRequest()
		Assert.assertThat(recordedRequest.path, CoreMatchers.`is`(path))
		Assert.assertThat(recordedRequest.method, CoreMatchers.`is`(method))
	}

	fun url() = server.url("/").toString()

}
