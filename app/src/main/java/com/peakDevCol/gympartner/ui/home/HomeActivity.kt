package com.peakDevCol.gympartner.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.peakDevCol.gympartner.R
import com.peakDevCol.gympartner.core.ex.toast
import com.peakDevCol.gympartner.databinding.ActivityHomeBinding
import com.peakDevCol.gympartner.domain.ProviderTypeBodyPart
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), OnItemHero {

    private lateinit var binding: ActivityHomeBinding
    private var backPressedOnce = false
    private val backPressTimeLimit = 2000L // 2 seconds

    companion object {
        fun create(context: Context) = Intent(context, HomeActivity::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(backPressedOnce){
                finish()
            }
            backPressedOnce = true
            toast(getString(R.string.press_back_again_to_exit))

            // Reset the backPressedOnce flag after 2 seconds
            Handler(Looper.getMainLooper()).postDelayed({
                backPressedOnce = false
            }, backPressTimeLimit)
        }
    }

    override fun itemHero(heroBodyPart: ProviderTypeBodyPart) {
        TODO("Not yet implemented")
    }
}