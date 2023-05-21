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
            
            val contextForToast = LocalContext.current.applicationContext

            Column {

                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
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
fun CitySelector(){
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {
        expanded = !expanded
    }) {
        // text field
        TextField(stringResource(
            id = City.getStringId(selectedCity)
        ),
            {},
            readOnly = true,
            label = { Text(text = stringResource(id = R.string.city)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        // menu
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            // this is a column scope
            // all the items are added vertically
            City.cities.forEach { selectedOption ->
                // menu item
                DropdownMenuItem(onClick = {
                    selectedCity = selectedOption
                    Toast.makeText(
                        contextForToast, contextForToast.getText(
                            City.getStringId(
                                selectedOption
                            )
                        ), Toast.LENGTH_SHORT
                    ).show()
                    expanded = false
                    restaurantViewModel.getRestaurants(selectedCity)
                }) {
                    Text(
                        text = stringResource(
                            id = City.getStringId(
                                selectedOption
                            )
                        )
                    )
                }
            }
        }
    }
}