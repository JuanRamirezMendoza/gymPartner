package com.peakDevCol.gympartner.ui.home

import android.annotation.SuppressLint
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.HeroCarouselStrategy
import com.peakDevCol.gympartner.R
import com.peakDevCol.gympartner.core.dialog.BasicDialog
import com.peakDevCol.gympartner.core.dialog.LoadingDialog
import com.peakDevCol.gympartner.core.ex.capitalizeFirstLetter
import com.peakDevCol.gympartner.data.response.BodyPartExerciseResponse
import com.peakDevCol.gympartner.databinding.ActivityHomeBinding
import com.peakDevCol.gympartner.domain.ProviderTypeBodyPart
import com.peakDevCol.gympartner.ui.introduction.IntroductionActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), OnItemHero {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var carouselHomeAdapter: CarouselHomeAdapter
    private val homeViewModel: HomeViewModel by viewModels()

    private val showLoading = LoadingDialog

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
        initCallServices()
        setUpRecyclerView()
        initObservers()
        initListeners()
    }

    private fun initCallServices() {
        homeViewModel.getLocalBodyPart()
    }

    private fun setUpRecyclerView() {
        val layoutManager =
            object : CarouselLayoutManager(HeroCarouselStrategy()) {
                override fun canScrollVertically(): Boolean = false
            }.apply {
                carouselAlignment = CarouselLayoutManager.ALIGNMENT_CENTER
            }
        binding.carouselRecyclerView.layoutManager = layoutManager

        carouselHomeAdapter = CarouselHomeAdapter(bodyPart, this)
        binding.carouselRecyclerView.adapter = carouselHomeAdapter

        val snapHelper = CarouselSnapHelper()
        snapHelper.attachToRecyclerView(binding.carouselRecyclerView)
    }


    private fun initObservers() {
        homeViewModel.bodyPartExercises.observe(this) {
            drawExercisesInformation(it[it.indices.random()])
        }

        homeViewModel.navigateToIntroduction.observe(this) {
            it.getContentIfNotHandled()?.let {
                goToIntroduction()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.bodyPartState.mapNotNull { it }.collect {
                    if (it.bodyParts.isNotEmpty()) {
                        Log.e("BODYPARTResponse", it.toString())
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.viewState.collect {
                    when (it) {
                        HomeViewState.Error -> showError()
                        HomeViewState.Loading -> showLoading.create(this@HomeActivity)
                        null -> showLoading.dismiss()
                    }
                }
            }
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showError() {
        BasicDialog.create(
            this,
            resources.getDrawable(R.drawable.dialog_bg, this.theme),
            resources.getString(R.string.title_default_error),
            resources.getString(R.string.supporting_text_default_error),
            resources.getString(R.string.accept_default_error)
        ) {
            it.dismiss()
        }
    }

    private fun drawExercisesInformation(bodyPartExerciseResponse: BodyPartExerciseResponse) {
        loadGif(bodyPartExerciseResponse)
        with(binding) {
            target.text = bodyPartExerciseResponse.target.capitalizeFirstLetter()
            equipment.text = bodyPartExerciseResponse.equipment.capitalizeFirstLetter()
            name.text = bodyPartExerciseResponse.name.capitalizeFirstLetter()
            secondaryMuscles.text =
                homeViewModel.formatSecondaryMuscles(bodyPartExerciseResponse.secondaryMuscles)
            instructions.text =
                homeViewModel.formatInstructions(bodyPartExerciseResponse.instructions)
        }
    }

    private fun loadGif(bodyPartExerciseResponse: BodyPartExerciseResponse) {
        homeViewModel.setHomeViewState(HomeViewState.Loading)

        Glide.with(this).asGif().load(bodyPartExerciseResponse.gifUrl)
            .listener(object : RequestListener<GifDrawable> {

                override fun onResourceReady(
                    resource: GifDrawable,
                    model: Any,
                    target: Target<GifDrawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    homeViewModel.setHomeViewState(null)
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    homeViewModel.setHomeViewState(HomeViewState.Loading)
                    return false
                }
            })
            .into(binding.imageView)
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
            resources.getDrawable(R.drawable.dialog_bg, this.theme),
            resources.getString(R.string.title_close_session),
            resources.getString(R.string.supporting_text_close_session),
            resources.getString(R.string.accept_close_session)
        ) {
            homeViewModel.logOut()
            it.dismiss()
        }
    }

    override fun itemHero(heroBodyPart: ProviderTypeBodyPart) {
        getBodyPartExerciseList(heroBodyPart)
    }

    private fun getBodyPartExerciseList(bodyPart: ProviderTypeBodyPart) {
        lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.callBodyPartExerciseList(bodyPart)
            }
        }
    }


}