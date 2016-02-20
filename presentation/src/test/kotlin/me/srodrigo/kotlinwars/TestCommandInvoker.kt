package me.srodrigo.kotlinwars

import me.srodrigo.kotlinwars.infrastructure.*

class TestCommandExecutor : CommandExecutor {
	override fun <T : CommandResponse<out Any>> execute(execution: CommandExecution<T>) {
		CommandExecutorTask(execution, object : ExecutionThread {
			override fun execute(runnable: Runnable) {
				runnable.run()
			}
		}).run()
	}
}
