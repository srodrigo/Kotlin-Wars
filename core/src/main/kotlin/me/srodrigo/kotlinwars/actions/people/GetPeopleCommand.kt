package me.srodrigo.kotlinwars.actions.people

import me.srodrigo.kotlinwars.infrastructure.Command
import me.srodrigo.kotlinwars.infrastructure.CommandError
import me.srodrigo.kotlinwars.infrastructure.CommandResponse
import me.srodrigo.kotlinwars.model.people.PeopleApiRepository
import me.srodrigo.kotlinwars.model.people.PeopleService
import me.srodrigo.kotlinwars.model.people.Person

class GetPeopleCommand private constructor(
		private val peopleService: PeopleService) : Command<GetPeopleResponse> {

	companion object Builder {
		fun create(peopleApiRepository: PeopleApiRepository): GetPeopleCommand {
			return GetPeopleCommand(PeopleService(peopleApiRepository))
		}
	}

	override fun call(): GetPeopleResponse {
		return peopleService.getPeople()
	}
}

class GetPeopleResponse(peopleList: List<Person>? = null, error: CommandError? = null) : CommandResponse<List<Person>>(peopleList, error)

class GetPeopleError : CommandError
