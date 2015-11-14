package me.srodrigo.kotlinwars

import com.squareup.okhttp.OkHttpClient
import me.srodrigo.kotlinwars.infrastructure.SwapiService
import me.srodrigo.kotlinwars.model.people.PeopleApiRepository
import me.srodrigo.kotlinwars.model.people.PeopleApiRepositoryImp
import retrofit.RestAdapter
import retrofit.client.OkClient

interface ApiServiceLocator {
	fun createPeopleApiRepository() : PeopleApiRepository
}

class ApiServiceLocatorImp : ApiServiceLocator {

	private val restAdapter = RestAdapter.Builder()
			.setEndpoint("http://swapi.co/api")
			.setLogLevel(if (BuildConfig.DEBUG) RestAdapter.LogLevel.FULL else RestAdapter.LogLevel.NONE)
			.setClient(OkClient(OkHttpClient()))
			.build();

	private val swapiService: SwapiService = restAdapter.create(SwapiService::class.java)

	override fun createPeopleApiRepository() = PeopleApiRepositoryImp(swapiService)
}