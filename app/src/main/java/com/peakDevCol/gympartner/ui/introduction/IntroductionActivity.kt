package com.peakDevCol.gympartner.ui.introduction

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.peakDevCol.gympartner.databinding.ActivityIntroductionBinding
import com.peakDevCol.gympartner.ui.basefirststepaccount.BaseFirstStepAccountActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroductionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroductionBinding

    private val introductionViewModel: IntroductionViewModel by viewModels()


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

    }

    private fun initObservers() {
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

    }

    private fun goToSignIn() {
        startActivity(BaseFirstStepAccountActivity.create(this, "signIn"))

    }

    private fun goToLogin() {
        startActivity(BaseFirstStepAccountActivity.create(this, "Login"))

    }

}