package TourismWiz.TourismWiz.network

import TourismWiz.TourismWiz.model.Restaurant
import retrofit2.http.*

interface RestaurantApiService {
    @GET("v2/Tourism/Restaurant/{city}?%24format=JSON")
    suspend fun getRestaurants(
        @Path("city") city : String? = "",
        @Query("\$top") dataInPage : Int,
        @Query("\$skip") skippedData : Int,
        @HeaderMap headers : Map<String, String>
    ): List<Restaurant>
}