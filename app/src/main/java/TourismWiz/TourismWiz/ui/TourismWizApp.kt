package TourismWiz.TourismWiz.ui

import TourismWiz.TourismWiz.R
import TourismWiz.TourismWiz.ui.screens.RestaurantScreen
import TourismWiz.TourismWiz.ui.screens.RestaurantViewModel
import TourismWiz.TourismWiz.data.City
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterialApi::class)
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
            var expanded by remember { mutableStateOf(false) }
            val contextForToast = LocalContext.current.applicationContext

            Column() {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    // text field
                    TextField(
                        stringResource(id = City.mapEnToUserLanguage!!.get(selectedCity) ?: R.string.error ), {},
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
                        onDismissRequest = { expanded = false }
                    ) {
                        // this is a column scope
                        // all the items are added vertically
                        City.cities.forEach { selectedOption ->
                            // menu item
                            DropdownMenuItem(onClick = {
                                selectedCity = selectedOption
                                Toast.makeText(contextForToast, selectedOption, Toast.LENGTH_SHORT).show()
                                expanded = false
                                restaurantViewModel.getRestaurants(selectedCity)
                            }) {
                                Text(text = stringResource(id = City.mapEnToUserLanguage?.get(selectedOption) ?: R.string.error))
                            }
                        }
                    }
                }
                RestaurantScreen(restaurantUiState = restaurantViewModel.restaurantUiState,
                    retryAction = { restaurantViewModel.getRestaurants(selectedCity) })
            }

        }
    }
}