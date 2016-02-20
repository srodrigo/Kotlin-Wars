package me.srodrigo.kotlinwars.infrastructure

import java.util.*
import java.util.concurrent.Callable

interface Command<T> : Callable<T>

interface CommandResult<T> {
	fun onResult(result: T)
}

interface CommandError

class GenericError(val cause: Exception? = null) : CommandError

interface CommandErrorAction<T : CommandError> {
	fun onError(error: T)
}

open class CommandResponse<T>(val response: T? = null, val error: CommandError? = null) {
	fun hasError(): Boolean = error != null
}

interface ExecutionThread {
	fun execute(runnable: Runnable)
}

interface CommandInvoker {
	fun <T : CommandResponse<out Any>> execute(execution: CommandExecution<T>)
}

class CommandInvokerTask<T : CommandResponse<out Any>>(private val execution: CommandExecution<T>,
                                                       private val postExecutionThread: ExecutionThread) : Runnable {

	override fun run() {
		try {
			val response = execution.command.call()
			if (response.hasError()) {
				postExecutionThread.execute(Runnable { onErrorAction(execution, response) })
			} else {
				postExecutionThread.execute(Runnable { onResultAction(execution, response) })
			}
		} catch (e: Exception) {
			postExecutionThread.execute(Runnable { doGenericErrorAction(execution, GenericError(e)) })
		}
	}

	private fun <T : CommandResponse<out Any>> onResultAction(execution: CommandExecution<T>, response: T) {
		execution.commandResult.onResult(response)
	}

	private fun <T : CommandResponse<out Any>> onErrorAction(execution: CommandExecution<T>, response: T) {
		val error: CommandError = response.error!!
		val errorActions = execution.getActions(error.javaClass)
		if (errorActions == null) {
			doGenericErrorAction(execution, GenericError())
		} else {
			for (action in errorActions) {
				action.onError(error)
			}
		}
	}

	private fun <T : CommandResponse<out Any>> doGenericErrorAction(execution: CommandExecution<T>,
	                                                                genericError: GenericError) {
		val genericErrorActions = execution.getGenericErrorActions()
		if (genericErrorActions != null) {
			for (action in genericErrorActions) {
				action.onError(genericError)
			}
		}
	}
}

class CommandExecution<T : CommandResponse<out Any>>(val command: Command<T>,
                                                     val commandResult: CommandResult<T>) {

	private val genericErrorClass = GenericError::class.java

	private val errors = HashMap<Class<out CommandError>, List<CommandErrorAction<in CommandError>>> ()

	fun <E : CommandError> error(javaClass: Class<E>, vararg errorActions: CommandErrorAction<out CommandError>): CommandExecution<T> {
		val mappedActions = ArrayList<CommandErrorAction<in CommandError>>()
		for (act in errorActions) {
			mappedActions.add(castErrorAction(act))
		}
		errors[javaClass] = mappedActions
		return this
	}

	fun genericErrorActions(errorAction: CommandErrorAction<out CommandError>): CommandExecution<T> {
		errors[genericErrorClass] = listOf(castErrorAction(errorAction))
		return this
	}

	private fun castErrorAction(errorAction: CommandErrorAction<out CommandError>) =
			errorAction as CommandErrorAction<in CommandError>

	fun execute(invoker: CommandInvoker) {
		invoker.execute(this)
	}

	fun getActions(javaClass: Class<CommandError>) = errors[javaClass]

	/**
	 * Returns an optional, as it's not mandatory to be subscribed to generic errors from everywhere
	 */
	fun getGenericErrorActions() = errors[genericErrorClass]
}
