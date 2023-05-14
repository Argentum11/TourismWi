package TourismWiz.TourismWiz.ui

import TourismWiz.TourismWiz.ui.screens.RestaurantViewModel
import TourismWiz.TourismWiz.R
import TourismWiz.TourismWiz.network.City
import TourismWiz.TourismWiz.ui.screens.RestaurantScreen
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
            var city = remember { mutableStateOf("") }
            Column() {
                Button(onClick = { Log.e("waxa294", city.value)
                    city.value = City.kaohsiung
                    Log.e("waxa294", city.value)
                    restaurantViewModel.getRestaurants(city.value)
                }
                ) {
                    Text("change city")
                }
                RestaurantScreen(
                    restaurantUiState = restaurantViewModel.restaurantUiState,
                    retryAction = { restaurantViewModel.getRestaurants(city.value) }
                )
            }

        }
    }
}