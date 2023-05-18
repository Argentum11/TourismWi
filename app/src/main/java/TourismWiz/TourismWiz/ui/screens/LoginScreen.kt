package TourismWiz.TourismWiz.ui.screens

import TourismWiz.TourismWiz.data.FireBase
import TourismWiz.TourismWiz.data.FireBase.Companion.gson
import TourismWiz.TourismWiz.model.Restaurant
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
@Composable
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var user by remember { mutableStateOf("") }


    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.padding(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.padding(16.dp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Button(
            onClick = {
                user = FireBase.loginAccount(email,password,context)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Login")
        }
        Button(
            onClick = {
                FireBase.createAccount(email,password,context)
                //跳轉
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Sign up")
        }
        Button(
            onClick = {
                FireBase.getComment(email){result ->
                    for(i in result) {
                        Log.d("FireBaseRelated", i.get("comment").toString())
                    }
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("read comment")
        }
        Button(
            onClick = {
                FireBase.addComment(email,password)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("add comment")
        }
        Button(
            onClick = {
                FireBase.getFavorite(email){result ->
                    for(i in result) {
                        val person:Restaurant = gson.fromJson(i.get("item").toString(), Restaurant::class.java)
                        Log.d("FireBaseRelated",person.toString())
                    }
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("read favorite")
        }
        Button(
            onClick = {
                val r = Restaurant(password,"","","","","","","",Picture=null,Position=null,"","","","","","")
                FireBase.addFavorite(email,r)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("add favorite")
        }
    }
}