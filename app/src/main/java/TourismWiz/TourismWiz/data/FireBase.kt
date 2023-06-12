package TourismWiz.TourismWiz.data

import TourismWiz.TourismWiz.model.Restaurant
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
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
                    Toast.makeText(context, "Create account successful", Toast.LENGTH_SHORT).show()
                    Log.d("FireBaseRelated", "createUserWithEmail:success")
                    // 登入操作
                    return@continueWithTask loginAccount(account, password, context)
                } else {
                    Toast.makeText(context, createTask.exception?.localizedMessage!!, Toast.LENGTH_SHORT)
                        .show()
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
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                    Log.d("FireBaseRelated", "loginUserWithEmail:success")
                    MyUser.password=password
                } else {
                    Toast.makeText(context, "User not found or password mismatch", Toast.LENGTH_SHORT).show()
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
        public fun <T> addFavorite(email: String, field: String, item: T) {
            val input = hashMapOf(
                "field" to field,
                "item" to gson.toJson(item)
            )
            MyUser.user?.email?.let {
                getFavorite(it, field) { result ->
                    var flag=true
                    var id:String?=null
                    for (i in result) {
                        val r: Restaurant = gson.fromJson(
                            i.get("item").toString(),
                            Restaurant::class.java
                        )
                        if (r == item) {
                            flag = false
                            id=i.id
                            break
                        }
                    }
                    if (flag)
                        addData(email, input)
                    else
                        deleteData(email,id!!)

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
                        Toast.makeText(context, "change name successful!", Toast.LENGTH_SHORT).show()
                        MyUser.user!!.email?.let { MyUser.password?.let { it1 ->
                            loginAccount(it,it1,context)?.addOnCompleteListener{ task ->
                                if (task.isSuccessful) {
                                    MyUser.user = task.result
                                } else {
                                    Toast.makeText(context, "something wrong, try again later!", Toast.LENGTH_SHORT).show()
                                    MyUser.user = null
                                }
                            }
                        } }
                    } else {
                        Toast.makeText(context, "change name fail! please try again later", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
}