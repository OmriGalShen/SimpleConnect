package com.example.myapplication

class Board(rows:Int, cols:Int) {
    val board_arr= Array(rows){Array<Player>(cols){ Player("","") }}

    private fun pokerToPlayer(pokerNum: Int):Player{
        var row = 0
        when(pokerNum%board_arr.size == 0){
            true -> row = Math.max(pokerNum/board_arr.size-1,0)
            false -> row = Math.max(pokerNum/board_arr.size,0)
        }
        val col= pokerNum%board_arr[0].size
        return board_arr[row][col]
    }
    fun isFree(pokerNum: Int):Boolean{
        return pokerToPlayer(pokerNum).name==""
    }
    fun insert(pokerNum: Int,player: Player){
        pokerToPlayer(pokerNum).name=player.name
        pokerToPlayer(pokerNum).color=player.color
    }

}