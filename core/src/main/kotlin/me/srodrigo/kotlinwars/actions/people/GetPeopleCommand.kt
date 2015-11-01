package me.srodrigo.kotlinwars.actions.people

import me.srodrigo.kotlinwars.infrastructure.Command
import me.srodrigo.kotlinwars.infrastructure.CommandError
import me.srodrigo.kotlinwars.infrastructure.CommandResponse
import me.srodrigo.kotlinwars.model.people.Person

class GetPeopleCommand private constructor(
		private val peopleService: PeopleService) : Command<GetPeopleResponse> {

	companion object Builder {
		fun create(peopleApiRepository: PeopleApiRepository): GetPeopleCommand {
			return GetPeopleCommand(PeopleService(peopleApiRepository))
		}
	}

	override fun call(): GetPeopleResponse {
		val peopleList = peopleService.getPeople()
		return GetPeopleResponse(peopleList)
	}
}

class GetPeopleResponse(peopleList: List<Person>, error: CommandError? = null) : CommandResponse<List<Person>>(peopleList, error)
