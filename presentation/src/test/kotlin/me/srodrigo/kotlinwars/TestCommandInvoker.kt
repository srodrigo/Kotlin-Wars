package me.srodrigo.kotlinwars

import me.srodrigo.kotlinwars.infrastructure.*

class TestCommandInvoker : CommandInvoker {
	override fun <T : CommandResponse<out Any>> execute(execution: CommandExecution<T>) {
		CommandInvokerTask(execution, object : ExecutionThread {
			override fun execute(runnable: Runnable) {
				runnable.run()
			}
		}).run()
	}
}
