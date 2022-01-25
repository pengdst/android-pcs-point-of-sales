package io.github.pengdst.salescashier.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import io.github.pengdst.salescashier.R
import io.github.pengdst.salescashier.ui.main.MainActivity
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        lifecycleScope.launchWhenCreated {
            delay(4000)
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }
}