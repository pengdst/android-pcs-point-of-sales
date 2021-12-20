package io.github.pengdst.salescashier.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.viewpager.widget.ViewPager
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import io.github.pengdst.salescashier.R
import timber.log.Timber

fun Context?.longToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context?.shortToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.longToast(message: String?) {
    context.longToast(message)
}

fun Fragment.shortToast(message: String?) {
    context.shortToast(message)
}

fun Activity.longToast(message: String?) {
    applicationContext.longToast(message)
}

fun Activity.shortToast(message: String?) {
    applicationContext.shortToast(message)
}

fun onNavDestinationSelected(
    itemId: Int,
    navController: NavController
): Boolean {
    val builder = NavOptions.Builder()
        .setLaunchSingleTop(true)
        .setEnterAnim(R.animator.nav_default_enter_anim)
        .setExitAnim(R.animator.nav_default_exit_anim)
        .setPopEnterAnim(R.animator.nav_default_pop_enter_anim)
        .setPopExitAnim(R.animator.nav_default_pop_exit_anim)
        .setPopUpTo(itemId, true)
    val options = builder.build()
    return try {
        navController.navigate(itemId, null, options)
        true
    } catch (e: IllegalArgumentException) {
        false
    }
}

fun ChipNavigationBar.setupWithNavController(navController: NavController) {
    navController.addOnDestinationChangedListener { _, destination, _ ->
        setItemSelected(destination.id, true)
    }
    this.setOnItemSelectedListener { id ->
        onNavDestinationSelected(id, navController)
    }
}