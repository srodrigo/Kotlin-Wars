package me.srodrigo.kotlinwars

import com.squareup.okhttp.OkHttpClient
import me.srodrigo.kotlinwars.infrastructure.SwapiErrorHandler
import me.srodrigo.kotlinwars.infrastructure.SwapiService
import me.srodrigo.kotlinwars.model.people.PeopleApiRepository
import me.srodrigo.kotlinwars.model.people.PeopleSwapiApiRepository
import retrofit.RestAdapter
import retrofit.client.OkClient

interface ApiServiceLocator {
	fun createPeopleApiRepository() : PeopleApiRepository
}

class ApiServiceLocatorImp(private val endpointUrl: String) : ApiServiceLocator {

	private val restAdapter = RestAdapter.Builder()
			.setEndpoint(endpointUrl)
			.setLogLevel(if (BuildConfig.DEBUG) RestAdapter.LogLevel.FULL else RestAdapter.LogLevel.NONE)
			.setClient(OkClient(OkHttpClient()))
			.setErrorHandler(SwapiErrorHandler())
			.build();

	private val swapiService: SwapiService = restAdapter.create(SwapiService::class.java)

	override fun createPeopleApiRepository() = PeopleSwapiApiRepository(swapiService)
}
