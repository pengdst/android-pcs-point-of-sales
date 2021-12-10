package io.github.pengdst.salescashier.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.github.pengdst.libs.ui.activity.viewbinding.ActivityViewBindingDelegate.Companion.viewBindings
import io.github.pengdst.salescashier.data.local.prefs.SessionHelper
import io.github.pengdst.salescashier.data.remote.routes.SalesRoute
import io.github.pengdst.salescashier.databinding.ActivityLoginBinding
import timber.log.Timber

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by viewBindings()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}