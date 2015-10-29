package me.srodrigo.kotlinwars.actions.people

import me.srodrigo.kotlinwars.model.people.Person

interface PeopleApiRepository {
	fun getPeople(): List<Person>
}
