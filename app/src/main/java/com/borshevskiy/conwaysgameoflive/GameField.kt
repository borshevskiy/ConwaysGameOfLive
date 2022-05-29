package com.borshevskiy.conwaysgameoflive

import kotlin.random.Random

class GameField(private var width: Int, private var height: Int) {

    private var field = arrayOf<Array<Cell>>()

    init {
        for (i in 0 until width) {
            var array = arrayOf<Cell>()
            for (j in 0 until height) {
                array += Cell(i,j,Random.nextBoolean())
            }
            field += array
        }
    }

    fun get(i: Int, j: Int) = field[i][j]

    private fun getCountOfNeighbours(i:Int, j: Int): Int {
        var nb = 0
        for (k in i - 1..i + 1) {
            for (l in j - 1..j + 1) {
                if ((k != i || l != j) && k >= 0 && k < width && l >= 0 && l < height) { if (field[k][l].isAlive) nb++ }
            }
        }
        return nb
    }

    fun generateLifecycle() {
        val liveCells = mutableListOf<Cell>()
        val deadCells = mutableListOf<Cell>()

        for (i in 0 until width) {
            for (j in 0 until height) {
                val cell = field[i][j]
                val nb = getCountOfNeighbours(cell.x, cell.y)
                if(cell.isAlive && (nb < 2 || nb > 3)) { deadCells.add(cell) }
                if((cell.isAlive && (nb == 2 || nb == 3)) || (!cell.isAlive && nb == 3)) { liveCells.add(cell) }
            }
        }
        liveCells.forEach { it.isAlive = true }
        deadCells.forEach { it.isAlive = false }
    }
}