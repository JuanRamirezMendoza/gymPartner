package com.peakDevCol.gympartner.core.ex

fun String.capitalizeFirstLetter(): String {
    return this.substring(0, 1).uppercase() + this.substring(1)
}