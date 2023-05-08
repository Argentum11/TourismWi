package TourismWiz.TourismWiz.network

import TourismWiz.TourismWiz.model.Hotel
import TourismWiz.TourismWiz.model.Restaurant
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path

interface HotelApiService {
    @GET("v2/Tourism/Hotel/{city}?%24top=500&%24format=JSON")

    suspend fun getHotels(
        @Path("city") city: String? = "",
        @HeaderMap headers: Map<String, String>
    ): List<Hotel>
}