package me.srodrigo.kotlinwars

import android.app.Application
import android.os.Handler
import me.srodrigo.kotlinwars.actions.people.GetPeopleCommand
import me.srodrigo.kotlinwars.infrastructure.CommandInvoker
import me.srodrigo.kotlinwars.infrastructure.ExecutionThread
import me.srodrigo.kotlinwars.model.people.PeopleApiRepository
import kotlin.properties.Delegates

class KotlinWarsApp : Application() {

	object MainThread : ExecutionThread {
		private val handler = Handler()

		override fun execute(runnable: Runnable) {
			handler.post(runnable)
		}
	}

	val invoker: CommandInvoker = CommandInvokerImp(MainThread)
	var apiServiceLocator: ApiServiceLocator by Delegates.notNull<ApiServiceLocator>()

	private var _peopleApiRepository: PeopleApiRepository? = null
	val peopleApiRepository: PeopleApiRepository
		get() {
			if (_peopleApiRepository == null) {
				_peopleApiRepository = apiServiceLocator.createPeopleApiRepository()
			}
			return _peopleApiRepository ?: throw AssertionError("Null")
		}

	override fun onCreate() {
		super.onCreate()
		apiServiceLocator = ApiServiceLocatorImp()
	}

	fun createGetPeopleCommand(): GetPeopleCommand = GetPeopleCommand.create(peopleApiRepository)

}
