package me.srodrigo.kotlinwars

import me.srodrigo.kotlinwars.infrastructure.*
import java.util.concurrent.*

class CommandInvokerImp(private val postExecutionThread: ExecutionThread) : CommandInvoker {

	private val executor: ExecutorService =
			Executors.newScheduledThreadPool(2 * Runtime.getRuntime().availableProcessors())

	override fun <T : CommandResponse<out Any>> execute(execution: CommandExecution<T>) {
		executor.submit(CommandInvokerTask(execution, postExecutionThread))
	}
}
