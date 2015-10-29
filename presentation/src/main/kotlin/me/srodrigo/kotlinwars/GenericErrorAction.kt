package me.srodrigo.kotlinwars

import me.srodrigo.kotlinwars.infrastructure.CommandErrorAction
import me.srodrigo.kotlinwars.infrastructure.GenericError

class GenericErrorAction(val view: PresenterView) : CommandErrorAction<GenericError> {
	override fun onError(error: GenericError) {
		view.showGenericError()
	}
}
