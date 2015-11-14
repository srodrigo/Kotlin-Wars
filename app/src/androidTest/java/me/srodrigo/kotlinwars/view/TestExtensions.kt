package me.srodrigo.kotlinwars.view

import android.support.test.rule.ActivityTestRule
import android.view.View


fun ActivityTestRule<*>.waitForHiddenView(viewResId: Int) {
	val view = activity.findViewById(viewResId)
	while (view.visibility != View.VISIBLE) {
		Thread.sleep(200)
	}
}
