package me.srodrigo.kotlinwars.view.people

import kotlinx.android.synthetic.activity_people_list.peopleSwipeLayout
import kotlinx.android.synthetic.activity_people_list.peopleListView
import kotlinx.android.synthetic.activity_people_list.peopleEmptyView
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import me.srodrigo.kotlinwars.R
import me.srodrigo.kotlinwars.infrastructure.ViewStateHandler
import me.srodrigo.kotlinwars.model.people.Person

class PeopleListActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

	val peopleListAdapter = PeopleListAdapter()
	val peopleListStateHolder = ViewStateHandler<PeopleListState>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_people_list)

		initToolbar()
		initSwipeLayout()
		initPeopleList()
	}

	private fun initToolbar() {
		val toolbar = findViewById(R.id.toolbar) as? Toolbar
		if (toolbar != null) {
			setSupportActionBar(toolbar)
			supportActionBar.setDisplayHomeAsUpEnabled(false)
		}
	}

	private fun initSwipeLayout() {
		peopleSwipeLayout.setOnRefreshListener(this)
		peopleSwipeLayout.isEnabled = true
	}

	private fun initPeopleList() {
		peopleListView.layoutManager = LinearLayoutManager(this)
		peopleListView.adapter = peopleListAdapter
		peopleListView.itemAnimator = DefaultItemAnimator()
		peopleListView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

		})

		peopleListAdapter.set(listOf(Person(), Person()))

		peopleListStateHolder.bind(PeopleListState.EMPTY, peopleEmptyView)
		peopleListStateHolder.bind(PeopleListState.LIST, peopleListView)
		peopleListStateHolder.show(PeopleListState.LIST)
	}

	override fun onRefresh() {
		initPeopleList()
	}

	enum class PeopleListState {
		EMPTY,
		LIST
	}
}
