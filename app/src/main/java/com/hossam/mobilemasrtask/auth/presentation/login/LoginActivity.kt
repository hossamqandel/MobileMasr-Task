package com.hossam.mobilemasrtask.auth.presentation.login

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hossam.mobilemasrtask.auth.presentation.login.utl.LoginState
import com.hossam.mobilemasrtask.databinding.ActivityLoginBinding
import com.hossam.mobilemasrtask.product.presentation.ProductActivity
import com.hossam.mobilemasrtask.util.extension.onClick
import com.hossam.mobilemasrtask.util.extension.setSystemBarsPadding
import com.hossam.mobilemasrtask.util.extension.showSnackbar
import com.hossam.mobilemasrtask.util.extension.startActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSystemBarsPadding()

        submitLoginEvent()
        observeValidation()
        observeState()
    }

    private fun submitLoginEvent(){
        binding.btnLogin.onClick {
            startAuthentication()
        }

    }

    private fun startAuthentication(){
        with(binding){
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            viewModel.submit(email, password)
        }
    }

    private fun observeValidation(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                combine(viewModel.emailValidation, viewModel.passwordValidation) { email, password ->
                    binding.etEmail.error = email?.let { getString(email) }
                    binding.etPassword.error = password?.let { getString(password) }
                }.collect()
            }
        }
    }
    
    private fun observeState(){
        lifecycleScope.launch { 
            viewModel.state.collectLatest { state -> 
                when(state){
                    is LoginState.Loading -> binding.progressBar.isVisible = state.isLoading
                    is LoginState.Success -> {
                        startActivity<ProductActivity>().also {
                            finish()
                        }
                    }
                    is LoginState.Error -> { binding.root.showSnackbar(state.message) }
                }
            }
        }
    }



}