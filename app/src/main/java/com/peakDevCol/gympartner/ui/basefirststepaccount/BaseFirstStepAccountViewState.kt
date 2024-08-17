package com.peakDevCol.gympartner.ui.basefirststepaccount

import android.content.Context
import android.graphics.drawable.Drawable

sealed class BaseFirstStepAccountViewState {
    data object Loading : BaseFirstStepAccountViewState()
    data class Error(
        val context: Context,
        val background: Drawable,
        val title: String,
        val msg: String,
        val positiveMsg: String,
    ) : BaseFirstStepAccountViewState()

}



