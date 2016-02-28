package me.srodrigo.kotlinwars.infrastructure.files;

import android.content.res.AssetManager
import java.io.BufferedReader
import java.io.InputStreamReader

class JsonFile(val assets: AssetManager, val filePath: String) {

	fun readContent(): String {
		val fileContent = StringBuilder()

		val inputStream = assets.open(filePath)
		val reader = BufferedReader(InputStreamReader(inputStream))
		reader.forEachLine { line -> fileContent.append(line) }

		return fileContent.toString()
	}
}
