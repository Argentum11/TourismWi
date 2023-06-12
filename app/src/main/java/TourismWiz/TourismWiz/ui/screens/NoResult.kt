package TourismWiz.TourismWiz.ui.screens

import TourismWiz.TourismWiz.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoResult(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GifImage() // 在此处放置您的 GifImage 组件

            Text(
                text = stringResource(R.string.no_result),
                style = TextStyle(fontSize = 20.sp, color = Color.Black),
                modifier = Modifier.padding(top = 8.dp), // 根据需要调整文本与图像之间的间距
            color = MaterialTheme.colors.secondary
            )
        }

    }
}