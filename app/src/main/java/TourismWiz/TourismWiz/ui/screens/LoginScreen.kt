package TourismWiz.TourismWiz.ui.screens

import TourismWiz.TourismWiz.R
import TourismWiz.TourismWiz.data.FireBase
import TourismWiz.TourismWiz.data.FireBase.Companion.gson
import TourismWiz.TourismWiz.data.MyUser
import TourismWiz.TourismWiz.data.MyUser.Companion.user
import TourismWiz.TourismWiz.model.Hotel
import TourismWiz.TourismWiz.model.Restaurant
import TourismWiz.TourismWiz.model.ScenicSpot
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.material.Text
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource

//callback: (result: MutableList<Restaurant>) -> Unit
@Composable
fun LoginScreen(field: String, myItem: Any?, show: Boolean, saveList: () -> Unit) {
    var isLoginDialogVisible by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val toast_mes = stringResource(id = R.string.field_toast)
    var icon: Painter
    if (myItem == null) {
        if (!show)
            icon = painterResource(R.drawable.heart_fill)
        else
            icon = painterResource(R.drawable.heart_empty)
    } else {
        icon = painterResource(R.drawable.bookmark_add)
        if (field == "Restaurant") {
            for (i in MyUser.restaurantList) {
                if ((myItem as Restaurant).RestaurantID == i.RestaurantID) {
                    icon = painterResource(R.drawable.bookmark_remove)
                    break
                }
            }
        } else if (field == "Hotel") {
            for (i in MyUser.hotelList) {
                if ((myItem as Hotel).HotelID == i.HotelID) {
                    icon = painterResource(R.drawable.bookmark_remove)
                    break
                }
            }
        } else {
            for (i in MyUser.scenicspotList) {
                if ((myItem as ScenicSpot).ScenicSpotID == i.ScenicSpotID) {
                    icon = painterResource(R.drawable.bookmark_remove)
                    break
                }
            }
        }

    }
    Column {
        IconButton(
            onClick = {
                if (user != null) {
                    if (myItem != null) {
                        user?.email?.let { FireBase.addFavorite(it, field, myItem, context) }
                    } else {
                        saveList()
                    }
                } else {
                    isLoginDialogVisible = true
                }
            }) {
            Image(
                painter = icon,
                contentDescription = "Heart Icon",
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .padding(start = 15.dp)
            )
        }
    }
    if (isLoginDialogVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures {
                        focusManager.clearFocus()
                    }
                }
        ) {
            AlertDialog(
                onDismissRequest = {
                    isLoginDialogVisible = false
                    focusManager.clearFocus()
                },
                buttons = {
                    Column() {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.6f),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.wiz),
                                contentDescription = "wiz",
                                modifier = Modifier.fillMaxWidth()

                            )
                            Text(
                                text = "Welcome",
                                color = Color.White,
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        OutlinedTextField(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            value = email,
                            onValueChange = { email = it },
                            label = { Text(text = stringResource(id = R.string.login_email)) }
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            value = password,
                            onValueChange = { password = it },
                            label = { Text(text = stringResource(id = R.string.login_password)) },
                            visualTransformation = PasswordVisualTransformation()
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Spacer(modifier = Modifier.width(16.dp))
                            Button(
                                onClick = {
                                    if (email != "" && password != "") {
                                        FireBase.loginAccount(email, password, context)
                                            ?.addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    user = task.result
                                                    if (user != null)
                                                        isLoginDialogVisible = false

                                                } else {
                                                    user = null
                                                }
                                            }
                                    } else
                                        Toast.makeText(
                                            context,
                                            toast_mes,
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = stringResource(id = R.string.login_btn_Login))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Button(
                                onClick = {
                                    if (email != "" && password != "") {
                                        FireBase.createAccount(email, password, context)
                                            ?.addOnCompleteListener { task ->
                                                user = task.result
                                                if (user != null)
                                                    isLoginDialogVisible = false
                                            }
                                    } else
                                        Toast.makeText(
                                            context,
                                            toast_mes,
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = stringResource(id = R.string.login_btn_Register))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                        }
                    }
                }
            )
        }
    }
}
