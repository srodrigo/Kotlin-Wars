package me.srodrigo.kotlinwars.people

import me.srodrigo.kotlinwars.TestCommandExecutor
import me.srodrigo.kotlinwars.actions.people.GetPeopleCommand
import me.srodrigo.kotlinwars.infrastructure.ApiNetworkUnavailableException
import me.srodrigo.kotlinwars.infrastructure.CommandExecutor
import me.srodrigo.kotlinwars.model.people.PeopleApiRepository
import me.srodrigo.kotlinwars.model.people.Person
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*
import java.util.*
import kotlin.properties.Delegates

class PeopleListPresenterTest {

	var executor: CommandExecutor by Delegates.notNull<CommandExecutor>()
	var presenter: PeopleListPresenter by Delegates.notNull<PeopleListPresenter>()
	var view: PeopleListView by Delegates.notNull<PeopleListView>()
	var peopleApiRepository: PeopleApiRepository by Delegates.notNull<PeopleApiRepository>()

	@Before fun setUp() {
		executor = TestCommandExecutor()
		peopleApiRepository = mock(PeopleApiRepository::class.java)
		val getPeopleCommand = GetPeopleCommand.create(peopleApiRepository)
		presenter = PeopleListPresenter(executor, getPeopleCommand)
		view = mock(PeopleListView::class.java)
	}

	@Test fun onRefresh_whenThereAreResults_shouldRefreshList() {
		givenPeopleApiRepositoryReturnsResults(peopleApiRepository)

		attachAndVerifyInitialization()
		presenter.onRefresh()

		verify(view).showLoadingView()
		verify(view).refreshPeopleList(anyListOf(Person::class.java))
		verify(view).hideLoadingView()
		verifyNoMoreInteractions(view)
	}

	@Test fun onRefresh_whenThereAreNoResults_shouldShowEmptyView() {
		givenPeopleApiRepositoryReturnsEmptyResults(peopleApiRepository)

		attachAndVerifyInitialization()
		presenter.onRefresh()

		verify(view).showLoadingView()
		verify(view).showPeopleEmptyView()
		verify(view).hideLoadingView()
		verifyNoMoreInteractions(view)
	}

	@Test fun onRefresh_whenNetworkIsUnavailable_shouldShowError() {
		givenNetworkIsUnavailable(peopleApiRepository)

		attachAndVerifyInitialization()
		presenter.onRefresh()

		verify(view).showLoadingView()
		verify(view).showNetworkUnavailableError()
		verify(view).hideLoadingView()
		verifyNoMoreInteractions(view)
	}

	private fun attachAndVerifyInitialization() {
		presenter.attachView(view)
		verify(view).initPeopleListView()
	}

	private fun givenPeopleApiRepositoryReturnsResults(peopleRepository: PeopleApiRepository) {
		val peopleList = listOf(Person(), Person())
		given(peopleRepository.getPeople()).willReturn(peopleList)
	}

	private fun givenPeopleApiRepositoryReturnsEmptyResults(peopleRepository: PeopleApiRepository) {
		given(peopleRepository.getPeople()).willReturn(Collections.emptyList())
	}

	private fun givenNetworkIsUnavailable(peopleRepository: PeopleApiRepository) {
		given(peopleRepository.getPeople()).willThrow(ApiNetworkUnavailableException::class.java)
	}

}
