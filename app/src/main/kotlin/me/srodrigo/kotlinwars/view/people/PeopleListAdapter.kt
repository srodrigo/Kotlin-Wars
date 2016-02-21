package me.srodrigo.kotlinwars.view.people

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_people.view.*
import me.srodrigo.kotlinwars.R
import me.srodrigo.kotlinwars.infrastructure.ListRecyclerAdapter
import me.srodrigo.kotlinwars.model.people.Person


class PeopleListAdapter : ListRecyclerAdapter<Person, PeopleListAdapter.ViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
		val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_people, parent, false)
		return ViewHolder(view)
	}

	override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
		holder?.bind(getItem(position))
	}

	class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
		fun bind(item: Person) {
			itemView.nameView.text = item.name
			itemView.extraDataView.text = itemView.context.getString(
					R.string.height_and_mass, item.height, item.mass)
		}

	}
}
