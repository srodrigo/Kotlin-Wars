package me.srodrigo.kotlinwars.actions.people

import me.srodrigo.kotlinwars.infrastructure.Command
import me.srodrigo.kotlinwars.infrastructure.CommandError
import me.srodrigo.kotlinwars.infrastructure.CommandResponse
import me.srodrigo.kotlinwars.model.people.Person

class GetPeopleCommand(private val peopleApiRepository: PeopleApiRepository) : Command<GetPeopleResponse> {
	override fun call(): GetPeopleResponse {
		val peopleList = peopleApiRepository.getPeople()
		return GetPeopleResponse(peopleList)
	}
}

class GetPeopleResponse(peopleList: List<Person>, error: CommandError? = null) : CommandResponse<List<Person>>(peopleList, error)
