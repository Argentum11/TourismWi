package TourismWiz.TourismWiz.model

import TourismWiz.TourismWiz.data.FireBase
import android.util.Log
import androidx.compose.runtime.*

data class Comment(
    val name: String,
    val email: String,
    val comment: String,
    val rate: Int
)

@Composable
fun commentList(id:String): MutableList<Comment>{
    var commentList by remember { mutableStateOf(mutableListOf<Comment>()) }
    FireBase.getComment(id){
        val tempList = mutableListOf<Comment>()
        for(i in it){
            tempList.add(Comment(i.get("name").toString(),i.get("email").toString(),i.get("comment").toString(),i.get("rate").toString().toInt()))
        }
        commentList=tempList
    }
    Log.d("FireBaseRelated", "comment list -> $commentList")
    return commentList
}