package me.srodrigo.kotlinwars

import me.srodrigo.kotlinwars.infrastructure.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class CommandInvokerImp(private val postExecutionThread: ExecutionThread) : CommandInvoker {

	private val corePoolSize = 3
	private val maxPoolSize = 4
	private val keepAliveTime = 120L
	private val timeUnit = TimeUnit.SECONDS
	private val workQueue = LinkedBlockingQueue<Runnable>()
	private val threadPoolExecutor =
			ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, timeUnit, workQueue)

	override fun <T : CommandResponse<out Any>> execute(execution: CommandExecution<T>) {
		threadPoolExecutor.submit(CommandInvokerTask(execution, postExecutionThread))
	}
}
