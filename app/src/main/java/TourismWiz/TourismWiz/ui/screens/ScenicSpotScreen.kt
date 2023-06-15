package TourismWiz.TourismWiz.ui.screens

import TourismWiz.TourismWiz.R
import TourismWiz.TourismWiz.data.darkBlue
import TourismWiz.TourismWiz.data.lightBlue
import TourismWiz.TourismWiz.model.ScenicSpot
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun ScenicSpotScreen(
    scenicSpotUiState: ScenicSpotUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    var selectedScenicSpotId by remember { mutableStateOf("") }

    when (scenicSpotUiState) {
        is ScenicSpotUiState.Loading -> LoadingScreen(modifier)
        is ScenicSpotUiState.Error -> ErrorScreen(retryAction, modifier)
        is ScenicSpotUiState.Success -> {
            NavHost(navController = navController, startDestination = "scenicSpotGrid") {
                composable("scenicSpotGrid") {
                    ScenicSpotGridScreen(scenicSpots = scenicSpotUiState.scenicSpots, modifier,
                        onItemClick = { scenicSpot ->
                            selectedScenicSpotId = scenicSpot.ScenicSpotID
                            navController.navigate("scenicSpotDetail")
                        })
                }
                composable("scenicSpotDetail") {
                    val scenicSpot =
                        scenicSpotUiState.scenicSpots.find { it.ScenicSpotID == selectedScenicSpotId }
                    scenicSpot?.let { ScenicSpotDetailScreen(scenicSpot = it) }
                }
            }
        }
    }
}

@Composable
fun ScenicSpotGridScreen(
    scenicSpots: List<ScenicSpot>, modifier: Modifier = Modifier,
    onItemClick: (ScenicSpot) -> Unit
) {
    val filteredScenicSpots = remember { mutableStateListOf<ScenicSpot>() }
    val focusManager = LocalFocusManager.current
    var searchQuery by remember { mutableStateOf("") }
    Column(modifier = modifier
        .fillMaxWidth()
        .clickable { focusManager.clearFocus() }) {
        SearchTextField(
            searchQuery = searchQuery,
            onSearchQueryChange = { query -> searchQuery = query },
            onClearSearchQuery = { searchQuery = "" }
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            filteredScenicSpots.clear()
            filteredScenicSpots.addAll(
                scenicSpots.filter { scenicSpot ->
                    scenicSpot.ScenicSpotName.contains(searchQuery, ignoreCase = true) ||
                            scenicSpot.Description?.contains(searchQuery, ignoreCase = true) == true
                }
            )
            items(
                items = filteredScenicSpots,
                key = { scenicSpot -> scenicSpot.ScenicSpotID }) { scenicSpot ->
                ScenicSpotCard(scenicSpot, onItemClick = onItemClick)
            }
        }
    }
}

@Composable
fun ScenicSpotCard(
    scenicSpot: ScenicSpot, modifier: Modifier = Modifier, onItemClick: (ScenicSpot) -> Unit
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onItemClick(scenicSpot) },
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
                ImageDisplay(scenicSpot.Picture.PictureUrl1)
                Text(
                    text = scenicSpot.ScenicSpotName,
                    style = MaterialTheme.typography.h5,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = darkBlue
                )

                Text(
                    text = scenicSpot.DescriptionDetail.take(80),
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
fun ScenicSpotDetailScreen(scenicSpot: ScenicSpot) {
    val context = LocalContext.current
    var phoneNumber = ""
    val phoneNumberClick: () -> Unit = {
        val phoneUri = "tel:${phoneNumber}"
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse(phoneUri))
        context.startActivity(intent)
    }
    if (scenicSpot.Phone != null) {
        phoneNumber = "0" + scenicSpot.Phone.replace("-", "").removePrefix("886")

    }
    val addressClick: () -> Unit = {
        val mapUri = Uri.parse("geo:0,0?q=${scenicSpot.Address}")
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
            ImageDisplay(scenicSpot.Picture.PictureUrl1)
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
                    if (scenicSpot.Address != null) {
                        Image(
                            painter = painterResource(R.drawable.maps),
                            contentDescription = "Location icon",
                            modifier = Modifier
                                .size(40.dp)
                                .padding(end = 8.dp)
                        )
                        val fontSize = if (scenicSpot.Address.length > 20) 16.sp else 24.sp
                        Text(
                            text = scenicSpot.Address,
                            fontSize = fontSize
                        )
                    } else if (scenicSpot.TravelInfo != null) {
                        Image(
                            painter = painterResource(R.drawable.maps),
                            contentDescription = "Location icon",
                            modifier = Modifier
                                .size(40.dp)
                                .padding(end = 8.dp)
                        )
                        val fontSize = if (scenicSpot.TravelInfo.length > 20) 16.sp else 24.sp
                        Text(
                            text = scenicSpot.TravelInfo,
                            fontSize = fontSize
                        )
                    }
                }
                if (scenicSpot.Phone != null) {
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
                }
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                ) {
                    if (scenicSpot.OpenTime != null) {
                        Image(
                            painter = painterResource(R.drawable.open),
                            contentDescription = "Open sign icon",
                            modifier = Modifier
                                .size(40.dp)
                                .padding(end = 8.dp)
                        )
                        Text(
                            text = scenicSpot.OpenTime,
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
                    text = scenicSpot.DescriptionDetail,
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
                text = stringResource(R.string.data_update_date) + " : " + scenicSpot.UpdateTime,
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