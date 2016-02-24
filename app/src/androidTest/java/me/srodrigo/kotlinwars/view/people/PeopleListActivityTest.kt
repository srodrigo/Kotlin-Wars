package me.srodrigo.kotlinwars.view.people

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.v7.widget.RecyclerView
import android.test.suitebuilder.annotation.LargeTest
import me.srodrigo.kotlinwars.ApiServiceLocatorImp
import me.srodrigo.kotlinwars.R
import me.srodrigo.kotlinwars.infrastructure.JsonFile
import me.srodrigo.kotlinwars.infrastructure.TestApiServer
import me.srodrigo.kotlinwars.view.CustomActivityTestRule
import me.srodrigo.kotlinwars.view.waitForHiddenView
import org.junit.Rule
import org.junit.Test
import java.net.HttpURLConnection


@LargeTest
class PeopleListActivityTest {

	companion object {
		val getPeopleResponseFilePath = "people/get-people-response.json"
	}

	@get:Rule val activityRule = CustomActivityTestRule(PeopleListActivity::class.java, true, false)

	@Test fun onRefresh_whenNetworkIsAvailable_shouldShowList() {
		val server = TestApiServer()
		server.start()
		enqueueGetPeopleResponse(server)
		activityRule.launchActivity(ApiServiceLocatorImp(server.url()))

		activityRule.waitForHiddenView(R.id.peopleListView)

		val peopleListMatcher = withId(R.id.peopleListView)
		onView(peopleListMatcher).check { view, noMatchingViewException -> isDisplayed() }
		onView(peopleListMatcher).perform(
				RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.click()))

		server.shutdown()
	}

	private fun enqueueGetPeopleResponse(server: TestApiServer) {
		val assets = InstrumentationRegistry.getInstrumentation().context.assets
		server.enqueueResponse(
				responseCode = HttpURLConnection.HTTP_OK,
				body = JsonFile(assets, getPeopleResponseFilePath).readContent())
	}

}

