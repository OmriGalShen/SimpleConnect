package com.example.myapplication

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.media.MediaPlayer

class MainActivity : AppCompatActivity() {
    // 0 = red , 1= yellow
    var activePlayer:Int = 0
    var turnCount:Int = 1
    val board = Array(3, {IntArray(3)})
    var gameOn:Boolean = true
    var mplayer:MediaPlayer= MediaPlayer()
    //

    private fun initGame(){
        gameOn=true
        displayMessage("Red Turn")
        turnCount=1
        activePlayer=0
        for(i in 0..2){
            for(j in 0..2){
                board[i][j]=-1
            }
        }
        findViewById<ImageView>(R.id.poker1).setImageResource(0)
        findViewById<ImageView>(R.id.poker2).setImageResource(0)
        findViewById<ImageView>(R.id.poker3).setImageResource(0)
        findViewById<ImageView>(R.id.poker4).setImageResource(0)
        findViewById<ImageView>(R.id.poker5).setImageResource(0)
        findViewById<ImageView>(R.id.poker6).setImageResource(0)
        findViewById<ImageView>(R.id.poker7).setImageResource(0)
        findViewById<ImageView>(R.id.poker8).setImageResource(0)
        findViewById<ImageView>(R.id.poker9).setImageResource(0)
    }

    private fun imageToPokerNum(image:ImageView): Int{
        val defaultValue : Int = -1
        return image.tag.toString().toIntOrNull()?:defaultValue
    }

    private fun checkBoardSpace(pokerNum:Int):Boolean{
        when(pokerNum){
            1->return board[0][0]==-1
            2->return board[0][1]==-1
            3->return board[0][2]==-1
            4->return board[1][0]==-1
            5->return board[1][1]==-1
            6->return board[1][2]==-1
            7->return board[2][0]==-1
            8->return board[2][1]==-1
            9->return board[2][2]==-1
        }
        return false
    }

    private fun insertToBoard(pokerNum:Int,player:Int){
        when(pokerNum){
            1->board[0][0] = player
            2->board[0][1] = player
            3->board[0][2] = player
            4->board[1][0] = player
            5->board[1][1] = player
            6->board[1][2] = player
            7->board[2][0] = player
            8->board[2][1] = player
            9->board[2][2] = player
        }
    }

    private fun checkVictory(player:Int):Boolean
    {
        //check all rows and columns
        for(i in 0..2){
            if(board[i][0]==player && board[i][1]==player && board[i][2]==player)
                return true
            if(board[0][i]==player && board[1][i]==player && board[2][i]==player)
                return true
        }
        //check left to right diagonal
        if(board[0][0]==player && board[1][1]==player && board[2][2]==player)
            return true
        //check right to left diagonal
        if(board[0][2]==player && board[1][1]==player && board[2][0]==player)
            return true
        return false
    }

    fun dropPoker(view: View){
        val image:ImageView = findViewById(view.id)
        val pokerNum = imageToPokerNum(image)
        if(gameOn) {
            if (turnCount <= 9 && checkBoardSpace(pokerNum)) {
                //drop animation of poker chip
                image.translationY = -1000f
                image.animate().translationY(0f).duration = 800
                //
                if (activePlayer == 0)//red player turn
                {
                    Log.i("Info", "player status: red turn")
                    image.setImageResource(R.drawable.red)//insert yellow chip to board
                    insertToBoard(pokerNum, activePlayer) // insert play to board array
                    if (checkVictory(activePlayer)) {// red player won
                        victoryState(activePlayer)
                        return
                    } else {
                        activePlayer = 1 // next play active player is set to yellow
                        displayMessage("Yellow Turn")
                    }
                } else // yellow player turn
                {
                    Log.i("Info", "player status: yellow turn")
                    image.setImageResource(R.drawable.yellow)//insert yellow chip to board
                    insertToBoard(pokerNum, activePlayer)// insert play to board array
                    if (checkVictory(activePlayer)) { // yellow player won
                        victoryState(activePlayer)
                        return
                    } else {
                        activePlayer = 0 // next play active player is set to red
                        displayMessage("Red Turn")
                    }
                }
                if(isSound()) {
                    mplayer = MediaPlayer.create(this,R.raw.ting_sound)
                    mplayer.start()
                }
                turnCount++
                if (turnCount > 9)
                    drawState()
            }
            Log.i("Info", "turn status:" + turnCount)
        }
    }

    private fun drawState(){
        val message = "Draw!"
//        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
        displayMessage(message)
        gameOn=false
        if(isSound()) {
            mplayer = MediaPlayer.create(this,R.raw.draw_sound)
            mplayer.start()
        }
    }

    private fun victoryState(player:Int){
        var playerStr:String = "Red"
        if(player!=0){
            playerStr="Yellow"
        }
        val message = playerStr+" Won!"
//        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
        displayMessage(message)
        gameOn=false
        if(isSound()) {
            mplayer = MediaPlayer.create(this,R.raw.win_sound)
            mplayer.start()
        }
    }

    fun resetClicked(view:View){
        initGame()
    }

    private fun displayMessage(message:String){
        findViewById<TextView>(R.id.messageView).text = message
    }
//    private fun displayMessage(message:String, color: String){
//        findViewById<TextView>(R.id.messageView).text = message
//        findViewById<TextView>(R.id.messageView).setTextColor(Color.parseColor(color))
//    }
    private fun isSound():Boolean{
        return findViewById<ToggleButton>(R.id.soundToggle).isChecked
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initGame()
    }
}
