package com.natiqhaciyef.common.model.ui

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.natiqhaciyef.common.R

enum class Color(
    @DrawableRes
    val gradient: Int
) {
    WHITE(R.drawable.color_gray_gradient),
    BLACK(R.drawable.color_gray_gradient),
    GRAY(R.drawable.color_gray_gradient),
    RED(R.drawable.color_red_gradient),
    BLUE(R.drawable.color_blue_gradient),
    GREEN(R.drawable.color_green_gradient),
    ORANGE(R.drawable.color_orange_gradient),
    YELLOW(R.drawable.color_yellow_gradient),
    PURPLE(R.drawable.color_purple_gradient),
    PINK(R.drawable.color_pink_gradient),
    TEAL(R.drawable.color_teal_gradient),
    BROWN(R.drawable.color_brown_gradient);

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
    }
}