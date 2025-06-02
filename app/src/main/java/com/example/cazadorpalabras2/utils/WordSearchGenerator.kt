package com.example.cazadorpalabras2.utils

import kotlin.random.Random

enum class Direction {
    HORIZONTAL,
    VERTICAL,
    DIAGONAL_DOWN_RIGHT,
    DIAGONAL_UP_RIGHT
}

data class Placement(val row: Int, val col: Int, val direction: Direction)

// Generador de sopa de letras
object WordSearchGenerator {

    fun generate(
        width: Int,
        height: Int,
        words: List<String>
    ): Pair<Array<CharArray>, List<String>> {
        val board = Array(height) { CharArray(width) { '_' } }
        val placedWords = mutableListOf<String>()

        for (word in words) {
            val uppercaseWord = word.uppercase().filter { it in 'A'..'Z' }
            if (placeWordOnBoard(board, uppercaseWord)) {
                placedWords.add(uppercaseWord)
            }
        }

        for (i in 0 until height) {
            for (j in 0 until width) {
                if (board[i][j] == '_') {
                    board[i][j] = ('A' + Random.nextInt(26))
                }
            }
        }

        return board to placedWords
    }

    private fun placeWordOnBoard(board: Array<CharArray>, word: String): Boolean {
        val height = board.size
        val width = board[0].size
        val dirs = Direction.values()
        repeat(100) {
            val direction = dirs.random()

            val maxRow = when (direction) {
                Direction.HORIZONTAL -> height
                Direction.VERTICAL -> height - word.length + 1
                Direction.DIAGONAL_DOWN_RIGHT -> height - word.length + 1
                Direction.DIAGONAL_UP_RIGHT -> height - 1
            }
            val maxCol = when (direction) {
                Direction.HORIZONTAL -> width - word.length + 1
                Direction.VERTICAL -> width
                Direction.DIAGONAL_DOWN_RIGHT -> width - word.length + 1
                Direction.DIAGONAL_UP_RIGHT -> width - word.length + 1
            }

            val row = when (direction) {
                Direction.HORIZONTAL -> Random.nextInt(0, maxRow)
                Direction.VERTICAL -> Random.nextInt(0, maxRow)
                Direction.DIAGONAL_DOWN_RIGHT -> Random.nextInt(0, maxRow)
                Direction.DIAGONAL_UP_RIGHT -> Random.nextInt(word.length - 1, height)
            }
            val col = Random.nextInt(0, maxCol)

            if (canPlaceWord(board, word, row, col, direction)) {
                for (i in word.indices) {
                    val r = when (direction) {
                        Direction.HORIZONTAL -> row
                        Direction.VERTICAL -> row + i
                        Direction.DIAGONAL_DOWN_RIGHT -> row + i
                        Direction.DIAGONAL_UP_RIGHT -> row - i
                    }
                    val c = when (direction) {
                        Direction.HORIZONTAL -> col + i
                        Direction.VERTICAL -> col
                        Direction.DIAGONAL_DOWN_RIGHT -> col + i
                        Direction.DIAGONAL_UP_RIGHT -> col + i
                    }
                    board[r][c] = word[i]
                }
                return true
            }
        }
        return false
    }

    private fun canPlaceWord(
        board: Array<CharArray>,
        word: String,
        startRow: Int,
        startCol: Int,
        direction: Direction
    ): Boolean {
        val height = board.size
        val width = board[0].size
        for (i in word.indices) {
            val r = when (direction) {
                Direction.HORIZONTAL -> startRow
                Direction.VERTICAL -> startRow + i
                Direction.DIAGONAL_DOWN_RIGHT -> startRow + i
                Direction.DIAGONAL_UP_RIGHT -> startRow - i
            }
            val c = when (direction) {
                Direction.HORIZONTAL -> startCol + i
                Direction.VERTICAL -> startCol
                Direction.DIAGONAL_DOWN_RIGHT -> startCol + i
                Direction.DIAGONAL_UP_RIGHT -> startCol + i
            }
            if (r !in 0 until height || c !in 0 until width) return false
            if (board[r][c] != '_' && board[r][c] != word[i]) return false
        }
        return true
    }
}

