package TourismWiz.TourismWiz.network

import TourismWiz.TourismWiz.model.ScenicSpot
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path

interface ScenicSpotApiService {
    @GET("v2/Tourism/ScenicSpot/{city}?%24top=500&%24format=JSON")

    suspend fun getScenicSpots(
        @Path("city") city: String? = "",
        @HeaderMap headers: Map<String, String>
    ): List<ScenicSpot>
}