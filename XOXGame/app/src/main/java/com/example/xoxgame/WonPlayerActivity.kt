package com.example.xoxgame

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.xoxgame.databinding.ActivityWonPlayerBinding

class WonPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWonPlayerBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWonPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Retrieve the winner's name from the intent
        val winnerName = intent.getStringExtra("winnerName")
        val winnerScore = intent.getStringExtra("winnerScore") //todo: value should be passed

        // Save the winner's name to SharedPreferences
        saveLastWinner(winnerName)

        val startGameButton: Button = findViewById(R.id.startGameButton)

        startGameButton.setOnClickListener {
            val intent = Intent(this, AddPlayers::class.java)
            startActivity(intent)
        }

        // Display the last two winners' names in separate TextViews
        val lastTwoWinners = getLastTwoWinners()
        if (lastTwoWinners.isNotEmpty()) {
            binding.winnerNameTextView.text = "${lastTwoWinners[1]} Won"
            if (lastTwoWinners.size > 1) {
                val lastTwoWinnersText = lastTwoWinners.joinToString(separator = "\n") { "$it " }
                binding.lastPlayerWon.text = lastTwoWinnersText
            }
        }
    }

    private fun saveLastWinner(winnerName: String?) {
        winnerName?.let {
            val lastTwoWinners = getLastTwoWinners().toMutableList()
            if (lastTwoWinners.size >= 2) {
                lastTwoWinners.removeAt(0)
            }
            lastTwoWinners.add(winnerName)
            val editor = sharedPreferences.edit()
            editor.putString("lastTwoWinners", lastTwoWinners.joinToString(","))
            editor.apply()
        }
    }

    private fun getLastTwoWinners(): List<String> {
        val lastTwoWinnersString = sharedPreferences.getString("lastTwoWinners", "") ?: ""
        return lastTwoWinnersString.split(",").filter { it.isNotBlank() }
    }
}