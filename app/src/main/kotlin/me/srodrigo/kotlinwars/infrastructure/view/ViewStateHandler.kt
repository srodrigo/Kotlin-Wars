package me.srodrigo.kotlinwars.infrastructure.view

import android.view.View
import java.util.*

class ViewStateHandler<T : Enum<T>> {

	private val states: MutableMap<T, View> = HashMap()

	fun bind(state: T, view: View) {
		states[state] = view
	}

	fun show(state: T) {
		states.forEach {
			if (it.key == state) {
				it.value.visibility = View.VISIBLE
			} else {
				it.value.visibility = View.GONE
			}
		}
	}
}
