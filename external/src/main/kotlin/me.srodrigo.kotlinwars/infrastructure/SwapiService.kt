package me.srodrigo.kotlinwars.infrastructure

import me.srodrigo.kotlinwars.model.people.ApiPerson
import retrofit.http.GET

interface SwapiService {

	companion object Urls {
		const val PEOPLE_URL = "/people"
	}

	@GET(Urls.PEOPLE_URL)
	fun getPeople(): ApiPaginatedResponse<ApiPerson>
}
