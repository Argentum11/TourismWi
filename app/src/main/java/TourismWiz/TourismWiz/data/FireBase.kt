package TourismWiz.TourismWiz.data

import TourismWiz.TourismWiz.R
import TourismWiz.TourismWiz.model.Hotel
import TourismWiz.TourismWiz.model.Restaurant
import TourismWiz.TourismWiz.model.ScenicSpot
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.res.stringResource
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import java.util.concurrent.Executor
class FireBase {
    companion object {
        val gson = Gson()
        private val fireBaseExecutor = object : Executor {
            private val handler = Handler(Looper.getMainLooper())
            override fun execute(r: Runnable) {
                handler.post(r)
            }
        }
        private lateinit var auth: FirebaseAuth
        private lateinit var db: FirebaseFirestore
        init {
            // Initialize Firebase Auth
            auth = Firebase.auth
            db = Firebase.firestore
        }
        public fun createAccount(account: String, password: String, context: Context): Task<FirebaseUser> {
            val createInTask = auth.createUserWithEmailAndPassword(account, password)
            return createInTask.continueWithTask { createTask ->
                if (createTask.isSuccessful) {
                    Toast.makeText(context, context.getString(R.string.create_s), Toast.LENGTH_SHORT)
                        .show()
                    Log.d("FireBaseRelated", "createUserWithEmail:success")
                    // 登入操作
                    return@continueWithTask loginAccount(account, password, context)
                } else {
                    if (password.length < 6) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.create_too_short),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else {
                        Toast.makeText(
                            context,
                            createTask.exception?.localizedMessage!!,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    Log.d("FireBaseRelated", createTask.exception?.localizedMessage!!)
                    // 返回 null 表示註冊失敗
                    return@continueWithTask Tasks.forResult(null)
                }
            }
        }

        public fun loginAccount(account: String, password: String, context: Context): Task<FirebaseUser> {
            val signInTask = auth.signInWithEmailAndPassword(account, password)
            signInTask.addOnCompleteListener(fireBaseExecutor) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, context.getString(R.string.login_s), Toast.LENGTH_SHORT).show()
                    Log.d("FireBaseRelated", "loginUserWithEmail:success")
                    MyUser.password=password
                    getFavorite(account,"Restaurant"){
                        for (i in it) {
                            val r: Restaurant = gson.fromJson(
                                i.get("item").toString(),
                                Restaurant::class.java
                            )
                            MyUser.restaurantList.add(r)
                        }
                    }
                    getFavorite(account,"Hotel"){
                        for (i in it) {
                            val r: Hotel = gson.fromJson(
                                i.get("item").toString(),
                                Hotel::class.java
                            )
                            MyUser.hotelList.add(r)
                        }
                    }
                    getFavorite(account,"ScenicSpot"){
                        for (i in it) {
                            val r: ScenicSpot = gson.fromJson(
                                i.get("item").toString(),
                                ScenicSpot::class.java
                            )
                            MyUser.scenicspotList.add(r)
                        }
                    }
                } else {
                    Toast.makeText(context, context.getString(R.string.login_f), Toast.LENGTH_SHORT).show()
                    Log.d("FireBaseRelated", task.exception?.localizedMessage!!)
                }
            }
            return signInTask.continueWith { task ->
                if (task.isSuccessful) {
                    task.result?.user
                } else {
                    null
                }
            }
        }
        private fun addData(field :String,data:Map<String,Any>){
            db.collection(field)
                .add(data)
                .addOnSuccessListener { documentReference ->
                    Log.d("FireBaseRelated", "addDataWithField:${field}:success")
                }
                .addOnFailureListener { exception ->
                    Log.d("FireBaseRelated", "addDataWithField:${field}:fail",exception)
                }
        }
        private fun getData(field: String,constrain:Pair<String,String>, callback: (result: QuerySnapshot?) -> Unit) {
            if(constrain.first==""){
                db.collection(field)
                    .get()
                    .addOnSuccessListener { result ->
                        Log.d("FireBaseRelated", "getDataWithField:$field:success")
                        callback(result)
                    }
                    .addOnFailureListener { exception ->
                        Log.d("FireBaseRelated", "getDataWithField:$field:fail", exception)
                        callback(null)
                    }
            }else {
                db.collection(field)
                    .whereEqualTo(constrain.first, constrain.second)
                    .get()
                    .addOnSuccessListener { result ->
                        Log.d("FireBaseRelated", "getDataWithField:$field:success")
                        callback(result)
                    }
                    .addOnFailureListener { exception ->
                        Log.d("FireBaseRelated", "getDataWithField:$field:fail", exception)
                        callback(null)
                    }
            }
        }
        //,constrain:Pair<String,String>
        private fun deleteData(field: String,id:String) {
            db.collection(field).document(id).delete()
                .addOnSuccessListener {
                    Log.d("FireBaseRelated", "deleteDataWithField:$field,id:$id:success")
                }
                .addOnFailureListener { e ->
                    Log.d("FireBaseRelated", "deleteDataWithField:$field,id:$id:fail")
                }
        }
        public fun addComment(id:String,rate:String,email:String,name:String,comment:String){
            val input = hashMapOf(
                "id" to id,
                "rate" to rate,
                "email" to email,
                "name" to name,
                "comment" to comment
            )
            addData("comment",input)
        }
        public fun getComment(id:String,callback: (result: List<DocumentSnapshot>) -> Unit){
            val ret = getData("comment",Pair("id",id)){result ->
                if(result!=null){
                    callback(result.documents)
                }else{
                    callback(emptyList())
                }
            }
        }
        private fun updateComment(email: String,name: String){
            val db = FirebaseFirestore.getInstance()
            val collectionRef = db.collection("comment")
            val query = collectionRef.whereEqualTo("email", email)
            query.get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val documentRef = collectionRef.document(document.id)
                        val updates = hashMapOf<String, Any>(
                            "id" to document.get("id").toString(),
                            "email" to document.get("email").toString(),
                            "name" to name,
                            "comment" to document.get("comment").toString()
                        )
                        documentRef.update(updates)
                            .addOnSuccessListener {
                                Log.d("FireBaseRelated", "update_in:success")
                            }
                            .addOnFailureListener { e ->
                                Log.d("FireBaseRelated", "update_in:fail")
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("FireBaseRelated", "update:fail")
                }
            db.collection("comment").document()
        }
        public fun addFavorite(email: String, field: String, item: Any,context: Context) {
            val input = hashMapOf(
                "field" to field,
                "item" to gson.toJson(item)
            )
            MyUser.user?.email?.let {
                getFavorite(it, field) { result ->
                    var flag=true
                    var id:String?=null
                    var item_id:String?=null
                    Log.d("FireBaseRelated", item.toString())
                    if(field=="Restaurant") {
                        for (i in result) {
                            val r: Restaurant = gson.fromJson(
                                i.get("item").toString(),
                                Restaurant::class.java
                            )
                            Log.d("FireBaseRelated", r.toString())
                            if (r.RestaurantID == (item as Restaurant).RestaurantID) {
                                flag = false
                                id = i.id
                                item_id=r.RestaurantID
                                break
                            }
                        }
                    }else if(field=="Hotel"){
                        for (i in result) {
                            val r: Hotel = gson.fromJson(
                                i.get("item").toString(),
                                Hotel::class.java
                            )
                            Log.d("FireBaseRelated", r.toString())
                            if (r.HotelID == (item as Hotel).HotelID) {
                                flag = false
                                id = i.id
                                item_id=r.HotelID
                                break
                            }
                        }
                    }else{
                        for (i in result) {
                            val r: ScenicSpot = gson.fromJson(
                                i.get("item").toString(),
                                ScenicSpot::class.java
                            )
                            Log.d("FireBaseRelated", r.toString())
                            if (r.ScenicSpotID == (item as ScenicSpot).ScenicSpotID) {
                                flag = false
                                id = i.id
                                item_id=r.ScenicSpotID
                                break
                            }
                        }
                    }
                    if (flag) {
                        addData(email, input)
                        if(field=="Restaurant") {
                            MyUser.restaurantList.add(item as Restaurant)
                        }else if(field=="Hotel"){
                            MyUser.hotelList.add(item as Hotel)
                        }else{
                            MyUser.scenicspotList.add(item as ScenicSpot)
                        }
                        Toast.makeText(context, context.getString(R.string.add_favorite), Toast.LENGTH_SHORT).show()
                    }
                    else {
                        deleteData(email, id!!)
                        if(field=="Restaurant") {
                            MyUser.restaurantList.removeAll{it.RestaurantID==item_id!!}
                        }else if(field=="Hotel"){
                            MyUser.hotelList.removeAll{it.HotelID==item_id!!}
                        }else{
                            MyUser.scenicspotList.removeAll{it.ScenicSpotID==item_id!!}
                        }
                        Toast.makeText(context, context.getString(R.string.remove_favorite), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        public fun getFavorite(email:String,field:String,callback: (result: List<DocumentSnapshot>) -> Unit){
            val ret = getData(email,Pair("field",field)){result ->
                if(result!=null){
                    callback(result.documents)
                }else{
                    callback(emptyList())
                }
            }
        }
        public fun changeName(name:String,context:Context){
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            MyUser.user?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, context.getString(R.string.change_name_s), Toast.LENGTH_SHORT).show()
                        MyUser.user!!.email?.let { updateComment(it,name) }
                        MyUser.user!!.email?.let { MyUser.password?.let { it1 ->
                            loginAccount(it,it1,context)?.addOnCompleteListener{ task ->
                                if (task.isSuccessful) {
                                    MyUser.user = task.result
                                } else {
                                    MyUser.user = null
                                }
                            }
                        } }
                    } else {
                        Toast.makeText(context, context.getString(R.string.change_name_f), Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
}