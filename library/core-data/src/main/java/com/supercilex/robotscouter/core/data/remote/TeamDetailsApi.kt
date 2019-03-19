package com.supercilex.robotscouter.core.data.remote

import com.google.gson.annotations.SerializedName
import com.supercilex.robotscouter.core.model.Team
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface TeamDetailsApi {
    @GET("team/frc{number}")
    fun getInfoAsync(
            @Path("number") number: String,
            @Query("X-TBA-Auth-Key") auth: String
    ): Deferred<Team>

    @GET("team/frc{number}/media/{year}")
    fun getMediaAsync(
            @Path("number") number: String,
            @Path("year") year: Int,
            @Query("X-TBA-Auth-Key") auth: String
    ): Deferred<List<Media>>

    data class Media(
            @SerializedName("type") var type: String = "null",
            @SerializedName("foreign_key") var key: String? = null,
            @SerializedName("preferred") var preferred: Boolean = false,
            @SerializedName("details") var details: Details? = null
    ) {
        data class Details(@SerializedName("image_partial") var id: String = "null")
    }
}
