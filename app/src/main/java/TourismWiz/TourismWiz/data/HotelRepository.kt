package TourismWiz.TourismWiz.data

import TourismWiz.TourismWiz.BuildConfig
import TourismWiz.TourismWiz.model.Hotel
import TourismWiz.TourismWiz.network.HotelApiService
import TourismWiz.TourismWiz.network.TDXTokenApi
import TourismWiz.TourismWiz.network.TokenResponse
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface HotelRepository {
    suspend fun getHotels(city: String): List<Hotel>
}

class NetworkHotelRepository(private val hotelApiService: HotelApiService) : HotelRepository {
    var headers = mapOf("authorization" to "Bearer 123")
    private val clientID = BuildConfig.CLIENT_ID
    private val clientSecret = BuildConfig.CLIENT_SECRET

    /* TODO make get token somewhere for restaurant, hotel and scenic spot to access */
    private fun getToken() {
        runBlocking {
            launch {
                TDXTokenApi.retrofitService
                    .getUserLogin("client_credentials", clientID, clientSecret)
                    .enqueue(object : Callback<TokenResponse> {
                        override fun onResponse(
                            call: Call<TokenResponse>,
                            response: Response<TokenResponse>
                        ) {
                            if (response.isSuccessful) {
                                val responseData = response.body() // 获取响应数据
                                headers =
                                    mapOf("authorization" to "Bearer ${responseData!!.access_token}")
                            } else {
                                val errorBody = response.errorBody()?.string()
                                Log.e(
                                    "Error",
                                    "Failed to get access token. Error response: $errorBody"
                                )
                                //TODO Show an error message to the user
                            }
                        }

                        override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                            Log.e("Error", "Network error. Error: ${t.message}")
                            //TODO Show an error message to the user
                        }
                    })
            }
        }
    }

    override suspend fun getHotels(city: String): List<Hotel> {
        getToken()
        delay(2000)
        return hotelApiService.getHotels(city = city, headers)
    }
}