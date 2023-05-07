package TourismWiz.TourismWiz.data

import TourismWiz.TourismWiz.network.HotelApiService
import TourismWiz.TourismWiz.network.RestaurantApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit

interface AppContainer {
    val restaurantRepository : RestaurantRepository
    val hotelRepository : HotelRepository
}

class DefaultAppContainer : AppContainer{
    private val BASE_URL = "https://tdx.transportdata.tw/api/basic/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
        .baseUrl(BASE_URL)
        .build()

    /* Restaurant */
    private val retrofitRestaurantService : RestaurantApiService by lazy {
        retrofit.create(RestaurantApiService::class.java)
    }

    override val restaurantRepository: RestaurantRepository by lazy {
        NetworkRestaurantRepository(retrofitRestaurantService)
    }

    /* Hotel */
    private val retrofitHotelService : HotelApiService by lazy{
        retrofit.create(HotelApiService::class.java)
    }

    override val hotelRepository: HotelRepository by lazy {
        NetworkHotelRepository(retrofitHotelService)
    }
}