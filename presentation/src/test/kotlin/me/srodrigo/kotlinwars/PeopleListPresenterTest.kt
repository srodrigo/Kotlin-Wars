package me.srodrigo.kotlinwars

import me.srodrigo.kotlinwars.actions.people.GetPeopleCommand
import me.srodrigo.kotlinwars.model.people.Person
import me.srodrigo.kotlinwars.people.PeopleListPresenter
import me.srodrigo.kotlinwars.people.PeopleListView

import org.junit.Test
import org.mockito.Mockito.*

class PeopleListPresenterTest {

	@Test fun onRefresh_shouldDisplayResults() {
		val invoker = TestCommandInvoker();
		val getPeopleCommand = GetPeopleCommand()
		val presenter = PeopleListPresenter(invoker, getPeopleCommand)
		val view = mock(PeopleListView::class.java)
		presenter.attachView(view)
		verify(view).initPeopleListView()

		presenter.onRefresh()

		verify(view).showLoadingView()
		verify(view).refreshPeopleList(anyListOf(Person::class.java))
		verifyNoMoreInteractions(view)
	}

}
