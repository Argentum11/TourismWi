package TourismWiz.TourismWiz.data

import RestaurantApiService
import TourismWiz.TourismWiz.model.Restaurant
import TourismWiz.TourismWiz.network.TDXTokenApi
import TourismWiz.TourismWiz.network.TokenResponse
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface RestaurantRepository {
    suspend fun getRestaurants(): List<Restaurant>
}
class NetworkRestaurantRepository(private val restaurantApiService: RestaurantApiService): RestaurantRepository {
    var headers=mapOf("authorization" to "Bearer 123")
    val clientID = ""
    val clientSecret = ""
    fun getToken(){
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
                                    // 处理非成功响应
                                }
                            }

                            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                                //TODO: alert user that there is error getting data
                            }
                        })
            }
        }
    }
    override suspend fun getRestaurants(): List<Restaurant>{
        getToken()
        delay(2000)
        return restaurantApiService.getRestaurants("Taoyuan", headers)
    }
}

