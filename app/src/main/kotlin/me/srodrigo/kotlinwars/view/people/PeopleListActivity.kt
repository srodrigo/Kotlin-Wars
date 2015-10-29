package me.srodrigo.kotlinwars.view.people

import kotlinx.android.synthetic.activity_people_list.peopleSwipeLayout
import kotlinx.android.synthetic.activity_people_list.peopleListView
import kotlinx.android.synthetic.activity_people_list.peopleLoadingView
import kotlinx.android.synthetic.activity_people_list.peopleEmptyView
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import me.srodrigo.kotlinwars.CommandInvokerImp
import me.srodrigo.kotlinwars.R
import me.srodrigo.kotlinwars.actions.people.GetPeopleCommand
import me.srodrigo.kotlinwars.infrastructure.CommandInvoker
import me.srodrigo.kotlinwars.infrastructure.ExecutionThread
import me.srodrigo.kotlinwars.infrastructure.ViewStateHandler
import me.srodrigo.kotlinwars.model.people.Person
import me.srodrigo.kotlinwars.people.PeopleListPresenter
import me.srodrigo.kotlinwars.people.PeopleListView

class PeopleListActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, PeopleListView {

	object MainThread : ExecutionThread {
		private val handler = Handler()

		override fun execute(runnable: Runnable) {
			handler.post(runnable)
		}
	}

	companion object InvokerFactory {
		val invoker: CommandInvoker = CommandInvokerImp(MainThread)
	}

	val peopleListAdapter = PeopleListAdapter()
	val peopleListStateHolder = ViewStateHandler<PeopleListState>()
	val peopleListPresenter = PeopleListPresenter(InvokerFactory.invoker, GetPeopleCommand())

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_people_list)
		peopleListPresenter.attachView(this)
	}

	override fun onResume() {
		super.onResume()
		peopleListPresenter.onRefresh()
	}

	override fun onRefresh() {
		peopleListPresenter.onRefresh()
	}

	//--- View actions

	override fun initPeopleListView() {
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

		peopleListStateHolder.bind(PeopleListState.LOADING, peopleLoadingView)
		peopleListStateHolder.bind(PeopleListState.EMPTY, peopleEmptyView)
		peopleListStateHolder.bind(PeopleListState.LIST, peopleListView)
	}

	override fun refreshPeopleList(peopleList: List<Person>) {
		peopleListAdapter.set(peopleList)
		peopleListStateHolder.show(PeopleListState.LIST)
	}

	override fun showLoadingView() {
		peopleListStateHolder.show(PeopleListState.LOADING)
	}

	override fun showPeopleEmptyView() {
		peopleListStateHolder.show(PeopleListState.EMPTY)
	}

	override fun showGenericError() {
		throw UnsupportedOperationException()
	}

	enum class PeopleListState {
		LOADING,
		EMPTY,
		LIST
	}
}
