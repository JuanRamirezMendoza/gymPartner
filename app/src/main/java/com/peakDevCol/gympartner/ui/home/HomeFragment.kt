package com.peakDevCol.gympartner.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
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
import com.peakDevCol.gympartner.databinding.FragmentHomeBinding
import com.peakDevCol.gympartner.domain.ProviderTypeBodyPart
import com.peakDevCol.gympartner.ui.introduction.IntroductionActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), OnItemHero {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var carouselHomeAdapter: CarouselHomeAdapter
    private val homeViewModel: HomeViewModel by viewModels()

    private val showLoading = LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.target.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }
        initUi()
        return binding.root
    }

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

    private fun initListeners() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.perfil -> {
                    Log.e("PRUEBA SEBASTIAN   ","entró perfil tap")
                    findNavController().navigate(R.id.profileFragment)
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

    private fun initUi() {
        initCallServices()
        initListeners()
        setUpRecyclerView()
        initObservers()
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
        homeViewModel.bodyPartExercises.observe(requireActivity()) {
            drawExercisesInformation(it[it.indices.random()])
        }

        homeViewModel.navigateToIntroduction.observe(requireActivity()) {
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
                        HomeViewState.Loading -> showLoading.create(requireContext())
                        null -> showLoading.dismiss()
                    }
                }
            }
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showError() {
        BasicDialog.create(
            requireContext(),
            resources.getDrawable(R.drawable.dialog_bg),
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
        startActivity(IntroductionActivity.create(requireContext()))
        requireActivity().finish()
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showExitDialog() {
        BasicDialog.create(
            requireContext(),
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