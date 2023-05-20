package TourismWiz.TourismWiz.ui

import TourismWiz.TourismWiz.R
import TourismWiz.TourismWiz.ui.screens.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun TourismWizApp(modifier: Modifier = Modifier) {
    val selectedScreenIndex = remember { mutableStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) },
        bottomBar = {
            BottomAppBar(backgroundColor = Color.White) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { selectedScreenIndex.value = 0 },
                        modifier = Modifier.size(24.dp) // 設定IconButton的大小為24dp
                    ) {
                        Image(
                            painter = painterResource(R.drawable.hotel),
                            contentDescription = "按鈕3"
                        )
                    }

                    IconButton(
                        onClick = { selectedScreenIndex.value = 1 },
                        modifier = Modifier.size(24.dp) // 設定IconButton的大小為24dp
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ferriswheel),
                            contentDescription = "按鈕3"
                        )
                    }

                    IconButton(
                        onClick = { selectedScreenIndex.value = 2 },
                        modifier = Modifier.size(24.dp) // 設定IconButton的大小為24dp
                    ) {
                        Image(
                            painter = painterResource(R.drawable.restaurant),
                            contentDescription = "按鈕3"
                        )
                    }

                    IconButton(
                        onClick = { selectedScreenIndex.value = 3 },
                        modifier = Modifier.size(24.dp) // 設定IconButton的大小為24dp
                    ) {
                        Image(
                            painter = painterResource(R.drawable.user),
                            contentDescription = "按鈕3"
                        )
                    }
                }
            }
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = MaterialTheme.colors.background
        ) {
            when (selectedScreenIndex.value) {
                0 -> {
                    val hotelViewModel: HotelViewModel = viewModel(factory = HotelViewModel.Factory)
                    HotelScreen(
                        hotelUiState = hotelViewModel.hotelUiState,
                        retryAction = hotelViewModel::getHotels
                    )
                }
                1 -> {
                    val scenicSpotViewModel: ScenicSpotViewModel =
                        viewModel(factory = ScenicSpotViewModel.Factory)
                    ScenicSpotScreen(
                        scenicSpotUiState = scenicSpotViewModel.scenicSpotUiState,
                        retryAction = scenicSpotViewModel::getScenicSpots
                    )
                }
                2 -> {
                    val restaurantViewModel: RestaurantViewModel =
                        viewModel(factory = RestaurantViewModel.Factory)
                    RestaurantScreen(
                        restaurantUiState = restaurantViewModel.restaurantUiState,
                        retryAction = restaurantViewModel::getRestaurants
                    )
                }
                3 -> {
                    LoginScreen()
                }
            }
        }
    }
}
