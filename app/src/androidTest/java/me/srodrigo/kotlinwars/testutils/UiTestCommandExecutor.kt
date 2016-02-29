package me.srodrigo.kotlinwars.testutils

import android.os.AsyncTask
import me.srodrigo.kotlinwars.infrastructure.*

class UiTestCommandExecutor : CommandExecutor {
	override fun <T : CommandResponse<out Any>> execute(execution: CommandExecution<T>) {
		CommandExecutorTask(execution, object : ExecutionThread {
			override fun execute(runnable: Runnable) {
				object : AsyncTask<Void, Void, Void>() {
					override fun doInBackground(vararg p0: Void?): Void? {
						runnable.run()
						return null
					}
				}.execute()
			}
		}).run()
	}
}
