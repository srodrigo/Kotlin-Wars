package me.srodrigo.kotlinwars.actions.people

import me.srodrigo.kotlinwars.model.people.Person

internal class PeopleService(private val peopleApiRepository: PeopleApiRepository) {

	fun getPeople(): List<Person> {
		return peopleApiRepository.getPeople()
	}
}
