package TourismWiz.TourismWiz.data

import TourismWiz.TourismWiz.model.Restaurant
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
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
        public fun createAccount(account: String, password: String,context: Context) {
            auth.createUserWithEmailAndPassword(account, password)
                .addOnCompleteListener(fireBaseExecutor) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Create account successful", Toast.LENGTH_SHORT).show()
                        Log.d("FireBaseRelated", "createUserWithEmail:success")
                    } else {
                        Toast.makeText(context, task.exception?.localizedMessage!!, Toast.LENGTH_SHORT)
                            .show()
                        Log.d("FireBaseRelated", task.exception?.localizedMessage!!)
                    }
                }
        }
        //        user!!.sendEmailVerification()
//                                .addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                Log.d(TAG, "Email sent.")
//                            }
//                        }
        public fun loginAccount(account: String, password: String,context:Context):String {
            auth.signInWithEmailAndPassword(account, password)
                .addOnCompleteListener(fireBaseExecutor) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                        Log.d("FireBaseRelated", "loginUserWithEmail:success")
                    } else {
//                        Toast.makeText(context, task.exception?.localizedMessage!!, Toast.LENGTH_SHORT)
//                            .show()
                        Toast.makeText(context, "User not found", Toast.LENGTH_SHORT)
                            .show()
                        Log.d("FireBaseRelated", task.exception?.localizedMessage!!)
                    }
                }
            return account
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
        public fun addComment(name:String,comment:String){
            val input = hashMapOf(
                "name" to name,
                "comment" to comment
            )
            addData("comment",input)
        }
        public fun getComment(name:String,callback: (result: List<DocumentSnapshot>) -> Unit){
            val ret = getData("comment",Pair("name",name)){result ->
                if(result!=null){
                    callback(result.documents)
                }else{
                    callback(emptyList())
                }
            }
        }
        public fun addFavorite(email:String,restaurant: Restaurant){
            val input = hashMapOf(
                "item" to gson.toJson(restaurant)
            )
            addData(email,input)
        }
        public fun getFavorite(email:String,callback: (result: List<DocumentSnapshot>) -> Unit){
            val ret = getData(email,Pair("","")){result ->
                if(result!=null){
                    callback(result.documents)
                }else{
                    callback(emptyList())
                }
            }
        }
    }
}