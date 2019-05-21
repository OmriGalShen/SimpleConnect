package com.example.myapplication

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import android.media.MediaPlayer

class MainActivity : AppCompatActivity() {
    //init players
    private var redPlayer: Player = Player("Red", Color.parseColor("#FF4136"), R.drawable.red)
    private var yellowPlayer: Player = Player("Yellow", Color.parseColor("#FFDC00"), R.drawable.yellow)
//    private var bluePlayer: Player = Player("Blue", Color.parseColor("#0074D9"), R.drawable.cat1)
    private var players = arrayOf(redPlayer, yellowPlayer) // array containing all players
    // initialize variable
    var activePlayer: Int = 0 // index of active player in player array
    var gameBoard:Board? = null //initialize game board
    var isGameOn: Boolean = true
    var mplayer: MediaPlayer = MediaPlayer() // used to play sounds
    var originalTextColor:Int = 0 //save original color of game message in order to return to it after color change
    //
    /**
     * Initialize all game variables and make images
     */
    private fun initGame() {
        isGameOn = true
        activePlayer = 0
        displayMessage(players[activePlayer].turnMessage(),originalTextColor)
        val grid:android.support.v7.widget.GridLayout=findViewById(R.id.gameGrid)
        //images are empty
        for(childInx:Int in 0..grid.childCount-1){
            val childView = grid.getChildAt(childInx) as ImageView
            childView.setImageResource(0)
        }
        //initialize game board
        gameBoard = Board(grid.rowCount, grid.columnCount)
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
    fun dropPoker(view: View) {
        val image: ImageView = findViewById(view.id)
        val pokerNum = imageToPokerNum(image)
        val player = players[activePlayer] //current player
        if (isGameOn) {
            if (gameBoard!!.isFree(pokerNum))
            {
                //drop animation of poker chip
                image.translationY = -1000f
                image.animate().translationY(0f).duration = 800
                //
                image.setImageResource(player.image) //change to player image
                gameBoard!!.insert(pokerNum, player) // add player to game board
                if (gameBoard!!.checkVictory(player))  //player won
                {
                    victoryState(player)
                    return
                }
                passPlayerTurn()
                displayMessage(players[activePlayer].turnMessage())
            }
            if(gameBoard!!.isFull()) {// Game is tie!
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
        isGameOn = false
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
        isGameOn = false
        if (isSound()) {
            mplayer = MediaPlayer.create(this, R.raw.win_sound)
            mplayer.start()
        }
    }

    /**
     * Player clicked on reset button
     */
    fun resetClicked(view: View) {
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
