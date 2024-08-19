package com.peakDevCol.gympartner.ui.loading

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.peakDevCol.gympartner.R
import com.peakDevCol.gympartner.databinding.FragmentLoadingBinding
import com.peakDevCol.gympartner.ui.basefirststepaccount.BaseFirstStepAccountViewModel.Companion.navigateToNextScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoadingFragment : Fragment() {


    private var _binding: FragmentLoadingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoadingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        initObservers()
    }

    private fun initObservers() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            navigateToNextScreen.observe(viewLifecycleOwner) { eventDestination ->
                eventDestination.getContentIfNotHandled()?.let {
                    val destination = when (it) {
                        "signIn" -> R.id.action_loadingFragment_to_sigInFragment
                        "Login" -> R.id.action_loadingFragment_to_loginFragment
                        else -> R.id.action_loadingFragment_to_sigInFragment
                    }
                    findNavController().navigate(
                        destination,
                        null,
                        NavOptions.Builder().setPopUpTo(R.id.loadingFragment, true).build()
                    )
                }
            }
        }, 2000)
    }

}

