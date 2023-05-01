package TourismWiz.TourismWiz.network

import TourismWiz.TourismWiz.model.Restaurant
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path

interface HotelApiService {
    @GET("v2/Tourism/Restaurant/{city}?%24top=100&%24format=JSON")
    suspend fun getRestaurants(
        @Path("city") city: String? = "Taoyuan",
        @HeaderMap headers: Map<String, String>
    ): List<Restaurant>
}