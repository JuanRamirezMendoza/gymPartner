package com.peakDevCol.gympartner.ui.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.peakDevCol.gympartner.core.ex.dismissKeyboard
import com.peakDevCol.gympartner.core.ex.loseFocusAfterAction
import com.peakDevCol.gympartner.core.ex.onTextChanged
import com.peakDevCol.gympartner.core.ex.toast
import com.peakDevCol.gympartner.databinding.FragmentSignInBinding
import com.peakDevCol.gympartner.ui.signin.model.UserSignIn
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment() {

    companion object {
        fun newInstance() = SignInFragment()
    }


    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val signInViewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        initObservers()
        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            etFullName.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            etFullName.onTextChanged { onFieldChanged() }

            etEmail.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            etEmail.onTextChanged { onFieldChanged() }

            etPassword.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            etPassword.onTextChanged { onFieldChanged() }

            etConfirmPassword.loseFocusAfterAction(EditorInfo.IME_ACTION_DONE)
            etConfirmPassword.onTextChanged { onFieldChanged() }

            btnCreateAcount.setOnClickListener {
                it.dismissKeyboard()
                signInViewModel.onSignInSelected(
                    UserSignIn(
                        fullName = etFullName.text.toString(),
                        email = etEmail.text.toString(),
                        password = etPassword.text.toString(),
                        passwordConfirmation = etConfirmPassword.text.toString()
                    )
                )
            }
        }
    }

    private fun onFieldChanged(hasFocus: Boolean = false) {
        if (!hasFocus) {
            signInViewModel.onFieldsChanged(
                UserSignIn(
                    fullName = binding.etFullName.text.toString(),
                    email = binding.etEmail.text.toString(),
                    password = binding.etPassword.text.toString(),
                    passwordConfirmation = binding.etConfirmPassword.text.toString()
                )
            )
        }
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                signInViewModel.viewState.collect { viewState ->
                    updateUi(viewState)
                }
            }
        }

        signInViewModel.showError.observe(viewLifecycleOwner) {
            if (it) requireActivity().toast("Error")
        }

        signInViewModel.navigateToOtherScreen.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                requireActivity().toast("Navega a otra pantalla")
            }
        }

    }

    private fun updateUi(viewState: SignInViewState) {
        with(binding) {
            pbLoading.isVisible = viewState.isLoading
            tilFullName.error = if (viewState.isValidFullName) null else "Name no valido"
            tilEmail.error = if (viewState.isValidEmail) null else "Email no valido"
            tilPassword.error = if (viewState.isValidPassword) null else "Contraseñas no coinciden"
            tilConfirmPassword.error =
                if (viewState.isValidPassword) null else "Contraseñas no coinciden"
        }
    }
}