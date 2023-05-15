package TourismWiz.TourismWiz.ui

import TourismWiz.TourismWiz.R
import TourismWiz.TourismWiz.ui.screens.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TourismWizApp(modifier: Modifier = Modifier) {
    Scaffold (
        modifier = Modifier
            .fillMaxSize(),
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name))}) }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colors.background
        ) {
            val restaurantViewModel : RestaurantViewModel = viewModel(factory = RestaurantViewModel.Factory)
            RestaurantScreen(
                restaurantUiState = restaurantViewModel.restaurantUiState,
                retryAction = restaurantViewModel::getRestaurants
            )
//            val hotelViewModel : HotelViewModel = viewModel(factory = HotelViewModel.Factory)
//            HotelScreen(
//                hotelUiState = hotelViewModel.hotelUiState,
//                retryAction = hotelViewModel::getHotels
//            )
//            val scenicSpotViewModel : ScenicSpotViewModel = viewModel(factory = ScenicSpotViewModel.Factory)
//            ScenicSpotScreen(
//                scenicSpotUiState = scenicSpotViewModel.scenicSpotUiState,
//                retryAction = scenicSpotViewModel::getScenicSpots
//            )
        }
    }
}