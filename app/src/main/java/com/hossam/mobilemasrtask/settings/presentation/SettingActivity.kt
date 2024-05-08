package com.hossam.mobilemasrtask.settings.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.hossam.mobilemasrtask.R
import com.hossam.mobilemasrtask.auth.presentation.login.LoginActivity
import com.hossam.mobilemasrtask.databinding.ActivitySettingBinding
import com.hossam.mobilemasrtask.settings.presentation.util.SettingState
import com.hossam.mobilemasrtask.splash.presentation.MainVM
import com.hossam.mobilemasrtask.util.extension.onClick
import com.hossam.mobilemasrtask.util.extension.setSystemBarsPadding
import com.hossam.mobilemasrtask.util.extension.setVisibleOrGone
import com.hossam.mobilemasrtask.util.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private val viewModel: SettingVM by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackEvent()
        onLogoutEvent()
        collectState()
    }


    /**
     * Sets up the back button click event listener.
     * When the back button is clicked, the current activity finishes.
     */
    private fun onBackEvent() {
        binding.btnBack.onClick {
            finish()
        }
    }

    /**
     * Sets up the logout button click event listener.
     * When the logout button is clicked, the [viewModel] triggers the logout operation.
     */
    private fun onLogoutEvent() {
        binding.btnLogout.onClick { viewModel.logout() }
    }

    /**
     * Observes the state emitted by [viewModel] and reacts accordingly.
     * If the state is Loading, shows or hides the loading container based on [SettingState.isLoading].
     * If the state is Success, navigates to the LoginActivity, clearing the task stack.
     * If the state is Error, shows a toast with the [SettingState.message].
     */
    private fun collectState() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.state.collectLatest { state ->
                when(state) {
                    is SettingState.Loading -> binding.loadingContainer.setVisibleOrGone(state.isLoading)
                    is SettingState.Success -> navigateToLogin()
                    is SettingState.Error -> showToast(state.message)
                }
            }
        }
    }

    /**
     * Navigates to the LoginActivity, clearing the task stack and all previous activities.
     */
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

}