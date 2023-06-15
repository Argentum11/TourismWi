package TourismWiz.TourismWiz.data

import TourismWiz.TourismWiz.network.RestaurantApiService
import TourismWiz.TourismWiz.BuildConfig
import TourismWiz.TourismWiz.model.Restaurant
import TourismWiz.TourismWiz.network.TDXTokenApi
import TourismWiz.TourismWiz.network.TokenResponse
import android.util.Log
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface RestaurantRepository {
    suspend fun getRestaurants(city: String, pageNumber: Int): List<Restaurant>
}
class NetworkRestaurantRepository(private val restaurantApiService: RestaurantApiService): RestaurantRepository {

    var headers=mapOf("authorization" to "Bearer 123")
    private val clientID = BuildConfig.CLIENT_ID
    private val clientSecret = BuildConfig.CLIENT_SECRET


    private fun getToken(){
        runBlocking {
            launch{
                TDXTokenApi.retrofitService
                    .getUserLogin("client_credentials", clientID, clientSecret)
                    .enqueue(object : Callback<TokenResponse> {
                        override fun onResponse(
                            call: Call<TokenResponse>,
                            response: Response<TokenResponse>
                        ) {
                            if (response.isSuccessful) {
                                val responseData = response.body() // 获取响应数据
                                headers = mapOf("authorization" to "Bearer ${responseData!!.access_token}")
                            } else {
                                val errorBody = response.errorBody()?.string()
                                Log.e("Error", "Failed to get access token. Error response: $errorBody")
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
    override suspend fun getRestaurants(city:String, pageNumber:Int): List<Restaurant>{
        getToken()
        delay(2000)
        val skippedData = (pageNumber-1) * numberOfDataInOnePage
        return restaurantApiService.getRestaurants(city, dataInPage = 100, skippedData = 0, headers)
    }
}

