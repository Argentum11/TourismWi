package TourismWiz.TourismWiz.ui

import TourismWiz.TourismWiz.R
import TourismWiz.TourismWiz.ui.screens.RestaurantScreen
import TourismWiz.TourismWiz.ui.screens.RestaurantViewModel
import TourismWiz.TourismWiz.data.City
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TourismWizApp(modifier: Modifier = Modifier) {
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it), color = MaterialTheme.colors.background
        ) {
            val restaurantViewModel: RestaurantViewModel =
                viewModel(factory = RestaurantViewModel.Factory)
            var selectedCity by remember {
                mutableStateOf(City.defaultCity)
            }
            var searchText by remember { mutableStateOf("") }
            var expanded by remember { mutableStateOf(false) }
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
                        restaurantViewModel.getRestaurants(selectedCity)
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
                RestaurantScreen(restaurantUiState = restaurantViewModel.restaurantUiState,
                    retryAction = { restaurantViewModel.getRestaurants(selectedCity) }, searchText = searchText)
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