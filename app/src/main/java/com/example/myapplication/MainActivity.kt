package com.example.myapplication

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.media.MediaPlayer

class MainActivity : AppCompatActivity() {
    //init players
    var redPlayer: Player = Player("Red", Color.parseColor("#FF4136"), R.drawable.red)
    var yellowPlayer: Player = Player("Yellow", Color.parseColor("#FFDC00"), R.drawable.yellow)
    var players = arrayOf(redPlayer, yellowPlayer) // array containing all players
    // initialize variables
    var activePlayer: Int = 0 // index of active player in player array
    private val gridHeight = 3
    private val gridWidth = 3
    var gameBoard = Board(gridHeight, gridWidth) //initialize game board
    var gameOn: Boolean = true
    var mplayer: MediaPlayer = MediaPlayer() // used to play sounds
    var originalTextColor:Int = 0 //save original color of game message in order to return to it after color change
    //
    /**
     * Initialize all game variables and make images
     */
    private fun initGame() {
        gameOn = true
        activePlayer = 0
        displayMessage(players[activePlayer].turnMessage(),originalTextColor)
        gameBoard = Board(gridHeight, gridWidth) //initialize game board
        //images are empty
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

    /**
     * Change current player to next player in players array
     */
    private fun passPlayerTurn() {
        activePlayer++
        if (activePlayer >= players.size) activePlayer = 0
    }

    /**
     * Get poker number from image view
     */
    private fun imageToPokerNum(image: ImageView): Int {
        val defaultValue: Int = -1
        return image.tag.toString().toIntOrNull() ?: defaultValue
    }

    /**
     * Player clicked on game grid
     * This is the main game function
     */
    private fun dropPoker(view: View) {
        val image: ImageView = findViewById(view.id)
        val pokerNum = imageToPokerNum(image)
        val player = players[activePlayer] //current player
        if (gameOn) {
            if (gameBoard.isFree(pokerNum))
            {
                //drop animation of poker chip
                image.translationY = -1000f
                image.animate().translationY(0f).duration = 800
                //
                image.setImageResource(player.image) //change to player image
                gameBoard.insert(pokerNum, player) // add player to game board
                if (gameBoard.checkVictory(player))  //player won
                {
                    victoryState(player)
                    return
                }
                passPlayerTurn()
                displayMessage(players[activePlayer].turnMessage())
            }
            if(gameBoard.isFull()) {// Game is tie!
                drawState()
                return
            }
            if (isSound()) //makes turn sound
            {
                mplayer = MediaPlayer.create(this, R.raw.ting_sound)
                mplayer.start()
            }
        }
    }

    /**
     * Game is tie
     */
    private fun drawState() {
        val message = "Draw!"
//        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
        displayMessage(message,Color.BLACK)
        gameOn = false
        if (isSound()) {
            mplayer = MediaPlayer.create(this, R.raw.draw_sound)
            mplayer.start()
        }
    }

    /**
     * Game is won by given player
     */
    private fun victoryState(player: Player) {
//        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
        displayMessage(player.winMessage(),player.color)
        gameOn = false
        if (isSound()) {
            mplayer = MediaPlayer.create(this, R.raw.win_sound)
            mplayer.start()
        }
    }

    /**
     * Player clicked on reset button
     */
    private fun resetClicked(view: View) {
        initGame()
    }

    private fun displayMessage(message: String) {
        val view:TextView = findViewById<TextView>(R.id.messageView)
        view.text= message
    }

    private fun displayMessage(message: String, color: Int) {
        val view:TextView = findViewById<TextView>(R.id.messageView)
        view.text=message
        view.setTextColor(color)
    }

    /**
     * Check if sound is enabled
     */
    private fun isSound(): Boolean {
        return findViewById<ToggleButton>(R.id.soundToggle).isChecked
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        originalTextColor = findViewById<TextView>(R.id.messageView).currentTextColor
        initGame()
    }
}
