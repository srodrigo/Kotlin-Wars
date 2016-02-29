package me.srodrigo.kotlinwars.testutils

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import me.srodrigo.kotlinwars.ApiServiceLocator
import me.srodrigo.kotlinwars.CommandThreadPoolExecutor
import me.srodrigo.kotlinwars.KotlinWarsApp
import me.srodrigo.kotlinwars.infrastructure.ExecutionThread
import java.util.concurrent.ExecutorService

class CustomActivityTestRule<T : Activity>(
		activityClass: Class<T>?,
		initialTouchMode: Boolean,
		launchActivity: Boolean)
: ActivityTestRule<T>(activityClass, initialTouchMode, launchActivity) {

	object MainThread : ExecutionThread {
		private val handler = Handler(Looper.getMainLooper())

		override fun execute(runnable: Runnable) {
			handler.post(runnable)
		}
	}

	fun launchActivity(apiServiceLocator: ApiServiceLocator) {
		injectServiceLocators(apiServiceLocator)
		super.launchActivity(Intent())
	}

	private fun injectServiceLocators(apiServiceLocator: ApiServiceLocator) {
		val instrumentation = InstrumentationRegistry.getInstrumentation()
		val app = instrumentation.targetContext.applicationContext as KotlinWarsApp
		app.executor = CommandThreadPoolExecutor(MainThread, AsyncTask.THREAD_POOL_EXECUTOR as ExecutorService)
		app.apiServiceLocator = apiServiceLocator
	}
}
