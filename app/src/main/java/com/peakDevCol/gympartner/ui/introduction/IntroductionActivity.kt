package com.peakDevCol.gympartner.ui.introduction

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.peakDevCol.gympartner.R
import com.peakDevCol.gympartner.core.dialog.BasicDialog
import com.peakDevCol.gympartner.databinding.ActivityIntroductionBinding
import com.peakDevCol.gympartner.ui.basefirststepaccount.BaseFirstStepAccountActivity
import com.peakDevCol.gympartner.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class IntroductionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroductionBinding

    private val introductionViewModel: IntroductionViewModel by viewModels()

    @Inject
    lateinit var getGoogleIdOption: GetGoogleIdOption

    @Inject
    lateinit var credentialManager: CredentialManager

    companion object {
        fun create(context: Context) = Intent(context, IntroductionActivity::class.java)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityIntroductionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUi()
    }

    private fun initUi() {
        initListeners()
        initObservers()
    }

    private fun initListeners() {
        binding.loginBtn.setOnClickListener {
            introductionViewModel.onLoginSelected()
        }

        binding.signInBtn.setOnClickListener {
            introductionViewModel.onSingInSelected()
        }

        binding.googleBtn.setOnClickListener {
            introductionViewModel.onGoogleSelected()
        }

    }

    private fun initObservers() {

        introductionViewModel.navigateToMenu.observe(this) {
            it.getContentIfNotHandled()?.let { hasCurrentUser ->
                if (hasCurrentUser)
                    goToHome()
            }
        }

        introductionViewModel.navigateToLogin.observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                goToLogin()
            }

        }

        introductionViewModel.navigateToSignIn.observe(this) {
            it.getContentIfNotHandled()?.let {
                goToSignIn()
            }
        }

        introductionViewModel.googleSelected.observe(this) {
            it.getContentIfNotHandled()?.let {
                getCredentialRequest()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                introductionViewModel.viewState.collect { viewState ->
                    updateUi(viewState)
                }
            }
        }
    }

    private fun updateUi(viewState: IntroductionViewState?) {
        when (viewState) {
            IntroductionViewState.Error -> showError()
            IntroductionViewState.Loading -> binding.pbLoading.isVisible = true
            null -> binding.pbLoading.isVisible = false
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showError() {
        BasicDialog.create(
            this,
            resources.getDrawable(R.drawable.dialog_bg, this.theme),
            resources.getString(R.string.title_login_google),
            resources.getString(R.string.supporting_text_login_google),
            resources.getString(R.string.accept_login_google)
        ) {
            it.dismiss()
        }
    }

    private fun getCredentialRequest() {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(getGoogleIdOption)
            .build()
        lifecycleScope.launch {
            introductionViewModel.getCredentials(this@IntroductionActivity, request)
        }
    }

    private fun goToHome() {
        startActivity(HomeActivity.create(this))

    }

    private fun goToSignIn() {
        startActivity(BaseFirstStepAccountActivity.create(this, "signIn"))

    }

    private fun goToLogin() {
        startActivity(BaseFirstStepAccountActivity.create(this, "Login"))

    }

}