package TourismWiz.TourismWiz.ui.screens

import TourismWiz.TourismWiz.model.Hotel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HotelScreen(
    hotelUiState: HotelUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (hotelUiState){
        is HotelUiState.Loading -> LoadingScreen(modifier)
        is HotelUiState.Error -> ErrorScreen(retryAction, modifier)
        is HotelUiState.Success -> HotelGridScreen(hotels = hotelUiState.hotels, modifier)
    }
}

@Composable
fun HotelGridScreen(hotels: List<Hotel>, modifier: Modifier  = Modifier){
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(items = hotels, key = {hotel -> hotel.HotelID}) {restaurant ->
            HotelCard(restaurant)
        }
    }
}

@Composable
fun HotelCard(hotel: Hotel, modifier: Modifier = Modifier){
    Card(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .aspectRatio(1f),
        elevation = 8.dp
    ) {
        Column {
            Text(text = hotel.HotelName)
            Text(text = hotel.Description ?: "沒")
        }
    }
}