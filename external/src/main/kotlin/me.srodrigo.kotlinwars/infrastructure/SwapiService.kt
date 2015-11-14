package me.srodrigo.kotlinwars.infrastructure

import me.srodrigo.kotlinwars.model.people.ApiPerson
import retrofit.http.GET

interface SwapiService {

	@GET("/people/")
	fun getPeople(): ApiPaginatedResponse<ApiPerson>
}
