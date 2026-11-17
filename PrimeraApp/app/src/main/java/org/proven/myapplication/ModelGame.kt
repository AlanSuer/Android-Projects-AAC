package org.proven.myapplication

import java.util.Random

class ModelGame {
    var positionBall = 0
        private set
    var numCubes = 2
        private set

    private val rnd = Random()

    fun startNewGame() {
        numCubes = 2
    }

    fun shuffle() {
        positionBall = rnd.nextInt(numCubes) + 1
    }

    fun checkSelection(selected: Int): Boolean {
        val isCorrect = selected == positionBall
        if (isCorrect) {
            numCubes++
        }
        return isCorrect
    }
}