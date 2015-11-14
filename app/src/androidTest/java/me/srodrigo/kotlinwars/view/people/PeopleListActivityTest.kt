package me.srodrigo.kotlinwars.view.people

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.v7.widget.RecyclerView
import android.test.suitebuilder.annotation.LargeTest
import me.srodrigo.kotlinwars.R
import me.srodrigo.kotlinwars.view.waitForHiddenView
import org.junit.Rule
import org.junit.Test


@LargeTest
class PeopleListActivityTest {

	@get:Rule val activityRule = ActivityTestRule(PeopleListActivity::class.java)

	@Test fun onRefresh_whenNetworkIsAvailable_shouldShowList() {
		activityRule.waitForHiddenView(R.id.peopleListView)

		val peopleListMatcher = withId(R.id.peopleListView)
		onView(peopleListMatcher).check { view, noMatchingViewException -> isDisplayed() }
		onView(peopleListMatcher).perform(
				RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.click()))
	}
}
