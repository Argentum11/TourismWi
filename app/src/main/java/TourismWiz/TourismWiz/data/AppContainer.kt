package TourismWiz.TourismWiz.data

import TourismWiz.TourismWiz.network.HotelApiService
import TourismWiz.TourismWiz.network.RestaurantApiService
import TourismWiz.TourismWiz.network.ScenicSpotApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val restaurantRepository : RestaurantRepository
    val hotelRepository : HotelRepository
    val scenicSpotRepository : ScenicSpotRepository
}

class DefaultAppContainer : AppContainer{
    private val BASE_URL = "https://tdx.transportdata.tw/api/basic/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
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

    /* ScenicSpot */
    private val retrofitScenicSpotApiService : ScenicSpotApiService by lazy{
        retrofit.create(ScenicSpotApiService::class.java)
    }

    override val scenicSpotRepository: ScenicSpotRepository by lazy {
        NetworkScenicSpotRepository(retrofitScenicSpotApiService)
    }
}