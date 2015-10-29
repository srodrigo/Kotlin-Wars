package me.srodrigo.kotlinwars.people

import me.srodrigo.kotlinwars.PresenterView
import me.srodrigo.kotlinwars.model.people.Person

interface PeopleListView : PresenterView {
	fun refreshPeopleList(peopleList: List<Person>)
}
