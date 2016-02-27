package me.srodrigo.kotlinwars.infrastructure

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ApiPaginatedResponse<T>(
		@Expose @SerializedName("count") val count: Int,
		@Expose @SerializedName("next") val next: String?,
		@Expose @SerializedName("previous") val previous: String?,
		@Expose @SerializedName("results") val results: List<T>?)

interface ApiMapper<DM, AM> {
	fun toDomain(apiModel: AM): DM

	fun toApi(domainModel: DM): AM
}
