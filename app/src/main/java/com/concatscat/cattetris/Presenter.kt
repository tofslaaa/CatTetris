package com.concatscat.cattetris

class Presenter(private val view: View) {

    private var grid = mutableListOf<Int>()
    private var lastCurrentBlock = mutableListOf<Int>()
    private var currentBlock = mutableListOf<Int>()

    private var isCurrentBlockDown = false

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
        if (grid.isEmpty()) {
            grid = positions
        }
    }

    fun updateBlockPosition(positions: MutableList<Int>, isFirstBlockUpdate: Boolean) {
        val newPositions: MutableList<Int>

        if (isFirstBlockUpdate) {
            newPositions = setNewBlockPosition(positions)
            lastCurrentBlock.clear()
        } else newPositions = positions

        view.updateCurrentBlockPositions(newPositions)

        currentBlock.clear()
        if (lastCurrentBlock.isNotEmpty()) {
            checkCurrentBlockPosition(newPositions)
            lastCurrentBlock = newPositions
        } else {
            currentBlock = newPositions
            lastCurrentBlock = newPositions
        }

        updateGrid(currentBlock)
        view.updateGrid(grid)

        if (checkBlockCollide()) {
            view.generateNewBlock()
        }

        val index = checkGridLineCompletion()
        if (index != 0) {
            updateGridAfterCompletion(index)
        }
    }

    private fun setNewBlockPosition(positions: MutableList<Int>):
            MutableList<Int> {
        var newPositions: MutableList<Int> = (0..95).map { 0 }.toList().toMutableList()

        var countOfNotNull = 0
        for (i in positions.size - 1..positions.size - 9) {
            if (positions[i] != 0) {
                countOfNotNull++
            }
        }

        if (countOfNotNull == 0) {
            for (i in positions.indices) {
                if (positions[i] != 0) {
                    newPositions[i + 8] = positions[i]

                }
            }
        } else {
            newPositions = positions
        }


        return newPositions
    }

    private fun checkCurrentBlockPosition(newPositions: MutableList<Int>) {
        //check block is on the floor
        var countOfNull = 0
        for (i in newPositions.size - 1..newPositions.size - 9) {
            if (newPositions[i] != 0) {
                countOfNull++
            }
        }

        if (countOfNull != 0) {
            currentBlock = newPositions
            isCurrentBlockDown = true
            return
        }

        //fill grid with zeroes
        for (i in newPositions.indices) {
            if (newPositions[i - 8] == 0 && lastCurrentBlock[i - 8] != 0) {
                currentBlock[i - 8] = 5
            } else {
                currentBlock[i] = newPositions[i]
            }
        }
    }

    private fun updateGrid(currentBlock: MutableList<Int>) {
        for (i in grid.indices) {
            if (grid[i] == 0 && currentBlock[i] != 0) {
                if (currentBlock[i] == 5) {
                    grid[i] = 0
                } else {
                    grid[i] = currentBlock[i]
                }
            }
        }
    }

    private fun checkBlockCollide(): Boolean {
        return if (isCurrentBlockDown) {
            isCurrentBlockDown = false
            true
        } else {
            for (i in currentBlock.indices) {
                if (currentBlock[i] != 0 && currentBlock[i] != 5) {
                    if (currentBlock[i + 8] != currentBlock[i] && currentBlock[i + 8] != 0) {
                        true
                    }
                }
            }
            false
        }
    }

    private fun checkGridLineCompletion(): Int {
        var countOfNotNull = 0

        for (i in grid) {
            if (grid[i] != 0) countOfNotNull++
            if (i % 7 == 0) {
                if (countOfNotNull == 8) {
                    return i
                } else {
                    countOfNotNull = 0
                }
            }
        }

        return 0
    }

    private fun updateGridAfterCompletion(index: Int) {
        for (i in grid[index - 8]..grid[8]) {
            grid[i + 8] = grid[i]
        }

        for (i in grid[7]..grid[0]) {
            if (grid[i] != 0) grid[i + 8] = grid[i]
        }
    }


    interface View {
        fun updateGrid(grid: MutableList<Int>)
        fun updateCurrentBlockPositions(positions: MutableList<Int>)
        fun generateNewBlock()
    }
}