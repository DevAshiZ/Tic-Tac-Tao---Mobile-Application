package com.example.xoxgame
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView


class ResultDialog(
    context: Context,
    private val message: String,
    private val restartListener: () -> Unit // Callback to restart the game
) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_dialog)

        val messageText = findViewById<TextView>(R.id.messageText)
        val startAgainButton = findViewById<Button>(R.id.startAgainButton)

        messageText.text = message

        startAgainButton.setOnClickListener {
            restartListener.invoke() // Call the callback to restart the game
            dismiss()
        }
    }
}
