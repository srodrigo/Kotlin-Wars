package me.srodrigo.kotlinwars.people

import me.srodrigo.kotlinwars.LoadingView
import me.srodrigo.kotlinwars.PresenterView
import me.srodrigo.kotlinwars.model.people.Person

interface PeopleListView : PresenterView, LoadingView {
	fun initPeopleListView()

	fun refreshPeopleList(peopleList: List<Person>)

	fun showPeopleEmptyView()
}

