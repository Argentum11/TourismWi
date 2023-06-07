package TourismWiz.TourismWiz.ui.screens

import TourismWiz.TourismWiz.model.Restaurant
import TourismWiz.TourismWiz.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.foundation.lazy.grid.items

@Composable
fun RestaurantScreen(
    restaurantUiState: RestaurantUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    searchText:String,
    onTotalUpdated: (Int) -> Unit
) {
    when (restaurantUiState){
        is RestaurantUiState.Loading -> LoadingScreen(modifier)
        is RestaurantUiState.Error -> ErrorScreen(retryAction, modifier)
        is RestaurantUiState.Success -> RestaurantGridScreen(restaurants = restaurantUiState.restaurants, modifier, searchText, onTotalUpdated)
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
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier){
    Column (
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
            ){
        Text(stringResource(R.string.search_failed))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun RestaurantGridScreen(restaurants: List<Restaurant>, modifier: Modifier = Modifier, searchText: String, onTotalUpdated: (Int) -> Unit) {
    val filteredRestaurants = restaurants.filter { restaurant ->
        restaurant.RestaurantName.contains(searchText, ignoreCase = true)||
                restaurant.Description.contains(searchText, ignoreCase = true)
    }
    val total = filteredRestaurants.size
    if(total == 0){
        NoResult()
    }
    else{
        onTotalUpdated(total)
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(items = filteredRestaurants, key = { restaurant -> restaurant.RestaurantID }) { restaurant ->
                RestaurantCard(restaurant)
            }
        }
    }
}


@Composable
fun RestaurantCard(restaurant: Restaurant, modifier: Modifier = Modifier) {
    val LightBlue = Color(0xFFB2EBF2)
    val DarkBlue = Color(0xFF00008B)
    Card(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .aspectRatio(1f),
        elevation = 8.dp,
        backgroundColor = LightBlue,
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

                if (restaurant.Picture?.PictureUrl1 == null) {
                    Image(
                        painter = painterResource(id = R.drawable.noimage),
                        contentDescription = "No Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                } else {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(restaurant.Picture.PictureUrl1)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Restaurant Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }


                Text(
                    text = restaurant.RestaurantName,
                    style = MaterialTheme.typography.h5,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = DarkBlue
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
