package dev.vijayakumar.userapp.view


import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.vijayakumar.userapp.model.response.User
import dev.vijayakumar.userapp.state.UsersUIState
import dev.vijayakumar.userapp.viewmodel.LoginViewModel


@Composable
fun UserListScreen( navController: NavController , viewmodel:LoginViewModel = hiltViewModel()){


    LaunchedEffect(Unit) {
        viewmodel.getUserOperation()

    }

    val usersState = viewmodel.userUIState.collectAsState()

    Column (modifier = Modifier.fillMaxSize(),verticalArrangement = Arrangement.Center,  horizontalAlignment = Alignment.CenterHorizontally,){

        when (val state = usersState.value) {
            is UsersUIState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(20.dp)
                )
            }


            is UsersUIState.Success -> {
                val userList =state.userListResponse.users
                UserListContent(
                    userList)

            }

            is UsersUIState.Failure -> {
                ErrorScreen(message = state.message, onRetry = { viewmodel.getUserOperation() })
            }

            else  -> {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }


    }


}


@Composable
fun UserListContent(users: List<User>) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 100.dp)) {
        items(users) { usersData ->
            UserCard(user = usersData)
        }
    }
}

@Composable
fun UserCard(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Text(text = "${user.firstName} ${user.lastName}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}


@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}