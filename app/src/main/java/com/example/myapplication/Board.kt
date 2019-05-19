package com.example.myapplication

class Board(rows:Int, cols:Int) {
//    val board_arr= Array(rows){Array<Player>(cols){ Player("","") }}
    val board_arr= Array(rows){ arrayOfNulls<Player>(cols)}

    private fun pokerToPlayer(pokerNum: Int):IntArray{
        val row = (pokerNum-1)/board_arr.size
        val col= (pokerNum-1)%board_arr[0].size
        return intArrayOf(row,col)
    }
    fun isFull():Boolean{
        for (i in 0..board_arr.size-1)
            for (j in 0..board_arr[0].size-1)
                if(board_arr[i][j]==null)return false
        return true
    }
    fun isFree(pokerNum: Int):Boolean{
        val position = pokerToPlayer(pokerNum)
        return board_arr[position[0]][position[1]]==null
    }
    fun insert(pokerNum: Int,player: Player){
        val position = pokerToPlayer(pokerNum)
        board_arr[position[0]][position[1]] = player
    }
    fun checkVictory(player: Player):Boolean{
        var count=0
        //check rows
        for (i in 0..board_arr.size-1){
            for (j in 0..board_arr[0].size-1){
                if(board_arr[i][j]!=null&&
                    board_arr[i][j]!!.equals(player))
                    count++
            }
            if(count==board_arr[0].size)return true
            count=0
        }
        //check cols
        count=0
        for (i in 0..board_arr[0].size-1){
            for (j in 0..board_arr.size-1){
                if(board_arr[j][i]!=null&&
                    board_arr[j][i]!!.equals(player))
                    count++
            }
            if(count==board_arr.size)return true
            count=0
        }
        //check left to right diagonal
        count=0
        for (i in 0..board_arr.size-1){
            if(board_arr[i][i]!=null&&
                board_arr[i][i]!!.equals(player))
                count++
        }
        if(count==board_arr.size)return true
        //check right to left diagonal
        count=0
        for (i in 0..board_arr.size-1){
            if(board_arr[i][board_arr[0].size-1-i]!=null&&
                board_arr[i][board_arr[0].size-1-i]!!.equals(player))
                count++
        }
        if(count==board_arr.size)return true
        //no win conditions found
        return false
    }

}