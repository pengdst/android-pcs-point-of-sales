package io.github.pengdst.salescashier.ui.about

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import io.github.pengdst.libs.ui.fragment.viewbinding.FragmentViewBindingDelegate.Companion.viewBindings
import io.github.pengdst.salescashier.data.local.prefs.Session
import io.github.pengdst.salescashier.databinding.FragmentAboutBinding
import io.github.pengdst.salescashier.ui.login.LoginActivity
import javax.inject.Inject

@AndroidEntryPoint
class AboutFragment : Fragment() {

    private val binding: FragmentAboutBinding by viewBindings()

    @Inject
    lateinit var session: Session

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnLogout.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Logout Account")
                    .setMessage("Are you sure Logout your account?")
                    .setPositiveButton("Logout") { _, _ ->
                        session.logout()
                        startActivity(Intent(requireContext(), LoginActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        }).also {
                            requireActivity().finish()
                        }
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }.show()
            }
        }
    }
}