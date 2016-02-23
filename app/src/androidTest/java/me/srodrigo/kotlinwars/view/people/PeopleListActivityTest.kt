package me.srodrigo.kotlinwars.view.people

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.v7.widget.RecyclerView
import android.test.suitebuilder.annotation.LargeTest
import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import me.srodrigo.kotlinwars.ApiServiceLocatorImp
import me.srodrigo.kotlinwars.R
import me.srodrigo.kotlinwars.infrastructure.JsonFile
import me.srodrigo.kotlinwars.view.CustomActivityTestRule
import me.srodrigo.kotlinwars.view.waitForHiddenView
import org.junit.Rule
import org.junit.Test
import java.net.HttpURLConnection


@LargeTest
class PeopleListActivityTest {

	@get:Rule val activityRule = CustomActivityTestRule(PeopleListActivity::class.java, true, false)

	@Test fun onRefresh_whenNetworkIsAvailable_shouldShowList() {
		val server = MockWebServer()
		server.start()
		val url = server.url("/").toString()
		enqueueGetPeopleResponse(server);
		activityRule.launchActivity(ApiServiceLocatorImp(url))

		activityRule.waitForHiddenView(R.id.peopleListView)

		val peopleListMatcher = withId(R.id.peopleListView)
		onView(peopleListMatcher).check { view, noMatchingViewException -> isDisplayed() }
		onView(peopleListMatcher).perform(
				RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.click()))

		server.shutdown()
	}

	private fun enqueueGetPeopleResponse(server: MockWebServer) {
		val assets = InstrumentationRegistry.getInstrumentation().context.assets
		val mockResponse = MockResponse()
		mockResponse.setResponseCode(HttpURLConnection.HTTP_OK)
		mockResponse.setBody(JsonFile(assets, "people/get-people-response.json").readContent())
		server.enqueue(mockResponse)
	}

}

