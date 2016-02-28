package me.srodrigo.kotlinwars.testutils

import android.app.Activity
import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import me.srodrigo.kotlinwars.ApiServiceLocator
import me.srodrigo.kotlinwars.KotlinWarsApp

class CustomActivityTestRule<T : Activity>(
		activityClass: Class<T>?,
		initialTouchMode: Boolean,
		launchActivity: Boolean)
: ActivityTestRule<T>(activityClass, initialTouchMode, launchActivity) {

	fun launchActivity(apiServiceLocator: ApiServiceLocator) {
		injectServiceLocators(apiServiceLocator)
		super.launchActivity(Intent())
	}

	private fun injectServiceLocators(apiServiceLocator: ApiServiceLocator) {
		val instrumentation = InstrumentationRegistry.getInstrumentation()
		val app = instrumentation.targetContext.applicationContext as KotlinWarsApp
		app.apiServiceLocator = apiServiceLocator
	}
}
