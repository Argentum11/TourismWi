package TourismWiz.TourismWiz.ui.screens

import TourismWiz.TourismWiz.data.FireBase
import TourismWiz.TourismWiz.model.Restaurant
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*

data class Comment(
    val name: String,
    val email: String,
    val comment: String,
    val rate: Int
)

@Composable
fun CommentList(id:String): MutableList<Comment>{
    var commentList by remember { mutableStateOf(mutableListOf<Comment>()) }
    FireBase.getComment(id){
        var tempList = mutableListOf<Comment>()
        for(i in it){
            tempList.add(Comment(i.get("name").toString(),i.get("email").toString(),i.get("comment").toString(),i.get("rate").toString().toInt()))
        }
        commentList=tempList
    }
    Log.d("FireBaseRelated","comment list -> "+commentList.toString())
    return commentList
}