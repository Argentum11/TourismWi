package TourismWiz.TourismWiz.ui.screens

import TourismWiz.TourismWiz.RestaurantApplication
import TourismWiz.TourismWiz.model.Restaurant
import TourismWiz.TourismWiz.data.RestaurantRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

//getToken():String{
/*
runBlocking {
        launch {
            //val restaurantList = TDXApi.retrofitService.getRestaurants(City.keelung)

            /*
            TDXTokenApi.retrofitService
                .getUserLogin("client_credentials", "","")
                .enqueue(object : Callback<TokenResponse> {
                    override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                        if (response.isSuccessful) {
                            val responseData = response.body() // 获取响应数据
                            Log.w("token",responseData!!.access_token)
                        } else {
                            // 处理非成功响应
                        }
                    }

                    override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                        //TODO: alert user that there is error getting data
                    }
                })
             */
        }
    }*/
sealed interface RestaurantUiState{
    data class Success(val restaurants: List<Restaurant>): RestaurantUiState
    object Error: RestaurantUiState
    object Loading: RestaurantUiState
}


class RestaurantViewModel(private val restaurantRepository:RestaurantRepository) : ViewModel() {
    var restaurantUiState : RestaurantUiState by mutableStateOf(RestaurantUiState.Loading)
        private set

    private var reachableRestaurant: MutableSet<String> = mutableSetOf()

    init{
        getRestaurants()
    }

    fun getRestaurants(){
        viewModelScope.launch {
            restaurantUiState = RestaurantUiState.Loading
            restaurantUiState = try {
                RestaurantUiState.Success(restaurantRepository.getRestaurants())
            } catch (error: IOException){
                RestaurantUiState.Error
            } catch (error: HttpException){
                RestaurantUiState.Error
            }
        }
    }

    companion object{
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                //val application = (this[APPLICATION_KEY] as RestaurantApplication)
                val application = RestaurantApplication()
                val restaurantRepository = application.container.restaurantRepository
                RestaurantViewModel(restaurantRepository = restaurantRepository)
            }
        }
    }
}