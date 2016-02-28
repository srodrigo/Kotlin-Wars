package me.srodrigo.kotlinwars.actions.people

import me.srodrigo.kotlinwars.actions.NetworkUnavailableError
import me.srodrigo.kotlinwars.infrastructure.ApiException
import me.srodrigo.kotlinwars.infrastructure.ApiNetworkUnavailableException
import me.srodrigo.kotlinwars.model.people.PeopleApiRepository
import me.srodrigo.kotlinwars.model.people.Person
import org.junit.Assert
import org.junit.Test
import java.util.*

class GetPeopleCommandTest {

	@Test fun call_whenApiReturnsResults_shouldReturnList() {
		val getPeopleCommand = GetPeopleCommand.Builder.create(PeopleApiRepositoryWithResultsMock())

		val response = getPeopleCommand.call()

		Assert.assertTrue(response.response!!.size > 0)
	}

	@Test fun call_whenApiThrowsApiError_shouldReturnError() {
		val getPeopleCommand = GetPeopleCommand.Builder.create(PeopleApiRepositoryErrorMock())

		val response = getPeopleCommand.call()

		Assert.assertTrue(response.hasError())
		// It seems like Mockito.any(GetPeopleError::class.java) is not working
		Assert.assertTrue(response.error is GetPeopleError)
	}

	@Test fun call_networkIsUnavailable_shouldReturnError() {
		val getPeopleCommand = GetPeopleCommand.Builder.create(PeopleApiRepositoryNetworkUnavailableMock())

		val response = getPeopleCommand.call()

		Assert.assertTrue(response.hasError())
		Assert.assertTrue(response.error is NetworkUnavailableError)
	}

}

class PeopleApiRepositoryWithResultsMock : PeopleApiRepository {

	override fun getPeople(): List<Person> = Arrays.asList(Person(), Person())
}

class PeopleApiRepositoryErrorMock : PeopleApiRepository {

	override fun getPeople(): List<Person> = throw ApiException()
}

class PeopleApiRepositoryNetworkUnavailableMock : PeopleApiRepository {

	override fun getPeople(): List<Person> = throw ApiNetworkUnavailableException()
}
