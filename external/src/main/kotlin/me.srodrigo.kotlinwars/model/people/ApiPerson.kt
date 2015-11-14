package me.srodrigo.kotlinwars.model.people

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import me.srodrigo.kotlinwars.infrastructure.ApiMapper
import me.srodrigo.kotlinwars.infrastructure.toFloatOrNull
import me.srodrigo.kotlinwars.infrastructure.toIntOrNull

class ApiPerson(
		@Expose @SerializedName("birth_year") val birthYear: String?,
		@Expose @SerializedName("eye_color") val eyeColor: String?,
		@Expose @SerializedName("height") val height: String?,
		@Expose @SerializedName("mass") val mass: String?,
		@Expose @SerializedName("name") val name: String?)

class ApiPersonMapper : ApiMapper<Person, ApiPerson> {
	override fun toDomain(apiModel: ApiPerson): Person =
		Person(apiModel.birthYear, apiModel.eyeColor, apiModel.height?.toIntOrNull(),
				apiModel.mass?.toFloatOrNull(), apiModel.name)

	override fun toApi(domainModel: Person): ApiPerson {
		throw UnsupportedOperationException()
	}
}
