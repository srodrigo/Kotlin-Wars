package me.srodrigo.kotlinwars

abstract class Presenter<T : PresenterView> {
	private var view: T? = null

	fun attachView(view: T) {
		this.view = view
		onViewAttached()
	}

	protected abstract fun onViewAttached()

	fun getView(): T = view!!
}

interface PresenterView {
	fun showGenericError()
}
