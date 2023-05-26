package TourismWiz.TourismWiz.ui

import TourismWiz.TourismWiz.R
import TourismWiz.TourismWiz.data.City
import TourismWiz.TourismWiz.data.numberOfDataInOnePage
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
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
                    var selectedCity by remember {
                        mutableStateOf(City.defaultCity)
                    }
                    var searchText by remember { mutableStateOf("") }
                    var expanded by remember { mutableStateOf(false) }
                    var pageNumber by remember { mutableStateOf(1) }
                    var restaurantTotal by remember { mutableStateOf(0) }
                    val contextForToast = LocalContext.current.applicationContext
                    Column {
                        CitySelector(
                            expanded = expanded,
                            onExpandedChange = { isExpanded -> expanded = isExpanded },
                            selectedCity = selectedCity,
                            onCitySelected = { city ->
                                selectedCity = city
                                Toast.makeText(
                                    contextForToast,
                                    contextForToast.getText(City.getStringId(city)),
                                    Toast.LENGTH_SHORT
                                ).show()
                                expanded = false
                                restaurantViewModel.getRestaurants(selectedCity, pageNumber)
                            }
                        )
                        TextField(
                            value = searchText,
                            onValueChange = { newValue -> searchText = newValue },
                            label = { Text(stringResource(R.string.keyword)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                        Row {
                            Button(onClick = {
                                if (pageNumber >= 2) {
                                    pageNumber -= 1
                                    restaurantViewModel.getRestaurants(selectedCity, pageNumber)
                                } else {
                                    Toast.makeText(
                                        contextForToast,
                                        contextForToast.getText(R.string.noPreviousPage),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }) {
                                Text(text = "previous")
                            }
                            Text(text = pageNumber.toString())
                            Button(onClick = {
                                if(pageNumber * numberOfDataInOnePage < restaurantTotal){
                                    pageNumber += 1
                                    restaurantViewModel.getRestaurants(selectedCity, pageNumber)
                                } else {
                                    Toast.makeText(
                                        contextForToast,
                                        contextForToast.getText(R.string.noNextPage),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }) {
                                Text(text = "next")
                            }
                        }
                        RestaurantScreen(restaurantUiState = restaurantViewModel.restaurantUiState,
                            retryAction = {
                                restaurantViewModel.getRestaurants(
                                    selectedCity,
                                    pageNumber
                                )
                            }, searchText = searchText,
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
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    selectedCity: String,
    onCitySelected: (String) -> Unit
) {
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
            val citiesForRestaurant = City.cities.filterNot { it == City.taipei }

            citiesForRestaurant.forEach { selectedOption ->
                DropdownMenuItem(onClick = {
                    onCitySelected(selectedOption)
                }) {
                    Text(text = stringResource(id = City.getStringId(selectedOption)))
                }
            }
        }
    }
}