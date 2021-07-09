package com.example.numberpuzzles

class Item(private var number:Int? = null) {
    fun itemNumber():Int{ return number!! }
}