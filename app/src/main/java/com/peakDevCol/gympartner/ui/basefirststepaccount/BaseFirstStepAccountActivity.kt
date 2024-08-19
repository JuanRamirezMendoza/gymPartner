package com.peakDevCol.gympartner.ui.basefirststepaccount

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.peakDevCol.gympartner.R
import com.peakDevCol.gympartner.core.dialog.BasicDialog
import com.peakDevCol.gympartner.databinding.ActivityBaseFirstStepAccountBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BaseFirstStepAccountActivity : AppCompatActivity() {
    companion object {
        fun create(context: Context, navigation: String): Intent {
            return Intent(context, BaseFirstStepAccountActivity::class.java).putExtra(
                "navigation",
                navigation
            )
        }
    }

    private lateinit var binding: ActivityBaseFirstStepAccountBinding

    private val baseFirstStepAccountViewModel: BaseFirstStepAccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBaseFirstStepAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUi()
        baseFirstStepAccountViewModel.nextScreenSelected(intent.extras?.getString("navigation")!!)
    }

    private fun initUi() {
        initObservers()
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                baseFirstStepAccountViewModel.baseViewState.collect { viewState ->
                    updateUi(viewState)
                }
            }
        }
    }

    private fun updateUi(viewState: BaseFirstStepAccountViewState?) {
        when (viewState) {
            is BaseFirstStepAccountViewState.Error -> {
                showError(viewState)
            }

            BaseFirstStepAccountViewState.Loading -> binding.pbLoading.isVisible = true
            null -> binding.pbLoading.isVisible = false
        }

    }

    private fun showError(infoError: BaseFirstStepAccountViewState.Error) {
        binding.pbLoading.isVisible = false
        BasicDialog.create(
            infoError.context,
            infoError.background,
            infoError.title,
            infoError.msg,
            infoError.positiveMsg,
        ) {
            it.dismiss()
        }
    }

}