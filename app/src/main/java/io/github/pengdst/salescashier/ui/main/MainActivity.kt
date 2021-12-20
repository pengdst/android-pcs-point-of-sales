package io.github.pengdst.salescashier.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.pengdst.libs.ui.activity.viewbinding.ActivityViewBindingDelegate.Companion.viewBindings
import io.github.pengdst.salescashier.R
import io.github.pengdst.salescashier.data.local.prefs.Session
import io.github.pengdst.salescashier.databinding.ActivityMainBinding
import io.github.pengdst.salescashier.ui.login.LoginActivity
import io.github.pengdst.salescashier.utils.setupWithNavController
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBindings()

    private val navController: NavController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
                as NavHostFragment
        navHostFragment.navController
    }

    @Inject
    lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!session.isLogin()) startActivity(Intent(applicationContext, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }).also {
            finish()
            return
        }

        setContentView(binding.root)

        with(binding) {
            toolbar.setupWithNavController(navController, AppBarConfiguration(
                setOf(
                    R.id.productFragment,
                    R.id.transactionFragment,
                    R.id.reportFragment,
                    R.id.aboutFragment,
                )
            ))
            bottomNav.setupWithNavController(navController)
        }
    }
}