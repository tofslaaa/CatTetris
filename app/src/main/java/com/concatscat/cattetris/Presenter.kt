package com.concatscat.cattetris

class Presenter(private val view: View) {

    fun generateBlocks() {
        val positions: List<Int> = when ((0..4).random()) {
            0 -> BlocksGenerator.generateBlocks("L")
            1 -> BlocksGenerator.generateBlocks("I")
            2 -> BlocksGenerator.generateBlocks("S")
            3 -> BlocksGenerator.generateBlocks("O")
            4 -> BlocksGenerator.generateBlocks("J")
            else -> BlocksGenerator.generateBlocks("L")
        }

        view.setNewBlock(positions)
    }


    interface View {
        fun setNewBlock(positions: List<Int>)
    }
}