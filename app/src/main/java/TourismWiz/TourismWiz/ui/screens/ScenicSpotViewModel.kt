package TourismWiz.TourismWiz.ui.screens

import TourismWiz.TourismWiz.ScenicSpotApplication
import TourismWiz.TourismWiz.data.City
import TourismWiz.TourismWiz.data.ScenicSpotRepository
import TourismWiz.TourismWiz.model.ScenicSpot
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

sealed interface ScenicSpotUiState{
    data class Success(val scenicSpots: List<ScenicSpot>):ScenicSpotUiState
    object Error : ScenicSpotUiState
    object Loading : ScenicSpotUiState
}

class ScenicSpotViewModel(private val scenicSpotRepository: ScenicSpotRepository) : ViewModel(){
    var scenicSpotUiState : ScenicSpotUiState by mutableStateOf(ScenicSpotUiState.Loading)
        private set

    private var reachableScenicSpot : MutableSet<String> = mutableSetOf()

    init{
        getScenicSpots(city = City.defaultCity)
    }

    fun getScenicSpots(city: String){
        viewModelScope.launch{
            scenicSpotUiState = ScenicSpotUiState.Loading
            scenicSpotUiState = try {
                ScenicSpotUiState.Success(scenicSpotRepository.getScenicSpots(city = city))
            } catch (error: IOException){
                ScenicSpotUiState.Error
            } catch (error: HttpException){
                ScenicSpotUiState.Error
            }
        }
    }

    companion object{
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = ScenicSpotApplication()
                val scenicSpotRepository = application.container.scenicSpotRepository
                ScenicSpotViewModel(scenicSpotRepository = scenicSpotRepository)
            }
        }
    }
}