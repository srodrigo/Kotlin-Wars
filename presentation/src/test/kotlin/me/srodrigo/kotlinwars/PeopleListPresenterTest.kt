package me.srodrigo.kotlinwars

import me.srodrigo.kotlinwars.actions.people.GetPeopleCommand
import me.srodrigo.kotlinwars.actions.people.PeopleApiRepository
import me.srodrigo.kotlinwars.infrastructure.CommandInvoker
import me.srodrigo.kotlinwars.model.people.Person
import me.srodrigo.kotlinwars.people.PeopleListPresenter
import me.srodrigo.kotlinwars.people.PeopleListView
import org.junit.Before

import org.junit.Test
import org.mockito.BDDMockito.*
import java.util.*
import kotlin.properties.Delegates

class PeopleListPresenterTest {

	var invoker: CommandInvoker by Delegates.notNull<CommandInvoker>()
	var presenter: PeopleListPresenter by Delegates.notNull<PeopleListPresenter>()
	var view: PeopleListView by Delegates.notNull<PeopleListView>()
	var peopleApiRepository: PeopleApiRepository by Delegates.notNull<PeopleApiRepository>()

	@Before fun setUp() {
		invoker = TestCommandInvoker()
		peopleApiRepository = mock(PeopleApiRepository::class.java)
		presenter = PeopleListPresenter(invoker, GetPeopleCommand(peopleApiRepository))
		view = mock(PeopleListView::class.java)
	}

	@Test fun onRefresh_whenThereAreResults_shouldRefreshList() {
		givenPeopleApiRepositoryReturnsResults(peopleApiRepository)

		attachAndVerifyInitialization()
		presenter.onRefresh()

		verify(view).showLoadingView()
		verify(view).refreshPeopleList(anyListOf(Person::class.java))
		verifyNoMoreInteractions(view)
	}

	private fun givenPeopleApiRepositoryReturnsResults(peopleRepository: PeopleApiRepository) {
		val peopleList = listOf(Person(), Person())
		given(peopleRepository.getPeople()).willReturn(peopleList)
	}

	@Test fun onRefresh_whenThereAreNoResults_shouldShowEmptyView() {
		givenPeopleApiRepositoryReturnsEmptyResults(peopleApiRepository)

		attachAndVerifyInitialization()
		presenter.onRefresh()

		verify(view).showLoadingView()
		verify(view).showPeopleEmptyView()
		verifyNoMoreInteractions(view)
	}

	private fun attachAndVerifyInitialization() {
		presenter.attachView(view)
		verify(view).initPeopleListView()
	}

	private fun givenPeopleApiRepositoryReturnsEmptyResults(peopleRepository: PeopleApiRepository) {
		given(peopleRepository.getPeople()).willReturn(Collections.emptyList())
	}

}
