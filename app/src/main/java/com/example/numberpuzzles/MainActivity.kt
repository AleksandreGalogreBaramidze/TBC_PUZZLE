package com.example.numberpuzzles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.numberpuzzles.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private  var board:Board?=null
    private  var boardView:BoardView?=null
    private var boardSize = 3
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        init()
    }
    private fun init() {
        board = Board(boardSize)
        board!!.itemActionListener(itemMovements)
        board!!.rearrange()
        binding.mainView.removeView(boardView)
        boardView = BoardView(this,board!!)
        binding.mainView.addView(boardView)
        binding.moves.text = "Moves : 0"
    }
    private val itemMovements :ItemMovements = object :ItemMovements{
        override fun itemAction(from: Place?, to: Place?, numOfMoves: Int) {
            binding.moves.text = "Moves : $numOfMoves"
        }
    }
}