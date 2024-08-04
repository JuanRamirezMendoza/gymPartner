package com.peakDevCol.gympartner.ui.basefirststepaccount

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.peakDevCol.gympartner.R
import com.peakDevCol.gympartner.databinding.ActivityBaseFirstStepAccountBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BaseFirstStepAccountActivity : AppCompatActivity() {
    companion object {
        fun create(context: Context, navigation: String): Intent {
            return Intent(context, BaseFirstStepAccountActivity::class.java).putExtra("navigation", navigation)
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
        baseFirstStepAccountViewModel.nextScreenSelected(intent.extras?.getString("navigation")!!)
    }
}