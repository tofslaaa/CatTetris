package com.concatscat.cattetris

data class BlockModel (val type: Type = Type.NONE) {

    enum class Type(val type: Int) {
        NONE(0),
        GUN(1),
        LINE(2),
        SQUARE(3),
        SNAKE(4),
        STAIRS(5),
    }
}