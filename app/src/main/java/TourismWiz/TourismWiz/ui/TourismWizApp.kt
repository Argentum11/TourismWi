package TourismWiz.TourismWiz.ui

import TourismWiz.TourismWiz.R
import TourismWiz.TourismWiz.data.City
import TourismWiz.TourismWiz.ui.screens.*
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun TourismWizApp() {
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
                        onClick = { selectedScreenIndex.value = 2 },
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
            val contextForToast = LocalContext.current.applicationContext

            when (selectedScreenIndex.value) {
                0 -> {
                    var selectedCityForHotel by remember {
                        mutableStateOf(City.defaultCity)
                    }
                    val hotelViewModel: HotelViewModel = viewModel(factory = HotelViewModel.Factory)

                    Column {
                        CitySelector(
                            selectedCity = selectedCityForHotel,
                            onCitySelected = { city ->
                                selectedCityForHotel = city
                                Toast.makeText(
                                    contextForToast,
                                    contextForToast.getText(City.getStringId(city)),
                                    Toast.LENGTH_SHORT
                                ).show()
                                hotelViewModel.getHotels(selectedCityForHotel)
                            }
                        )
                        HotelScreen(
                            hotelUiState = hotelViewModel.hotelUiState,
                            retryAction = { hotelViewModel.getHotels(selectedCityForHotel) }
                        )
                    }
                }
                1 -> {
                    var selectedCityForScenicSpot by remember {
                        mutableStateOf(City.defaultCity)
                    }
                    val scenicSpotViewModel: ScenicSpotViewModel =
                        viewModel(factory = ScenicSpotViewModel.Factory)

                    Column{
                        CitySelector(
                            selectedCity = selectedCityForScenicSpot,
                            onCitySelected = { city ->
                                selectedCityForScenicSpot = city
                                Toast.makeText(
                                    contextForToast,
                                    contextForToast.getText(City.getStringId(city)),
                                    Toast.LENGTH_SHORT
                                ).show()
                                scenicSpotViewModel.getScenicSpots(selectedCityForScenicSpot)
                            }
                        )
                        ScenicSpotScreen(
                            scenicSpotUiState = scenicSpotViewModel.scenicSpotUiState,
                            retryAction = {scenicSpotViewModel.getScenicSpots(selectedCityForScenicSpot)}
                        )
                    }
                }
                2 -> {
                    val restaurantViewModel: RestaurantViewModel =
                        viewModel(factory = RestaurantViewModel.Factory)
                    var selectedCity by remember {
                        mutableStateOf(City.defaultCity)
                    }

                    var pageNumber by remember { mutableStateOf(1) }
                    var restaurantTotal by remember { mutableStateOf(0) }
                    Column {
                        CitySelector(
                            selectedCity = selectedCity,
                            onCitySelected = { city ->
                                selectedCity = city
                                Toast.makeText(
                                    contextForToast,
                                    contextForToast.getText(City.getStringId(city)),
                                    Toast.LENGTH_SHORT
                                ).show()
                                restaurantViewModel.getRestaurants(selectedCity, pageNumber)
                            }
                        )
                        RestaurantScreen(restaurantUiState = restaurantViewModel.restaurantUiState,
                            retryAction = {
                                restaurantViewModel.getRestaurants(
                                    selectedCity,
                                    pageNumber
                                )
                            },
                            onTotalUpdated = { total ->
                                restaurantTotal = total
                            }
                        )
                    }
                }
                3 -> {
                    // TODO user page
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CitySelector(
    selectedCity: String,
    onCitySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val onExpandedChange = { isExpanded: Boolean -> expanded = isExpanded }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = onExpandedChange) {
        TextField(
            stringResource(id = City.getStringId(selectedCity)),
            {},
            readOnly = true,
            label = { Text(text = stringResource(id = R.string.city)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
            val citiesForRestaurant = City.cities

            citiesForRestaurant.forEach { selectedOption ->
                DropdownMenuItem(onClick = {
                    onCitySelected(selectedOption)
                    expanded = false
                }) {
                    Text(text = stringResource(id = City.getStringId(selectedOption)))
                }
            }
        }
    }
}
