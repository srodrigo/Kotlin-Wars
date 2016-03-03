package me.srodrigo.kotlinwars.testutils

import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers
import org.junit.Assert

class TestApiServer {

	companion object {
		val getPeopleResponseFilePath = "people/get-people-response.json"
	}

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
