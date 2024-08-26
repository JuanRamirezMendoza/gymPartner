package com.peakDevCol.gympartner.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.HeroCarouselStrategy
import com.peakDevCol.gympartner.R
import com.peakDevCol.gympartner.databinding.ActivityHomeBinding
import com.peakDevCol.gympartner.domain.ProviderTypeBodyPart
import com.peakDevCol.gympartner.ui.introduction.IntroductionActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var carouselHomeAdapter: CarouselHomeAdapter
    private val homeViewModel: HomeViewModel by viewModels()

    private val bodyPart = listOf(
        ProviderTypeBodyPart.BACK,
        ProviderTypeBodyPart.CARDIO,
        ProviderTypeBodyPart.CHEST,
        ProviderTypeBodyPart.LOWER_ARMS,
        ProviderTypeBodyPart.LOWER_LEGS,
        ProviderTypeBodyPart.NECK,
        ProviderTypeBodyPart.SHOULDERS,
        ProviderTypeBodyPart.UPPER_ARMS,
        ProviderTypeBodyPart.UPPER_LEGS,
        ProviderTypeBodyPart.WAIST,
    )

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
        initUi()

    }

    private fun initUi() {
        setUpRecyclerView()
        initObservers()
        initListeners()
        initCallServices()
        //getBodyPartExerciseList()
    }

    private fun initCallServices() {
        homeViewModel.getLocalBodyPart()
        getBodyPartList()
    }

    private fun getBodyPartExerciseList() {
        lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.callBodyPartExerciseList("back")
            }
        }
    }

    private fun getBodyPartList() {
        lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.callBodyPart()
            }
        }
    }

    private fun setUpRecyclerView() {
        val layoutManager =
            object : CarouselLayoutManager(HeroCarouselStrategy()) {
                override fun canScrollVertically(): Boolean = false
            }.apply {
                carouselAlignment = CarouselLayoutManager.ALIGNMENT_CENTER
            }
        binding.carouselRecyclerView.layoutManager = layoutManager

        carouselHomeAdapter = CarouselHomeAdapter(bodyPart)
        binding.carouselRecyclerView.adapter = carouselHomeAdapter

        val snapHelper = CarouselSnapHelper()
        snapHelper.attachToRecyclerView(binding.carouselRecyclerView)
    }


    private fun initObservers() {
        homeViewModel.bodyPart.observe(this) {
            if (it != null) {
                Log.e("bodyPart", it.toString())
            }
        }
        homeViewModel.bodyPartExercises.observe(this) {
            Log.e("bodyPartExercises", it.toString())
        }

        homeViewModel.navigateToIntroduction.observe(this) {
            it.getContentIfNotHandled()?.let {
                goToIntroduction()
            }
        }

    }

    private fun goToIntroduction() {
        startActivity(IntroductionActivity.create(this))
        finish()
    }

    private fun initListeners() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.perfil -> {
                    true
                }

                R.id.logout -> {
                    homeViewModel.logOut()
                    true
                }

                else -> false
            }
        }
    }

}