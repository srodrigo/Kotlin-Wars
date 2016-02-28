package me.srodrigo.kotlinwars.model.people

import me.srodrigo.kotlinwars.infrastructure.ApiException
import me.srodrigo.kotlinwars.infrastructure.api.ApiPaginatedResponse
import me.srodrigo.kotlinwars.infrastructure.api.SwapiService
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*
import kotlin.properties.Delegates

class PeopleSwapiApiRepositoryTest {

	var swapiService: SwapiService by Delegates.notNull<SwapiService>()
	var repository: PeopleSwapiApiRepository by Delegates.notNull<PeopleSwapiApiRepository>()

	@Before
	fun setUp() {
		swapiService = mock(SwapiService::class.java)
		repository = PeopleSwapiApiRepository(swapiService)
	}

	@Test
	fun callRemoteService() {
		givenApiServiceGetPeopleReturnsTwoElements()

		repository.getPeople()

		verify(swapiService).getPeople()
		verifyNoMoreInteractions(swapiService)
	}

	@Test
	fun returnTwoPeople() {
		givenApiServiceGetPeopleReturnsTwoElements()

		val people = repository.getPeople()

		assertThat(people.size, `is`(2))
	}

	@Test(expected = ApiException::class)
	fun throwRepositoryExceptionWhenThereIsAnError() {
		givenApiServiceThrowsException()

		repository.getPeople()
	}

	private fun givenApiServiceGetPeopleReturnsTwoElements() {
		val response = ApiPaginatedResponse(count = 2, next = null, previous = null,
				results = listOf(mockApiPerson(), mockApiPerson()))
		given(swapiService.getPeople()).willReturn(response)
	}

	private fun mockApiPerson() =
			ApiPerson(birthYear = "2090", eyeColor = "Blue", height = "185", mass = "79", name = "Test name")

	private fun givenApiServiceThrowsException() {
		given(swapiService.getPeople()).willThrow(ApiException::class.java)
	}

}
