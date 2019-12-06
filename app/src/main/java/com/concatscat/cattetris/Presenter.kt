package com.concatscat.cattetris

class Presenter(private val view: View) {

    fun generateBlock(callback: (positions: MutableList<Int>) -> Unit) {
        val positions: MutableList<Int> = when ((0..4).random()) {
            0 -> BlocksGenerator.generateBlocks("L")
            1 -> BlocksGenerator.generateBlocks("I")
            2 -> BlocksGenerator.generateBlocks("S")
            3 -> BlocksGenerator.generateBlocks("O")
            4 -> BlocksGenerator.generateBlocks("J")
            else -> BlocksGenerator.generateBlocks("L")
        }

        callback(positions)

        updateBlockPosition(positions)
    }

    fun updateBlockPosition(positions: MutableList<Int>){
        var newPositions: MutableList<Int> = (0..95).map { 0 }.toList().toMutableList()

        var countOfNotNull = 0
        for (i in positions.size-1..positions.size-9){
            if (positions[i] != 0){
                countOfNotNull++
            }
        }

        if (countOfNotNull==0){
            for (i in positions.indices){
                if (positions[i]!= 0){
                    newPositions[i+8] = positions[i]

                }
            }
        } else{
            newPositions = positions
        }

        view.updateBlockPositions(newPositions)
    }

    interface View {
        fun updateBlockPositions(positions: MutableList<Int>)
    }
}