package com.peakDevCol.gympartner.ui.home

sealed class HomeViewState {
    data object Loading : HomeViewState()
    data object Error : HomeViewState()
}
