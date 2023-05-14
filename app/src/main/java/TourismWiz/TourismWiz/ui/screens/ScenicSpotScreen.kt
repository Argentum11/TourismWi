package TourismWiz.TourismWiz.ui.screens

import TourismWiz.TourismWiz.model.ScenicSpot
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
fun ScenicSpotScreen(
    scenicSpotUiState: ScenicSpotUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (scenicSpotUiState){
        is ScenicSpotUiState.Loading -> LoadingScreen(modifier)
        is ScenicSpotUiState.Error -> ErrorScreen(retryAction, modifier)
        is ScenicSpotUiState.Success -> ScenicSpotGridScreen(scenicSpots = scenicSpotUiState.scenicSpots, modifier)
    }
}

@Composable
fun ScenicSpotGridScreen(scenicSpots: List<ScenicSpot>, modifier: Modifier  = Modifier){
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(items = scenicSpots, key = {scenicSpots -> scenicSpots.ScenicSpotID}) {restaurant ->
            ScenicSpotCard(restaurant)
        }
    }
}

@Composable
fun ScenicSpotCard(scenicSpots: ScenicSpot, modifier: Modifier = Modifier){
    Card(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .aspectRatio(1f),
        elevation = 8.dp
    ) {
        Column {
            Text(text = scenicSpots.ScenicSpotName)
            Text(text = scenicSpots.Description ?: "無介紹")//todo
        }
    }
}