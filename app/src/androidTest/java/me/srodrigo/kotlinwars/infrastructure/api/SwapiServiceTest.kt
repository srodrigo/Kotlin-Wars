package me.srodrigo.kotlinwars.infrastructure.api

import android.test.AndroidTestCase
import android.test.suitebuilder.annotation.LargeTest
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.mockwebserver.MockWebServer
import me.srodrigo.kotlinwars.infrastructure.ApiException
import me.srodrigo.kotlinwars.infrastructure.files.JsonFile
import me.srodrigo.kotlinwars.model.people.ApiPerson
import me.srodrigo.kotlinwars.testutils.TestApiServer
import me.srodrigo.kotlinwars.testutils.getInstrumentationAssets
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import retrofit.RestAdapter
import retrofit.client.OkClient
import java.net.HttpURLConnection

@LargeTest
class SwapiServiceTest : AndroidTestCase() {

	fun testSendValidGetPeopleRequest() {
		// This variable is local to avoid concurrency problems if we make it instance variable
		val server = TestApiServer()
		server.start()
		enqueueGetPeopleResponse(server)

		val swapiService: SwapiService = createSwapiService(server.url())

		swapiService.getPeople()

		server.assertRequest(path = "/people", method = "GET")

		server.shutdown()
	}

	fun testParseValidGetPeopleResponse() {
		val server = TestApiServer()
		server.start()
		enqueueGetPeopleResponse(server)

		val swapiService: SwapiService = createSwapiService(server.url())

		val peopleResponse = swapiService.getPeople()

		assertValidGetPeopleResponse(peopleResponse)

		server.shutdown()
	}

	private fun enqueueGetPeopleResponse(server: TestApiServer) {
		val assets = getInstrumentationAssets()
		server.enqueueResponse(
				responseCode = HttpURLConnection.HTTP_OK,
				body = JsonFile(assets, TestApiServer.getPeopleResponseFilePath).readContent())
	}

	private fun assertValidGetPeopleResponse(peopleResponse: ApiPaginatedResponse<ApiPerson>) {
		assertThat(peopleResponse.count, `is`(2))
		assertNull(peopleResponse.previous)
		assertNull(peopleResponse.next)
		val results = peopleResponse.results!!
		assertThat(results, `is`(
				listOf(
						ApiPerson(birthYear = "19BBY", eyeColor = "blue",
								height = "172", mass = "77", name = "Luke Skywalker"),
						ApiPerson(birthYear = "112BBY", eyeColor = "yellow",
								height = "167", mass = "75", name = "C-3PO")
				)
		))
	}

	fun testWhenApiThrowsError_shouldReturnException() {
		val server = MockWebServer()
		server.start()

		val swapiService: SwapiService = createSwapiService("http://invalid.api.for.test.co")

		try {
			swapiService.getPeople()
			fail()
		} catch (e: ApiException) {
			// Test ok
		}

		server.shutdown()
	}

	private fun createSwapiService(url: String): SwapiService {
		val restAdapter = RestAdapter.Builder()
				.setEndpoint(url)
				.setLogLevel(RestAdapter.LogLevel.NONE)
				.setClient(OkClient(OkHttpClient()))
				.setErrorHandler(SwapiErrorHandler())
				.build();
		return restAdapter.create(SwapiService::class.java)
	}
}
