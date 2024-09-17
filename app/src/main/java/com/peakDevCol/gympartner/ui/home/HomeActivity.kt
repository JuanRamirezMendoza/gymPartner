package com.peakDevCol.gympartner.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.peakDevCol.gympartner.R
import com.peakDevCol.gympartner.core.dialog.BasicDialog
import com.peakDevCol.gympartner.databinding.ActivityHomeBinding
import com.peakDevCol.gympartner.domain.ProviderTypeBodyPart
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), OnItemHero {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private val homeViewModel: HomeViewModel by viewModels()

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
        navController =
            (supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).navController
        initListeners()
    }

    private fun initListeners() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.perfil -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }

                R.id.logout -> {
                    showExitDialog()
                    true
                }

                else -> false
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showExitDialog() {
        BasicDialog.create(
            this,
            resources.getDrawable(R.drawable.dialog_bg),
            resources.getString(R.string.title_close_session),
            resources.getString(R.string.supporting_text_close_session),
            resources.getString(R.string.accept_close_session)
        ) {
            homeViewModel.logOut()
            it.dismiss()
        }
    }

    override fun itemHero(heroBodyPart: ProviderTypeBodyPart) {
        TODO("Not yet implemented")
    }
}