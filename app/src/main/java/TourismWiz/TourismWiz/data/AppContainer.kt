package TourismWiz.TourismWiz.data

import TourismWiz.TourismWiz.network.RestaurantApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val restaurantRepository : RestaurantRepository
}

class DefaultAppContainer : AppContainer{
    private val BASE_URL = "https://tdx.transportdata.tw/api/basic/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    private val retrofitRestaurantService : RestaurantApiService by lazy {
        retrofit.create(RestaurantApiService::class.java)
    }

    override val restaurantRepository: RestaurantRepository by lazy {
        NetworkRestaurantRepository(retrofitRestaurantService)
    }
}