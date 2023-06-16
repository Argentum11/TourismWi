package TourismWiz.TourismWiz.data

import TourismWiz.TourismWiz.R
import TourismWiz.TourismWiz.data.MyUser.Companion.user
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CommentAdd(id:String) {
    val options = listOf(1,2,3,4,5)
    var expanded by remember { mutableStateOf(false) }
    var isLoginDialogVisible by remember { mutableStateOf(false) }
    var isCommentDialogVisible by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val inExpandedChange={isExpanded:Boolean->expanded=isExpanded}
    val stars=listOf("","✰","✰✰","✰✰✰","✰✰✰✰","✰✰✰✰✰")
    val toast_mes= stringResource(id = R.string.field_toast)

    Column {
        IconButton(
            onClick = {
                if (user!=null) {
                    isCommentDialogVisible = true
                }
                else {
                    isLoginDialogVisible = true
                }
            }) {
            Image(
                painter = painterResource(R.drawable.commenticon),
                contentDescription = "Heart Icon",
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
            )
        }
    }
    if(isLoginDialogVisible) {
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
                    Column {
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
                                                if(user!=null)
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
                                        FireBase.createAccount(email, password, context)?.addOnCompleteListener { task ->
                                            user=task.result
                                            if(user!=null)
                                                isLoginDialogVisible = false
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
    if(isCommentDialogVisible){
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
                    isCommentDialogVisible=false
                    focusManager.clearFocus()
                },
                buttons = {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.6f),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.commenticon),
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
                        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = inExpandedChange) {
                            TextField(
                                if(rate=="") stringResource(id = R.string.dropdown_default) else stars[rate.toInt()],
                                {},
                                readOnly = true,
                                label = { Text(text = stringResource(id = R.string.dropdown_title)) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                                modifier = Modifier.fillMaxWidth()
                            )
                            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { inExpandedChange(false) }) {
                                options.forEach { option ->
                                    DropdownMenuItem(onClick = {
                                        rate = option.toString()
                                        inExpandedChange(false)
                                    }) {
                                        Text(text = stars[option])
                                    }
                                }
                            }
                        }
                        OutlinedTextField(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            value = comment,
                            onValueChange = { comment = it },
                            label = { Text(text = stringResource(id = R.string.comment)) },
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = {
                                    if (rate != "" && comment != "") {
                                        MyUser.user?.email?.let {
                                            MyUser.user?.displayName?.let { it1 ->
                                                FireBase.addComment(id,rate,
                                                    it, it1,comment)
                                            }
                                        }
                                        isCommentDialogVisible=false
                                    }
                                    else
                                        Toast.makeText(
                                            context,
                                            toast_mes,
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                            {
                                Text(text = stringResource(id = R.string.comment_send))
                            }
                        }
                    }
                }
            )
        }
    }
}