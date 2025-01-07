package dev.vijayakumar.userapp.view

import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.vijayakumar.userapp.R
import dev.vijayakumar.userapp.state.LoginUiState
import dev.vijayakumar.userapp.viewmodel.LoginViewModel
import dev.vijayakumar.userapp.viewmodel.SharedViewModel
import timber.log.Timber


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel(),
                 sharedViewModel: SharedViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
) {
    var username by remember { mutableStateOf("emilys") }
    var password by remember { mutableStateOf("emilyspass") }
    val loginState by viewModel.loginUIState.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = "Logo",
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            placeholder = { Text("Enter your username") },
            modifier = Modifier
                .padding(horizontal = 40.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            placeholder = { Text("Enter your password") },
            modifier = Modifier
                .padding(horizontal = 40.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { viewModel.loginOperation(username, password) },
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(20.dp))



        // Handle UI states
        when (loginState) {
            is LoginUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(20.dp)
                )
            }

            is LoginUiState.Success -> {
                val data = (loginState as LoginUiState.Success).loginResponse
                Timber.tag("LoginScreen").d("Login successful: %s", data)
                Text(
                    text = "Login successful: Welcome ${data.firstName}",
                    modifier = Modifier.padding(20.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                sharedViewModel.setLoginResponse(data)

            }

            is LoginUiState.Failure -> {
                val message = (loginState as LoginUiState.Failure).message
                Text(
                    text = "Login failed: $message",
                    modifier = Modifier.padding(20.dp),
                    color = MaterialTheme.colorScheme.error
                )
            }

            is LoginUiState.Error -> {
                Text(
                    text = "An unexpected error occurred. Please try again.",
                    modifier = Modifier.padding(20.dp),
                    color = MaterialTheme.colorScheme.error
                )
            }

            else -> {
                // Default UI if no specific state is active
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}




