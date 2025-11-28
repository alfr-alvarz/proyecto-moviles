package com.example.tiendaguaumiau.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tiendaguaumiau.data.Usuario
import com.example.tiendaguaumiau.data.UsuarioConMascotas
import com.example.tiendaguaumiau.data.repository.PreferencesRepository
import com.example.tiendaguaumiau.data.repository.UsuarioRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @RelaxedMockK
    private lateinit var usuarioRepository: UsuarioRepository

    @RelaxedMockK
    private lateinit var preferencesRepository: PreferencesRepository

    private lateinit var viewModel: MainViewModel

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = MainViewModel(usuarioRepository, preferencesRepository)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun logout_userdebesernulo() = runTest {
        // Hacemos un mock de un usuario
        val fakeUser = UsuarioConMascotas(Usuario(1, "Test User", "test@duoc.cl", "", ""), emptyList())
        // Forzamos un estado inicial de "logueado"
        val loggedInUserField = viewModel.javaClass.getDeclaredField("_loggedInUser")
        loggedInUserField.isAccessible = true
        (loggedInUserField.get(viewModel) as MutableStateFlow<UsuarioConMascotas?>).value = fakeUser

        // Probamos el logout
        viewModel.logout()

        // Se revisa si el valor de loggedInUser es nulo (debe serlo)
        assertNull(viewModel.loggedInUser.value)
    }
}