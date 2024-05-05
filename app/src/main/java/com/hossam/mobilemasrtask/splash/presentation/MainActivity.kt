package com.hossam.mobilemasrtask.splash.presentation

import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.hossam.mobilemasrtask.R
import com.hossam.mobilemasrtask.auth.presentation.LoginActivity
import com.hossam.mobilemasrtask.databinding.ActivityMainBinding
import com.hossam.mobilemasrtask.product.presentation.ProductActivity
import com.hossam.mobilemasrtask.splash.presentation.util.AuthState
import com.hossam.mobilemasrtask.util.extension.setSystemBarsPadding
import com.hossam.mobilemasrtask.util.extension.startActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mediaPlayer = MediaPlayer()
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainVM by viewModels()

    companion object {
        private const val INTRO_SOUND_PATH = "intro_sound.mp3"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSystemBarsPadding()

        animateTextLeftToRight()


    }

    override fun onStart() {
        super.onStart()
        bindMediaPlayer()
        navigate()
    }

    override fun onStop() {
        super.onStop()
        releaseMedia()
    }


    private fun navigate(){
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.checkAuthentication()
            observeSessionState()
        }
    }


    private suspend fun observeSessionState() {
            viewModel.state.collectLatest { state ->
                delay(4.seconds)
                when(state){
                    AuthState.Authenticated -> {
                        startActivity<ProductActivity>()
                        finish()
                    }

                    AuthState.NotAuthenticated -> {
                        startActivity<LoginActivity>()
                        finish()
                    }
                }
            }
    }

    private fun animateTextLeftToRight(){
        lifecycleScope.launch(Dispatchers.Main) {
            val screenWidth = resources.displayMetrics.widthPixels.toFloat()
            val animator = ObjectAnimator.ofFloat(binding.tvSplashTitle, "translationX", -screenWidth, 0f)
            animator.duration = 1000 // 1 second
            delay(1.seconds)
            binding.tvSplashTitle.isVisible = true
            animator.start()
        }
    }

    private fun bindMediaPlayer() {
        with(mediaPlayer) {
            setDataSource(assets.openFd(INTRO_SOUND_PATH))
            prepare()
            start()
        }
    }

    private fun releaseMedia() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.release()
        } else mediaPlayer.release()
    }

}