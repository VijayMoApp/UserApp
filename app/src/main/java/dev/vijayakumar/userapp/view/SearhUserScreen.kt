package dev.vijayakumar.userapp.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.vijayakumar.userapp.model.response.User
import dev.vijayakumar.userapp.model.response.UserSearch
import dev.vijayakumar.userapp.state.SearchUIState
import dev.vijayakumar.userapp.view.utils.toUsers
import dev.vijayakumar.userapp.viewmodel.LoginViewModel



@Composable
fun SearchUserScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    // State variables
    val searchState by viewModel.searchUIState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Search Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { query ->
                searchQuery = query

            },
            label = { Text("Username") },
            placeholder = { Text("Enter your username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (searchQuery.isNotEmpty()) {
                viewModel.getUserListByName(searchQuery)
            }
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)) {

            Text(text = "Search", modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp))
        }


        when (val state = searchState) {
            is SearchUIState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is SearchUIState.Success -> {
                if (state.searchByUserListResponse.users.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No users found")
                    }
                } else {

                    SearchUserList(
                        SearchUsers = state.searchByUserListResponse.users,
                        onSaveClick = { user ->
                            viewModel.insertUsers(user)
                        }
                    )
                }
            }

            is SearchUIState.Failure -> {
                // Show error message
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            else -> {
                // Handle empty or unknown state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Start searching for users!")
                }
            }
        }
    }
}

@Composable
fun SearchUserList(
    SearchUsers: List<User>,
    onSaveClick: (UserSearch) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp)
    ) {
        items(SearchUsers) { searchus ->

            SearchUserCard(user = searchus, onSaveClick = onSaveClick)
        }
    }
}

@Composable
fun SearchUserCard(user: User, onSaveClick: (UserSearch) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${user.firstName} ${user.lastName}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Image(
                imageVector = Icons.Default.Person,
                contentDescription = "Person Icon",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onSaveClick(user.toUserSearch()) // Convert User to UserSearch before passing
                    }
            )
        }
    }
}

fun User.toUserSearch(): UserSearch {
    return UserSearch(
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email
    )
}
