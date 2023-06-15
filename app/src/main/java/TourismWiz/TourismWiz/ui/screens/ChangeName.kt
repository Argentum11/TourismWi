package TourismWiz.TourismWiz.ui.screens

import TourismWiz.TourismWiz.R
import TourismWiz.TourismWiz.data.FireBase
import TourismWiz.TourismWiz.data.FireBase.Companion.changeName
import TourismWiz.TourismWiz.data.FireBase.Companion.gson
import TourismWiz.TourismWiz.data.MyUser
import TourismWiz.TourismWiz.data.MyUser.Companion.user
import TourismWiz.TourismWiz.model.Restaurant
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
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource

@Composable
fun ChangeName() {
    var name by remember { mutableStateOf("") }
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf(MyUser.user?.displayName ?: "") }
    var displayEmail by remember { mutableStateOf(MyUser.user?.email ?: "") }
    var isLoggedIn by remember { mutableStateOf(MyUser.user != null) }
    var isLoginDialogVisible by remember { mutableStateOf(false) }
    var isChange by remember { mutableStateOf(false) }
    val toast_mes= stringResource(id = R.string.field_toast)

    if (!isLoggedIn) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(id = R.string.before_Login_mes))
            Button(onClick = { isLoginDialogVisible = true }) {
                Text(text = stringResource(id = R.string.before_btn))
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.display_name)+" : "+ displayName ?: "")
            Text(text = stringResource(id = R.string.display_email)+" : "+displayEmail)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {  isChange = true }) {
                Text(text = stringResource(id = R.string.change_name_btn))
            }
            Button(onClick = {
                MyUser.user = null
                isLoggedIn = false
            }) {
                Text(text = stringResource(id = R.string.logout_btn))
            }
        }
    }

    if (isChange) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures {
                    }
                }
        ) {
            AlertDialog(
                onDismissRequest = {isChange=false},
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
                            value = name,
                            onValueChange = { name = it },
                            label = { Text(text = stringResource(id = R.string.new_name)) }
                        )
                        Button(
                            onClick = {
                                if (name.isNotBlank()) {
                                    FireBase.changeName(name, context)
                                    if(user!=null) {
                                        displayName=name
                                        displayEmail=user?.email!!
                                        name=""
                                        isChange=false
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        toast_mes,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                                ,modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = stringResource(id = R.string.change_btn))
                        }
                    }
                }
            )
        }
    }
    if(isLoginDialogVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures {
                    }
                }
        ) {
            AlertDialog(
                onDismissRequest = {
                    isLoginDialogVisible = false
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
                                        FireBase.loginAccount(email, password, context)?.addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                user = task.result
                                                if(user!=null) {
                                                    isLoginDialogVisible = false
                                                    isLoggedIn=true
                                                    displayName=user?.displayName ?: ""
                                                    displayEmail=user?.email!!
                                                }
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
                                        FireBase.createAccount(email, password, context)?.addOnCompleteListener { task ->
                                            user=task.result
                                            if(user!=null) {
                                                isLoginDialogVisible = false
                                                isLoggedIn=true
                                                displayName=user?.displayName ?: ""
                                                displayEmail=user?.email!!
                                            }
                                        }
                                    }
                                    else
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

/*
@Composable
fun ChangeName() {
    var isLoginDialogVisible by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf(MyUser.user?.displayName ?: "") }
    var displayEmail by remember { mutableStateOf(MyUser.user?.email ?: "") }
    var isLogin by remember { mutableStateOf(MyUser.user != null) }

    if(!isLogin){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("請先進行登入!")
            Button(onClick = {isLoginDialogVisible=true}) {
                Text(text = "登入")
            }
        }
        if(isLoginDialogVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures {
                        }
                    }
            ) {
                AlertDialog(
                    onDismissRequest = {
                        isLoginDialogVisible = false
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
                                label = { Text(text = "Email") }
                            )
                            OutlinedTextField(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally),
                                value = password,
                                onValueChange = { password = it },
                                label = { Text(text = "Password") },
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
                                            FireBase.loginAccount(email, password, context)?.addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    user = task.result
                                                    if(user!=null)
                                                        isLoginDialogVisible = false

                                                } else {
                                                    user = null
                                                }
                                            }
                                        } else
                                            Toast.makeText(
                                                context,
                                                "field must have value",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = "Login")
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Button(
                                    onClick = {
                                        if (email != "" && password != "") {
                                            FireBase.createAccount(email, password, context)?.addOnCompleteListener { task ->
                                                user=task.result
                                                if(user!=null)
                                                    isLoginDialogVisible = false
                                            }
                                        }
                                        else
                                            Toast.makeText(
                                                context,
                                                "field must have value",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = "Register")
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                            }
                        }
                    }
                )
            }
        }
    }
    else{
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "名字 : $displayName")
            Text(text = "帳號 : $displayEmail")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {isLoginDialogVisible=true}) {
                Text(text = "修改名字")
            }
            Button(onClick = {MyUser.user=null}) {
                Text(text = "登出")
            }
        }
        if(isLoginDialogVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures {
                        }
                    }
            ) {
                AlertDialog(
                    onDismissRequest = {
                        isLoginDialogVisible = false
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
                                value = name,
                                onValueChange = { name = it },
                                label = { Text(text = "Name") }
                            )
                            Button(
                                onClick = {
                                    if (name!=null) {
                                        FireBase.changeName(name,context)
                                        FireBase.loginAccount(email, password, context)?.addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                user = task.result
                                                if(user!=null)
                                                    isLoginDialogVisible = false
                                            } else {
                                                user = null
                                            }
                                        }
                                    } else
                                        Toast.makeText(
                                            context,
                                            "field must have value",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                },
                            ) {
                                Text(text = "修改")
                            }
                        }
                    }
                )
            }
        }
    }
}

 */
