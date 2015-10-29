package me.srodrigo.kotlinwars

import me.srodrigo.kotlinwars.actions.people.GetPeopleCommand
import me.srodrigo.kotlinwars.actions.people.PeopleApiRepository
import me.srodrigo.kotlinwars.model.people.Person
import me.srodrigo.kotlinwars.people.PeopleListPresenter
import me.srodrigo.kotlinwars.people.PeopleListView

import org.junit.Test
import org.mockito.BDDMockito.*

class PeopleListPresenterTest {

	@Test fun onRefresh_whenThereAreResults_shouldRefreshList() {
		val invoker = TestCommandInvoker();
		val peopleRepository = mock(PeopleApiRepository::class.java)
		val getPeopleCommand = GetPeopleCommand(peopleRepository)
		val presenter = PeopleListPresenter(invoker, getPeopleCommand)
		val view = mock(PeopleListView::class.java)

		givenPeopleApiRepositoryReturnsResults(peopleRepository)

		presenter.attachView(view)
		verify(view).initPeopleListView()

		presenter.onRefresh()

		verify(view).showLoadingView()
		verify(view).refreshPeopleList(anyListOf(Person::class.java))
		verifyNoMoreInteractions(view)
	}

	private fun givenPeopleApiRepositoryReturnsResults(peopleRepository: PeopleApiRepository) {
		val peopleList = listOf(Person(), Person())
		given(peopleRepository.getPeople()).willReturn(peopleList)
	}

}
