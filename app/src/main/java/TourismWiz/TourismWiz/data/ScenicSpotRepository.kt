package TourismWiz.TourismWiz.data

import TourismWiz.TourismWiz.BuildConfig
import TourismWiz.TourismWiz.model.ScenicSpot
import TourismWiz.TourismWiz.network.ScenicSpotApiService
import TourismWiz.TourismWiz.network.TDXTokenApi
import TourismWiz.TourismWiz.network.TokenResponse
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface ScenicSpotRepository {
    suspend fun getScenicSpots(city: String): List<ScenicSpot>
}

class NetworkScenicSpotRepository(private val scenicSpotApiService: ScenicSpotApiService) :
    ScenicSpotRepository {
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

    override suspend fun getScenicSpots(city: String): List<ScenicSpot> {
        getToken()
        delay(2000)
        return scenicSpotApiService.getScenicSpots(city = city, headers)
    }
}