package com.lds.quickdeal.login

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

import androidx.navigation.NavController
import com.unboundid.ldap.sdk.LDAPConnection
import com.unboundid.ldap.sdk.LDAPException


@Composable
fun LoginScreen(navController: NavController) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Авторизация", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Логин") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Button(
            onClick = {

                username = "reznichenko.i"
                password = "123@Aabbqq12"

                performAdLogin(context, username, password) {
                        isSuccess ->
                    if (isSuccess) {
                        navController.navigate("form") // Навигация при успешной авторизации
                    } else {
                        Toast.makeText(context, "Ошибка авторизации", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Войти")
        }
    }




}

fun performAdLogin(
    context: Context,
    username: String,
    password: String,
    onResult: (Boolean) -> Unit
) {





    val ldapHost = "ldsii.office.lds.ua" //ldsi.office.lds.ua или "ldsii.office.lds.ua"
    val ldapPort = 389 // LDAP
    val domain = "OFFICE" // NetBIOS-домен

    Thread {
        try {
            val ldapConnection = LDAPConnection(ldapHost, ldapPort)
            ldapConnection.bind("{$domain}\\$username", password)
            ldapConnection.close()


            Handler(Looper.getMainLooper()).post { onResult(true) }
        } catch (e: LDAPException) {
            e.printStackTrace()
            Handler(Looper.getMainLooper()).post { onResult(false) }
        }
    }.start()
}

