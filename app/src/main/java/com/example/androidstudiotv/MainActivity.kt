package com.example.androidstudiotv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.Button
import android.widget.TextView
import android.os.CountDownTimer
import kotlin.math.round
import android.app.AlertDialog
import android.media.MediaPlayer

class MainActivity : AppCompatActivity() {
    private lateinit var inputTime: EditText
    private lateinit var startTimerButton: Button
    private var countDownTimer: CountDownTimer? = null
    private lateinit var timerTextView: TextView
    private lateinit var secondText: EditText
    private lateinit var minuteText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startTimerButton = findViewById(R.id.startTimerButton)
        minuteText = findViewById(R.id.inputMinutesText)
        secondText = findViewById(R.id.inputSecondsText)
        timerTextView = findViewById(R.id.timerTextView)

        startTimerButton.setOnClickListener {
            var minutes = 0L
            if(minuteText.text.toString().isNotEmpty()) {
                minutes = minuteText.text.toString().toLong()
            }
            var seconds = 0L
            if(secondText.text.toString().isNotEmpty()) {
                seconds = secondText.text.toString().toLong()
            }
            val timeInSeconds = (minutes * 60) + seconds
            startTimer(timeInSeconds * 1000) // Convert to milliseconds
        }
    }

    private fun startTimer(timeInMillis: Long) {
        countDownTimer?.cancel() // Cancel any existing timer
        countDownTimer = object : CountDownTimer(timeInMillis, 1) {
            override fun onTick(millisUntilFinished: Long) {
                // Convert to seconds
                val timeInSeconds = round(millisUntilFinished / 1000.0).toInt()
                // Display time in MM:SS format
                timerTextView.text = String.format("%02d:%02d", timeInSeconds / 60, timeInSeconds % 60)
            }

            override fun onFinish() {
                timerTextView.text = "Done!"
                playSoundEffect()
                showTimerFinishedAlert()
            }
        }.start()
    }

    private fun showTimerFinishedAlert() {
        val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
        alertDialogBuilder.setTitle("Timer Finished")
        alertDialogBuilder.setMessage("The countdown timer has finished.")
        alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            // Do nothing.
        }
        // Optionally, add a negative button or other responses.
        alertDialogBuilder.setCancelable(false) // Makes the dialog non-cancelable, requiring a user response.
        alertDialogBuilder.show()
    }

    private fun playSoundEffect() {
        val mediaPlayer: MediaPlayer = MediaPlayer.create(this@MainActivity, R.raw.timer_finished)
        mediaPlayer.setOnCompletionListener { mp -> mp.release() } // Release the MediaPlayer resource on completion
        mediaPlayer.start()
    }
}