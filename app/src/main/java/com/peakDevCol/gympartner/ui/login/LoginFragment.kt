package com.peakDevCol.gympartner.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.peakDevCol.gympartner.core.ex.dismissKeyboard
import com.peakDevCol.gympartner.core.ex.loseFocusAfterAction
import com.peakDevCol.gympartner.core.ex.onTextChanged
import com.peakDevCol.gympartner.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
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

    /**
     * I chose the scopeFunction .with instead of .apply because when you use .apply you
     * need to change properties of the object and return it. But when you only need to work
     * with the object context and you don´t need to return it. .with is the best choice.
     */
    private fun initListeners() {
        with(binding) {
            etEmail.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            etEmail.onTextChanged { onFieldChanged() }

            binding.etPassword.loseFocusAfterAction(EditorInfo.IME_ACTION_DONE)
            etPassword.onTextChanged { onFieldChanged() }

            btnLogin.setOnClickListener {
                it.dismissKeyboard()
                viewModel.onLoginSelected(etEmail.text.toString(), etPassword.text.toString())
            }

        }

    }

    private fun onFieldChanged() {
            viewModel.onFieldsChanged(
                email = binding.etEmail.text.toString(),
                password = binding.etPassword.text.toString()
            )
    }

    private fun initObservers() {
        // use viewLifecycleOwner because in this form the coroutine are match with the
        // lifecycle of fragment, but if you only use lifecycle the coroutine its match for activity lifecycle
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { viewState ->
                    updateUi(viewState)
                }
            }
        }


    }

    private fun updateUi(viewState: LoginViewState) {
        with(binding) {
            tilEmail.error = if (viewState.isValidEmail) null else "email"
            tilPassword.error = if (viewState.isValidPassword) null else "password"
        }
    }

}