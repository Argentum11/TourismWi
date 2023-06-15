package TourismWiz.TourismWiz.ui.screens

import TourismWiz.TourismWiz.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ImageDisplay(imageUrl: String?) {
    if (imageUrl == null) {
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
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Image for Restaurants, hotels and scenicspots",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}