package TourismWiz.TourismWiz.ui.screens

import TourismWiz.TourismWiz.model.Comment
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import TourismWiz.TourismWiz.R
import androidx.compose.material.Divider

@Composable
fun CommentDisplay(comment: Comment) {
    Column {
        Row(modifier = Modifier.padding(vertical = 10.dp)){
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Black, CircleShape)
            ) {
                Image(
                    painter = painterResource(R.drawable.usericon),
                    contentDescription = "Image",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column(modifier = Modifier.padding(horizontal = 5.dp)) {
                Text(comment.name, fontSize = 20.sp)
                Text(" ${comment.email}", color = Color.Gray, fontSize = 10.sp)
            }
        }
        Row(modifier = Modifier.padding(bottom = 3.dp)) {
            repeat(comment.rate) {
                Image(
                    painter = painterResource(R.drawable.star),
                    contentDescription = "Image",
                    modifier = Modifier.size(15.dp)
                )
            }
        }
        Text(comment.comment, modifier = Modifier.padding(bottom = 5.dp))
        Divider()
    }
}