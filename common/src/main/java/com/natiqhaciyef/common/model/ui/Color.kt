package com.natiqhaciyef.common.model.ui

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.natiqhaciyef.common.R

enum class Color(
    @DrawableRes
    val gradient: Int,
    @ColorRes
    val color: Int
) {
    WHITE(R.drawable.color_gray_gradient, R.color.white),
    BLACK(R.drawable.color_gray_gradient, R.color.black),
    GRAY(R.drawable.color_gray_gradient, R.color.grayscale_500),
    RED(R.drawable.color_red_gradient, R.color.gradient_start_red),
    BLUE(R.drawable.color_blue_gradient, R.color.gradient_start_blue),
    GREEN(R.drawable.color_green_gradient, R.color.gradient_start_green),
    ORANGE(R.drawable.color_orange_gradient, R.color.gradient_start_orange),
    YELLOW(R.drawable.color_yellow_gradient, R.color.gradient_start_yellow),
    PURPLE(R.drawable.color_purple_gradient, R.color.gradient_start_purple),
    PINK(R.drawable.color_pink_gradient, R.color.gradient_start_pink),
    TEAL(R.drawable.color_teal_gradient, R.color.gradient_start_teal),
    BROWN(R.drawable.color_brown_gradient, R.color.gradient_start_brown);

    companion object{
        fun stringToColor(color: String) = when(color.lowercase()){
            WHITE.name, WHITE.name.lowercase() -> { WHITE }
            BLACK.name, BLACK.name.lowercase() -> { BLACK }
            GRAY.name, GRAY.name.lowercase() -> { GRAY }
            RED.name, RED.name.lowercase() -> { RED }
            BLUE.name, BLUE.name.lowercase() -> { BLUE }
            GREEN.name, GREEN.name.lowercase() -> { GREEN }
            ORANGE.name, ORANGE.name.lowercase() -> { ORANGE }
            PURPLE.name, PURPLE.name.lowercase() -> { PURPLE }
            PINK.name, PINK.name.lowercase() -> { PINK }
            YELLOW.name, YELLOW.name.lowercase() -> { YELLOW }
            TEAL.name, TEAL.name.lowercase() -> { TEAL }
            BROWN.name, BROWN.name.lowercase() -> { BROWN }
            else -> { WHITE }
        }

        fun stringToColorRes(color: String) = when(color.lowercase()){
            WHITE.name, WHITE.name.lowercase() -> { WHITE.color }
            BLACK.name, BLACK.name.lowercase() -> { BLACK.color }
            GRAY.name, GRAY.name.lowercase() -> { GRAY.color }
            RED.name, RED.name.lowercase() -> { RED.color }
            BLUE.name, BLUE.name.lowercase() -> { BLUE.color }
            GREEN.name, GREEN.name.lowercase() -> { GREEN.color }
            ORANGE.name, ORANGE.name.lowercase() -> { ORANGE.color }
            PURPLE.name, PURPLE.name.lowercase() -> { PURPLE.color }
            PINK.name, PINK.name.lowercase() -> { PINK.color }
            YELLOW.name, YELLOW.name.lowercase() -> { YELLOW.color }
            TEAL.name, TEAL.name.lowercase() -> { TEAL.color }
            BROWN.name, BROWN.name.lowercase() -> { BROWN.color }
            else -> { WHITE.color }
        }

        fun colorGradientToString(@DrawableRes color: Int) = when(color){
            WHITE.gradient -> { WHITE.name.lowercase() }
            BLACK.gradient -> { BLACK.name.lowercase() }
            GRAY.gradient -> { GRAY.name.lowercase() }
            RED.gradient -> { RED.name.lowercase() }
            BLUE.gradient -> { BLUE.name.lowercase() }
            GREEN.gradient -> { GREEN.name.lowercase() }
            ORANGE.gradient -> { ORANGE.name.lowercase() }
            PURPLE.gradient -> { PURPLE.name.lowercase() }
            PINK.gradient -> { PINK.name.lowercase() }
            YELLOW.gradient -> { YELLOW.name.lowercase() }
            TEAL.gradient -> { TEAL.name.lowercase() }
            BROWN.gradient -> { BROWN.name.lowercase() }
            else -> { WHITE.name.lowercase() }
        }

        fun colorResToString(@DrawableRes color: Int) = when(color){
            WHITE.color -> { WHITE.name.lowercase() }
            BLACK.color -> { BLACK.name.lowercase() }
            GRAY.color -> { GRAY.name.lowercase() }
            RED.color -> { RED.name.lowercase() }
            BLUE.color -> { BLUE.name.lowercase() }
            GREEN.color -> { GREEN.name.lowercase() }
            ORANGE.color -> { ORANGE.name.lowercase() }
            PURPLE.color -> { PURPLE.name.lowercase() }
            PINK.color -> { PINK.name.lowercase() }
            YELLOW.color -> { YELLOW.name.lowercase() }
            TEAL.color -> { TEAL.name.lowercase() }
            BROWN.color -> { BROWN.name.lowercase() }
            else -> { WHITE.name.lowercase() }
        }

    }
}