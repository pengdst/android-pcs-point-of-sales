package io.github.pengdst.salescashier.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.pengdst.libs.ui.activity.viewbinding.ActivityViewBindingDelegate.Companion.viewBindings
import io.github.pengdst.salescashier.data.local.prefs.Session
import io.github.pengdst.salescashier.data.remote.responses.ErrorResponse
import io.github.pengdst.salescashier.data.remote.routes.SalesRoute
import io.github.pengdst.salescashier.databinding.ActivityLoginBinding
import io.github.pengdst.salescashier.ui.main.MainActivity
import io.github.pengdst.salescashier.utils.longToast
import io.github.pengdst.salescashier.utils.shortToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by viewBindings()

    @Inject lateinit var salesRoute: SalesRoute
    @Inject lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {

            btnLogin.setOnClickListener {
                val email = tilEmail.editText?.text.toString().trim()
                val password = tilPassword.editText?.text.toString().trim()

                login(email, password)
            }
        }
    }

    fun login(email: String, password: String) {
        binding.btnLogin.isEnabled = false
        lifecycleScope.launchWhenCreated {
            try {
                withContext(Dispatchers.IO) {
                    val response = salesRoute.login(email, password)
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        val loginData = responseBody?.data ?: return@withContext

                        session.saveUser(loginData.admin ?: return@withContext, loginData.token)

                        withContext(Dispatchers.Main) {
                            startActivity(Intent(applicationContext, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            }).also {
                                finish()
                            }
                            shortToast(responseBody.message)
                        }
                    } else {
                        val errorBody = ErrorResponse.fromErrorBody(response.errorBody())
                        withContext(Dispatchers.Main) {
                            longToast(errorBody.message ?: "Login Failed")
                        }
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
            } finally {
                binding.btnLogin.isEnabled = true
            }
        }
    }
}