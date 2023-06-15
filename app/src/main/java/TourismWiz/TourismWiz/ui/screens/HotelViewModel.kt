package TourismWiz.TourismWiz.ui.screens

import TourismWiz.TourismWiz.HotelApplication
import TourismWiz.TourismWiz.data.City
import TourismWiz.TourismWiz.data.HotelRepository
import TourismWiz.TourismWiz.model.Hotel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface HotelUiState {
    data class Success(val hotels: List<Hotel>) : HotelUiState
    object Error : HotelUiState
    object Loading : HotelUiState
}

class HotelViewModel(private val hotelRepository: HotelRepository) : ViewModel() {
    var hotelUiState: HotelUiState by mutableStateOf(HotelUiState.Loading)
        private set

    private var reachableHotel: MutableSet<String> = mutableSetOf()

    init {
        getHotels(City.defaultCity)
    }

    fun getHotels(city: String) {
        viewModelScope.launch {
            hotelUiState = HotelUiState.Loading
            hotelUiState = try {
                HotelUiState.Success(hotelRepository.getHotels(city = city))
            } catch (error: IOException) {
                HotelUiState.Error
            } catch (error: HttpException) {
                HotelUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = HotelApplication()
                val hotelRepository = application.container.hotelRepository
                HotelViewModel(hotelRepository = hotelRepository)
            }
        }
    }
}