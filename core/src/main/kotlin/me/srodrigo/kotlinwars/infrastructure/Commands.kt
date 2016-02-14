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
		val errorAction = execution.getAction(error.javaClass)
		errorAction?.onError(error) ?: doGenericErrorAction(execution, GenericError())
	}

	private fun <T : CommandResponse<out Any>> doGenericErrorAction(execution: CommandExecution<T>,
	                                                                genericError: GenericError) {
		execution.getGenericErrorAction()?.onError(genericError)
	}
}

class CommandExecution<T : CommandResponse<out Any>>(val command: Command<T>,
                                                     val commandResult: CommandResult<T>) {

	private val genericErrorClass = GenericError::class.java

	private val errors = HashMap<Class<out CommandError>, CommandErrorAction<in CommandError>> ()

	fun <E : CommandError> error(javaClass: Class<E>, errorAction: CommandErrorAction<out CommandError>): CommandExecution<T> {
		errors[javaClass] = castErrorAction(errorAction)
		return this
	}

	fun genericErrorAction(errorAction: CommandErrorAction<out CommandError>): CommandExecution<T> {
		errors[genericErrorClass] = castErrorAction(errorAction)
		return this
	}

	private fun castErrorAction(errorAction: CommandErrorAction<out CommandError>) =
			errorAction as CommandErrorAction<in CommandError>

	fun execute(invoker: CommandInvoker) {
		invoker.execute(this)
	}

	fun getAction(javaClass: Class<CommandError>) = errors[javaClass]

	/**
	 * Returns an optional, as it's not mandatory to be subscribed to generic errors from everywhere
	 */
	fun getGenericErrorAction() = errors[genericErrorClass]
}
