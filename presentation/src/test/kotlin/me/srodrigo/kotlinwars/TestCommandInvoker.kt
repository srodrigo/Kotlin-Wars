package me.srodrigo.kotlinwars

import me.srodrigo.kotlinwars.infrastructure.*
import java.util.concurrent.Future

class TestCommandInvoker : CommandInvoker {
	override fun <T : CommandResponse<out Any>> execute(execution: CommandExecution<T>): Future<T>? {
		try {
			val response = execution.command.call()
			if (response.hasError()) {
				onErrorAction(execution, response)
			} else {
				onResultAction(execution, response)
			}
		} catch (e: Exception) {
			doGenericErrorAction(execution, GenericError(e))
		}

		return null
	}

	private fun <T : CommandResponse<out Any>> onResultAction(execution: CommandExecution<T>, response: T) {
		execution.commandResult.onResult(response)
	}

	private fun <T : CommandResponse<out Any>> onErrorAction(execution: CommandExecution<T>, response: T) {
		val error: CommandError = response.error!!
		val errorAction = execution.getAction(error.javaClass)
		errorAction?.onError(error) ?: doGenericErrorAction(execution, GenericError())
	}

	private fun <T : CommandResponse<out Any>> doGenericErrorAction(execution: CommandExecution<T>,
	                                                                genericError: GenericError) {
		execution.getGenericErrorAction()?.onError(genericError)
	}
}
