package TourismWiz.TourismWiz.data

import RestaurantApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit

interface AppContainer {
    val restaurantRepository : RestaurantRepository
}

class DefaultAppContainer : AppContainer{
    private val BASE_URL = "https://tdx.transportdata.tw/api/basic/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
        .baseUrl(BASE_URL)
        .build()

    private val retrofitRestaurantService : RestaurantApiService by lazy {
        retrofit.create(RestaurantApiService::class.java)
    }

    override val restaurantRepository: RestaurantRepository by lazy {
        NetworkRestaurantRepository(retrofitRestaurantService)
    }
}