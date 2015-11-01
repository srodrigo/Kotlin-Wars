package me.srodrigo.kotlinwars.model.people

internal class PeopleService(private val peopleApiRepository: PeopleApiRepository) {

	fun getPeople(): List<Person> {
		return peopleApiRepository.getPeople()
	}
}
