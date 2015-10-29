package me.srodrigo.kotlinwars.people

import me.srodrigo.kotlinwars.GenericErrorAction
import me.srodrigo.kotlinwars.Presenter
import me.srodrigo.kotlinwars.actions.people.GetPeopleCommand
import me.srodrigo.kotlinwars.actions.people.GetPeopleResponse
import me.srodrigo.kotlinwars.infrastructure.CommandExecution
import me.srodrigo.kotlinwars.infrastructure.CommandInvoker
import me.srodrigo.kotlinwars.infrastructure.CommandResult

class PeopleListPresenter(private val invoker: CommandInvoker,
                          private val getPeopleCommand: GetPeopleCommand) : Presenter<PeopleListView>() {
	override fun onViewAttached() {
		getView().initPeopleListView()
	}

	fun onRefresh() {
		getView().showLoadingView()
		CommandExecution(command = getPeopleCommand,
				commandResult = object : CommandResult<GetPeopleResponse> {
					override fun onResult(result: GetPeopleResponse) {
						if (result.response.isEmpty()) {
							getView().showPeopleEmptyView()
						} else{
							getView().refreshPeopleList(result.response)
						}
					}

				})
				.genericErrorAction(GenericErrorAction(getView()))
				.execute(invoker)
	}
}
