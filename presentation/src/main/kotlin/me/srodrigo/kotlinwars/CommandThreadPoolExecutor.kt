package me.srodrigo.kotlinwars

import me.srodrigo.kotlinwars.infrastructure.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CommandThreadPoolExecutor(
		private val postExecutionThread: ExecutionThread,
		private val executor: ExecutorService =
		Executors.newScheduledThreadPool(2 * Runtime.getRuntime().availableProcessors())) : CommandExecutor {

	override fun <T : CommandResponse<out Any>> execute(execution: CommandExecution<T>) {
		executor.submit(CommandExecutorTask(execution, postExecutionThread))
	}
}
