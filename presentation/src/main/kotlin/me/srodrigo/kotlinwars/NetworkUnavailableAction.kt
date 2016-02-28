package me.srodrigo.kotlinwars

import me.srodrigo.kotlinwars.actions.NetworkUnavailableError
import me.srodrigo.kotlinwars.infrastructure.CommandErrorAction

class NetworkUnavailableAction(val view: PresenterView) : CommandErrorAction<NetworkUnavailableError> {

	override fun onError(error: NetworkUnavailableError) {
		view.showNetworkUnavailableError()
	}
}