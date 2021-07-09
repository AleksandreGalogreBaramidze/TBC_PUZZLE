package com.example.numberpuzzles

class Place(val x:Int, var y:Int, board: Board) {
    var item:Item? = null
    private val board:Board = board
    constructor(x:Int, y:Int,number:Int, board:Board):this(x,y,board){
        item = Item(number)
    }
    fun haveItem():Boolean{
        return item != null
    }
    fun canSlide():Boolean{
        return haveItem() && board.canSlide(this)
    }
    fun onSlide(){
        board.slide(item!!)
    }
}