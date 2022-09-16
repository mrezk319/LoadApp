package com.udacity
sealed class ButtonStates {
    object Clicked : ButtonStates()
    object Loading : ButtonStates()
    object Completed : ButtonStates()
}