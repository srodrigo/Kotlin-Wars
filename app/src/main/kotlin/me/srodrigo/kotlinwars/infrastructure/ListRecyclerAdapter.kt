package me.srodrigo.kotlinwars.infrastructure

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import java.util.*

abstract class ListRecyclerAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

	private val data: MutableList<T> = ArrayList()

	override fun getItemCount(): Int = data.size

	fun getItem(position: Int) = data[position]

	fun set(newData: List<T>) {
		data.clear()
		data.addAll(newData)
		notifyDataSetChanged()
	}
}
