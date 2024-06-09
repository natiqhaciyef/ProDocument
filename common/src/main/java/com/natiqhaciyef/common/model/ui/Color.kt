package com.natiqhaciyef.common.model.ui

enum class Color {
    WHITE,
    BLACK,
    GRAY,
    RED,
    BLUE,
    GREEN,
    ORANGE,
    PURPLE,
    PINK,
    CYAN,
    BROWN;

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
            CYAN.name, CYAN.name.lowercase() -> { CYAN }
            BROWN.name, BROWN.name.lowercase() -> { BROWN }
            else -> { WHITE }
        }
    }
}