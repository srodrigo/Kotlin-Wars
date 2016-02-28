package me.srodrigo.kotlinwars.infrastructure

fun String.toIntOrNull() =
		try {
			toInt()
		} catch (e: NumberFormatException) {
			null
		}

fun String.toFloatOrNull() =
		try {
			toFloat()
		} catch (e: NumberFormatException) {
			null
		}
