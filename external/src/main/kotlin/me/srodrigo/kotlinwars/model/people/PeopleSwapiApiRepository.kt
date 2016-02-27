package me.srodrigo.kotlinwars.model.people

import me.srodrigo.kotlinwars.infrastructure.SwapiService
import java.util.*

class PeopleSwapiApiRepository(private val swapiService: SwapiService) : PeopleApiRepository {

	override fun getPeople(): List<Person> {
		val apiMapper = ApiPersonMapper()
		return swapiService.getPeople().results?.map { apiMapper.toDomain(it) } ?: Collections.emptyList()
	}
}

