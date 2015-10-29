package me.srodrigo.kotlinwars.infrastructure

import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Future

interface Command<T> : Callable<T>

interface CommandResult<T> {
	fun onResult(result: T)
}

interface CommandError

interface CommandErrorAction<T : CommandError> {
	fun onError(error: T)
}

open class CommandResponse<T>(val response: T, val error: CommandError?) {
	fun hasError(): Boolean = error != null
}

class GenericError(val cause: Exception? = null) : CommandError

interface CommandInvoker {
	fun <T : CommandResponse<out Any>> execute(execution: CommandExecution<T>): Future<T>?
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

	fun execute(invoker: CommandInvoker): Future<T>? = invoker.execute(this)

	fun getAction(javaClass: Class<CommandError>) = errors[javaClass]

	/**
	 * Returns an optional, as it's not mandatory to be subscribed to generic errors from everywhere
	 */
	fun getGenericErrorAction() = errors[genericErrorClass]
}
