package com.peakDevCol.gympartner.ui.introduction

sealed class IntroductionViewState {
    data object Loading : IntroductionViewState()
    data object Error : IntroductionViewState()
}