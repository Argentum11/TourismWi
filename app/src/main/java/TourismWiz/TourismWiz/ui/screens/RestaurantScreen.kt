package TourismWiz.TourismWiz.ui.screens

import TourismWiz.TourismWiz.model.Restaurant
import TourismWiz.TourismWiz.R
import android.content.Intent
import android.net.Uri
import TourismWiz.TourismWiz.data.darkBlue
import TourismWiz.TourismWiz.data.lightBlue
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

@Composable
fun RestaurantScreen(
    restaurantUiState: RestaurantUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onTotalUpdated: (Int) -> Unit
) {
    val navController = rememberNavController()
    var selectedRestaurantId by remember { mutableStateOf("") }
    when (restaurantUiState) {
        is RestaurantUiState.Loading -> LoadingScreen(modifier)
        is RestaurantUiState.Error -> ErrorScreen(retryAction, modifier)
        is RestaurantUiState.Success -> {
            NavHost(navController = navController, startDestination = "restaurantGrid") {
                composable("restaurantGrid") {
                    RestaurantGridScreen(
                        restaurants = restaurantUiState.restaurants,
                        onTotalUpdated = onTotalUpdated,
                        onItemClick = { restaurant ->
                            selectedRestaurantId = restaurant.RestaurantID
                            navController.navigate("restaurantDetail")
                        }
                    )
                }
                composable("restaurantDetail") {
                    val restaurant =
                        restaurantUiState.restaurants.find { it.RestaurantID == selectedRestaurantId }
                    restaurant?.let { RestaurantDetailScreen(restaurant = it) }
                }
            }
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressIndicator() // 進度指示器，表示正在加載
    }
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.search_failed))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun RestaurantGridScreen(
    restaurants: List<Restaurant>,
    modifier: Modifier = Modifier,
    onTotalUpdated: (Int) -> Unit,
    onItemClick: (Restaurant) -> Unit
) {
    var filteredRestaurants = remember { mutableStateListOf<Restaurant>() }
    var searchQuery by remember { mutableStateOf("") }
    var total by remember { mutableStateOf(restaurants.size) }

    Column(modifier = modifier.fillMaxWidth()) {
        SearchTextField(
            searchQuery = searchQuery,
            onSearchQueryChange = { query -> searchQuery = query },
            onClearSearchQuery = {
                searchQuery = ""
                total = restaurants.size
            }
        )
        when (total) {
            0 -> NoResult()
            else -> {
                onTotalUpdated(total)

                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    filteredRestaurants.clear()
                    filteredRestaurants.addAll(
                        restaurants.filter { restaurant ->
                            restaurant.RestaurantName.contains(searchQuery, ignoreCase = true) ||
                                    restaurant.Description.contains(searchQuery, ignoreCase = true)
                        }
                    )
                    total = filteredRestaurants.size
                    items(
                        items = filteredRestaurants,
                        key = { restaurant -> restaurant.RestaurantID }) { restaurant ->
                        RestaurantCard(restaurant, onItemClick = onItemClick)
                    }
                }

            }
        }
    }
}

@Composable
fun RestaurantCard(
    restaurant: Restaurant,
    modifier: Modifier = Modifier,
    onItemClick: (Restaurant) -> Unit
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onItemClick(restaurant) },
        elevation = 8.dp,
        backgroundColor = lightBlue,
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.White)
            ) {
                ImageDisplay(restaurant.Picture?.PictureUrl1)
                Text(
                    text = restaurant.RestaurantName,
                    style = MaterialTheme.typography.h5,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = darkBlue
                )

                Text(
                    text = restaurant.Description.take(80),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colors.secondary
                )
            }
        }
    }
}

@Composable
fun RestaurantDetailScreen(restaurant: Restaurant) {
    val context = LocalContext.current
    val phoneNumber = "0" + restaurant.Phone.replace("-", "").removePrefix("886")
    val phoneNumberClick: () -> Unit = {
        val phoneUri = "tel:${phoneNumber}"
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse(phoneUri))
        context.startActivity(intent)
    }
    val addressClick: () -> Unit = {
        val mapUri = Uri.parse("geo:0,0?q=${restaurant.Address}")
        val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
        mapIntent.setPackage("com.google.android.apps.maps") // 指定使用 Google 地图应用
        context.startActivity(mapIntent)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            ImageDisplay(restaurant.Picture?.PictureUrl1)
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 16.dp)
                    .background(Color(0xFFE0E0E0))
                    .padding(16.dp)
            ) {

                Text(
                    text = stringResource(R.string.related_info) + " : ",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .clickable(onClick = addressClick)
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.maps),
                        contentDescription = "Location icon",
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 8.dp)
                    )
                    val fontSize = if (restaurant.Address.length > 20) 16.sp else 24.sp
                    Text(
                        text = restaurant.Address,
                        fontSize = fontSize
                    )
                }

                Row(
                    modifier = Modifier
                        .clickable(onClick = phoneNumberClick)
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                ) {
                    Image(
                        painter = painterResource(R.drawable.call),
                        contentDescription = "Phone icon",
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = phoneNumber,
                        fontSize = 24.sp
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                ) {
                    if (restaurant.OpenTime != null) {
                        Image(
                            painter = painterResource(R.drawable.open),
                            contentDescription = "Open sign icon",
                            modifier = Modifier
                                .size(40.dp)
                                .padding(end = 8.dp)
                        )
                        Text(
                            text = restaurant.OpenTime,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 16.dp)
                    .background(Color(0xFFE0E0E0))
                    .padding(16.dp)
            ) {

                Text(
                    text = stringResource(R.string.detailed_description) + " : ",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = restaurant.Description,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .background(Color(0xFFE0E0E0))
                        .padding(16.dp)
                )
            }
        }

        item {
            Text(
                text = stringResource(R.string.data_update_date) + " : " + restaurant.UpdateTime,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .background(Color(0xFFE0E0E0))
                    .padding(16.dp)
                    .clip(shape = RoundedCornerShape(8.dp)),
                color = Color.Black
            )
        }
    }
}
