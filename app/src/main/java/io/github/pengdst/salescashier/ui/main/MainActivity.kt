package io.github.pengdst.salescashier.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.pengdst.libs.ui.activity.viewbinding.ActivityViewBindingDelegate.Companion.viewBindings
import io.github.pengdst.salescashier.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBindings()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}