package me.srodrigo.kotlinwars.model.people

import me.srodrigo.kotlinwars.actions.NetworkUnavailableError
import me.srodrigo.kotlinwars.actions.people.GetPeopleError
import me.srodrigo.kotlinwars.actions.people.GetPeopleResponse
import me.srodrigo.kotlinwars.infrastructure.ApiException
import me.srodrigo.kotlinwars.infrastructure.ApiNetworkUnavailableException


internal class PeopleService(private val peopleApiRepository: PeopleApiRepository) {

	fun getPeople(): GetPeopleResponse {
		try {
			val people = peopleApiRepository.getPeople()
			return GetPeopleResponse(people)
		} catch(e: ApiNetworkUnavailableException) {
			return GetPeopleResponse(error = NetworkUnavailableError())
		} catch(e: ApiException) {
			return GetPeopleResponse(error = GetPeopleError())
		}
	}
}
