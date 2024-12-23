package dev.vijayakumar.userapp

import dev.vijayakumar.userapp.model.response.LoginResponse
import dev.vijayakumar.userapp.model.response.User
import dev.vijayakumar.userapp.repository.LocalRepository
import dev.vijayakumar.userapp.repository.LoginRepository
import dev.vijayakumar.userapp.state.LoginUiState
import dev.vijayakumar.userapp.state.UsersUIState
import dev.vijayakumar.userapp.viewmodel.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Response


@OptIn(ExperimentalCoroutinesApi::class)
class LoginOperationViewModelTest {


    private lateinit var viewModel: LoginViewModel
    private lateinit var loginRepository: LoginRepository
    private lateinit var localRepository: LocalRepository
    private val testDispatcher = StandardTestDispatcher()
    @Before
    fun setUp() {
        // Set the Main dispatcher to the test dispatcher
        Dispatchers.setMain(testDispatcher)
        // Mock the repository
        loginRepository = mock(LoginRepository::class.java)
        localRepository = mock(LocalRepository::class.java)

        // Create the ViewModel, passing the mocked repository
        viewModel = LoginViewModel(loginRepository , localRepository)
    }

    @After
    fun tearDown() {
        // Reset the Main dispatcher
        Dispatchers.resetMain()
    }

    @Test
    fun `loginOperation success updates UI state to Success`() = runTest {
        // Given
        val username = "testuser"
        val password = "testpassword"
        val response = Response.success(LoginResponse("token", "vj@gmail.com","testuser","male",1))
        `when`(loginRepository.loginUser(username, password)).thenReturn(response)

        // When
        viewModel.loginOperation(username, password)

        // Then
        assertEquals(LoginUiState.Loading, viewModel.loginUIState.value)  // Verify Loading state initially
        advanceUntilIdle() // Ensure the coroutine is finished

        assertTrue(viewModel.loginUIState.value is LoginUiState.Success)
        val successState = viewModel.loginUIState.value as LoginUiState.Success
        assertEquals("testuser", successState.loginResponse.firstName)
        assertEquals("token", successState.loginResponse.accessToken)
    }

    @Test
    fun `loginOperation failure updates UI state to Failure`() = runTest {
        // Given
        val username = "testuser"
        val password = "wrongpassword"
        val response = Response.error<LoginResponse>(400, "".toResponseBody())
        `when`(loginRepository.loginUser(username, password)).thenReturn(response)

        // When
        viewModel.loginOperation(username, password)

        // Then
        assertEquals(LoginUiState.Loading, viewModel.loginUIState.value)  // Verify Loading state initially
        advanceUntilIdle() // Ensure the coroutine is finished

        assertTrue(viewModel.loginUIState.value is LoginUiState.Failure)
        val failureState = viewModel.loginUIState.value as LoginUiState.Failure
        assertEquals("Something went wrong", failureState.message)
    }

    @Test
    fun `loginOperation error updates UI state to Error`() = runTest {
        // Given
        val username = "testuser"
        val password = "testpassword"
        `when`(loginRepository.loginUser(username, password))
            .thenThrow(RuntimeException("Network error"))  // Use unchecked exception like RuntimeException

        // When
        viewModel.loginOperation(username, password)

        // Then
        assertEquals(LoginUiState.Loading, viewModel.loginUIState.value)  // Verify Loading state initially
        advanceUntilIdle() // Ensure the coroutine is finished

        assertTrue(viewModel.loginUIState.value is LoginUiState.Error)
    }




    @Test
    fun `getUserOperation emits Success state with user data`() = runTest {
        // Given
        val usersList = listOf(
            User("John", "Doe" ,"" ),
            User("Jane", "Doe","")
        )
        `when`(loginRepository.getUsers()).thenReturn(flow { emit(usersList) })

        // When
        viewModel.getUserOperation()

        // Then
        assertEquals(UsersUIState.Loading, viewModel.userUIState.value) // Verify Loading state
        advanceUntilIdle() // Wait for coroutine to finish
        assertTrue(viewModel.userUIState.value is UsersUIState.Success)
        val successState = viewModel.userUIState.value as UsersUIState.Success
        assertEquals(usersList, successState.userListResponse.users)
    }

    @Test
    fun `getUserOperation emits Failure state on error`() = runTest {
        // Given
        val errorMessage = "Network error"
        `when`(loginRepository.getUsers()).thenReturn(flow { throw RuntimeException(errorMessage) })

        // When
        viewModel.getUserOperation()

        // Then
        assertEquals(UsersUIState.Loading, viewModel.userUIState.value) // Verify Loading state
        advanceUntilIdle() // Wait for coroutine to finish

        assertTrue(viewModel.userUIState.value is UsersUIState.Failure)
        val failureState = viewModel.userUIState.value as UsersUIState.Failure
        assertEquals(errorMessage, failureState.message)
    }





}