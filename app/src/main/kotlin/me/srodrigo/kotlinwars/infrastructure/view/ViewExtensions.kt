package me.srodrigo.kotlinwars.infrastructure.view

import android.app.Activity
import android.widget.Toast
import me.srodrigo.kotlinwars.KotlinWarsApp

fun Activity.app() = application as KotlinWarsApp

fun Activity.showMessage(messageResId: Int) {
	Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
}

fun Activity.showMessage(message: String) {
	Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
