package me.srodrigo.kotlinwars.people

import me.srodrigo.kotlinwars.GenericErrorAction
import me.srodrigo.kotlinwars.NetworkUnavailableAction
import me.srodrigo.kotlinwars.Presenter
import me.srodrigo.kotlinwars.actions.NetworkUnavailableError
import me.srodrigo.kotlinwars.actions.people.GetPeopleCommand
import me.srodrigo.kotlinwars.actions.people.GetPeopleResponse
import me.srodrigo.kotlinwars.infrastructure.*

class PeopleListPresenter(private val executor: CommandExecutor,
                          private val getPeopleCommand: GetPeopleCommand) : Presenter<PeopleListView>() {
	override fun onViewAttached() {
		getView().initPeopleListView()
	}

	fun onRefresh() {
		getView().showLoadingView()
		CommandExecution(command = getPeopleCommand,
				commandResult = object : CommandResult<GetPeopleResponse> {
					override fun onResult(result: GetPeopleResponse) {
						if (result.response!!.isEmpty()) {
							getView().showPeopleEmptyView()
							getView().hideLoadingView()
						} else {
							getView().refreshPeopleList(result.response!!)
							getView().hideLoadingView()
						}
					}
				})
				.error(NetworkUnavailableError::class.java,
						NetworkUnavailableAction(getView()),
						object : CommandErrorAction<CommandError> {
							override fun onError(error: CommandError) {
								getView().hideLoadingView()
							}
						})
				.genericErrorActions(GenericErrorAction(getView()))
				.execute(executor)
	}
}
