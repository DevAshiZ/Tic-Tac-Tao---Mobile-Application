package com.example.xoxgame

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.xoxgame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private val combinationList: MutableList<IntArray> = ArrayList()
    private var boxPositions = IntArray(9) { 0 } // Initialize with 9 zeros
    private var playerTurn = 1
    private var totalSelectedBoxes = 1

    private var playerOneScore = 0
    private var playerTwoScore = 0
    private val roundsToPlay = 3
    private var currentPlayerWins = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // List of winning combinations
        combinationList.apply {
            add(intArrayOf(0, 1, 2))
            add(intArrayOf(3, 4, 5))
            add(intArrayOf(6, 7, 8))
            add(intArrayOf(0, 3, 6))
            add(intArrayOf(1, 4, 7))
            add(intArrayOf(2, 5, 8))
            add(intArrayOf(2, 4, 6))
            add(intArrayOf(0, 4, 8))
        }

        // Get player names from intent
        val getPlayerOneName = intent.getStringExtra("playerOne")
        val getPlayerTwoName = intent.getStringExtra("playerTwo")

        // Set player names in the UI
        binding?.playerOneName?.text = getPlayerOneName
        binding?.playerTwoName?.text = getPlayerTwoName

        // Set up click listeners for each image view
        setupClickListener(binding?.image1, 0)
        setupClickListener(binding?.image2, 1)
        setupClickListener(binding?.image3, 2)
        setupClickListener(binding?.image4, 3)
        setupClickListener(binding?.image5, 4)
        setupClickListener(binding?.image6, 5)
        setupClickListener(binding?.image7, 6)
        setupClickListener(binding?.image8, 7)
        setupClickListener(binding?.image9, 8)

        updateScores()

    }

    //update scores of the players
    @SuppressLint("SetTextI18n")
    private fun updateScores() {
        binding?.playerOneScore?.text = "Score: $playerOneScore"
        binding?.playerTwoScore?.text = "Score: $playerTwoScore"
    }

    private fun setupClickListener(imageView: ImageView?, position: Int) {
        imageView?.setOnClickListener {
            if (isBoxSelectable(position)) {
                performAction(imageView, position)
            }
        }
    }

    private fun performAction(imageView: ImageView, selectedBoxPosition: Int) {
        boxPositions[selectedBoxPosition] = playerTurn
        imageView.setImageResource(if (playerTurn == 1) R.drawable.ximage else R.drawable.oimage)

        //score - add scores to the players if they won a round
        if (checkResults()) {
            currentPlayerWins++

            if (playerTurn == 1) {
                playerOneScore++
            } else {
                playerTwoScore++
            }
            updateScores()

            val winnerName = if (playerTurn == 1) binding?.playerOneName?.text.toString() else binding?.playerTwoName?.text.toString()
            showResultDialog("$winnerName is a Winner!")
            checkRoundResult()
        } else if (totalSelectedBoxes == 9) {
            showResultDialog("Match Draw")
        checkRoundResult()
        } else {
            changePlayerTurn()
            totalSelectedBoxes++
        }
    }

    private fun checkRoundResult() {

        //checking if the score is greater or equal to 2 in each round
        if (playerOneScore >= 2) {
            val winnerActivityIntent = Intent(this, WonPlayerActivity::class.java)
            val winnerName = if (playerTurn == 1) binding?.playerOneName?.text.toString() else binding?.playerTwoName?.text.toString()
            winnerActivityIntent.putExtra("winnerName", winnerName)
            winnerActivityIntent.putExtra("WinnerScore", playerOneScore)
            startActivity(winnerActivityIntent)
            finish()
        }
        if (playerTwoScore >= 2) {
            val winnerActivityIntent = Intent(this, WonPlayerActivity::class.java)
            val winnerName = if (playerTurn == 1) binding?.playerOneName?.text.toString() else binding?.playerTwoName?.text.toString()
            winnerActivityIntent.putExtra("winnerName", winnerName)
            winnerActivityIntent.putExtra("Winner score", playerTwoScore)
            startActivity(winnerActivityIntent)
            finish()
        } else {
            restartMatch()
        }
    }

    private fun changePlayerTurn() {
        playerTurn = if (playerTurn == 1) 2 else 1
        binding?.playerOneLayout?.setBackgroundResource(if (playerTurn == 1) R.drawable.black_border else R.drawable.white_box)
        binding?.playerTwoLayout?.setBackgroundResource(if (playerTurn == 2) R.drawable.black_border else R.drawable.white_box)
    }

    private fun checkResults(): Boolean {
        for (combination in combinationList) {
            if (boxPositions[combination[0]] == playerTurn && boxPositions[combination[1]] == playerTurn && boxPositions[combination[2]] == playerTurn) {
                return true
            }
        }
        return false
    }

    private fun isBoxSelectable(boxPosition: Int): Boolean {
        return boxPositions[boxPosition] == 0
    }

    private fun showResultDialog(message: String) {
        ResultDialog(this, message) {
            restartMatch()
        }.show()
    }

    private fun restartMatch() {
        boxPositions = IntArray(9) { 0 }
        playerTurn = 1
        totalSelectedBoxes = 1

        binding?.image1?.setImageResource(R.drawable.white_box)
        binding?.image2?.setImageResource(R.drawable.white_box)
        binding?.image3?.setImageResource(R.drawable.white_box)
        binding?.image4?.setImageResource(R.drawable.white_box)
        binding?.image5?.setImageResource(R.drawable.white_box)
        binding?.image6?.setImageResource(R.drawable.white_box)
        binding?.image7?.setImageResource(R.drawable.white_box)
        binding?.image8?.setImageResource(R.drawable.white_box)
        binding?.image9?.setImageResource(R.drawable.white_box)

        //if the game almost over(last round)
        if (currentPlayerWins == roundsToPlay) {
            // Reset round wins
            currentPlayerWins = 0

            // Increment player's total wins
            if (playerTurn == 1) {
                playerOneScore++
            } else {
                playerTwoScore++
            }
            updateScores()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
