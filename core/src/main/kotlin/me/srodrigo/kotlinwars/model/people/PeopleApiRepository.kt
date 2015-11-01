package me.srodrigo.kotlinwars.model.people

interface PeopleApiRepository {
	fun getPeople(): List<Person>
}
