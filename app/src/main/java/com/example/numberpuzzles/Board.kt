package com.example.numberpuzzles


import kotlin.properties.Delegates
import kotlin.random.Random

class Board(size: Int) {
    private val boardSize:Int = size
    private var numbersOfActions by Delegates.notNull<Int>()
    private var  places:MutableList<Place> = ArrayList<Place>(size * size)
    private var listeners:MutableList<ItemMovements> = ArrayList()
    init {
        for (x in 1..size){
            for (y in 1..size){
                places.add(if (x == size && y == size){
                        Place(x,y,this)
                    }else{
                        Place(x,y,(y-1)* size +x,this)
                    }
                )
            }
        }
        numbersOfActions = 0
    }
    fun rearrange(){
        numbersOfActions = 0
        for (i in 0 until boardSize * boardSize){
            swapItems()
        }
        do {
            swapItems()
        }while (!solvable() || solved())
    }
    private fun  swapItems(){
        val startPlace :Place? = itemPlace(random.nextInt(boardSize)+1,random.nextInt(boardSize)+1)
        val endPlace :Place? = itemPlace(random.nextInt(boardSize)+1,random.nextInt(boardSize)+1)
        if (startPlace != endPlace){
            val item :Item? = startPlace?.item
            startPlace!!.item = endPlace?.item
            endPlace!!.item = item
        }
    }
    private fun solvable():Boolean {
        var inversion = 0
        for(place:Place in places){
            val itemPlace :Item? = place.item
            for (q:Place in places){
                var qt :Item? = q.item
                if (place != q && itemPlace !=null && qt != null
                        && indexOf(place)< indexOf(q)
                        && itemPlace.itemNumber() > qt.itemNumber()
                ){
                    inversion++
                }
            }
        }
        val isEvenSize = boardSize % 2 == 0
        val isEvenInversion = inversion %2 ==0
        var isBlankOnOddRow = blank()!!.y %2 ===1
        isBlankOnOddRow = if (isEvenSize) !isBlankOnOddRow else isBlankOnOddRow
        return !isEvenSize && isEvenInversion || isEvenSize && isBlankOnOddRow == isEvenInversion
    }
    private fun indexOf(p:Place):Int{
        return  (p.y -1)* boardSize + p.x
    }
    fun solved():Boolean{
        var result = true
        for (place:Place in places){
            result = result &&
                    (place.x == boardSize && place.y == boardSize
                            || place.item != null &&
                            place.item!!.itemNumber() == indexOf(place))
        }
        return result
    }
    fun slide(item:Item){
        for (place:Place in places){
            if (place.item === item){
                val toPlace:Place? = blank()
                toPlace!!.item = item
                place.item = null
                numbersOfActions++
                notifyTileSliding(place,toPlace,numbersOfActions)
                return
            }
        }
    }
    fun canSlide(place: Place):Boolean {
        val x:Int = place.x
        val y:Int = place.y
        return  (isBlank(x-1,y)|| isBlank(x+1,y) || isBlank(x,y-1) || isBlank(x,y+1))
    }
    private fun isBlank(x:Int,y:Int):Boolean {
        return ((x in 1..boardSize) && (y in 1..boardSize) && (itemPlace(x,y)!!.item == null))
    }
    private fun blank():Place?{
        for (place:Place in places){
            if (place.item == null){
                return place
            }
        }
        return null
    }
    fun  places():Iterable<Place> = places
    fun itemPlace(x:Int, y:Int):Place?{
        for (place:Place in places){
            if (place.x == x && place.y == y){
                return place
            }
        }
        return null
    }
    fun size():Int = boardSize
    fun itemActionListener(listener:ItemMovements){
        if (!listeners.contains(listener)){
            listeners.add(listener)
        }
    }
    private fun notifyTileSliding(from:Place, to:Place, numOfMove:Int){
        for (listener : ItemMovements in listeners){
            listener.itemAction(from,to,numOfMove)
        }
    }
    companion object{
        private val random:Random = Random
    }
}