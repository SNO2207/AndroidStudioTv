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
    private lateinit var stopTimerButton: Button
    private var countDownTimer: CountDownTimer? = null
    private lateinit var timerTextView: TextView
    private lateinit var secondText: EditText
    private lateinit var minuteText: EditText
    private var timeInMillis: Long = 0
    private var storedTimeInMillis: Long = 0
    private var timeInSeconds: Long = 0
    private var isPaused: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startTimerButton = findViewById(R.id.startTimerButton)
        minuteText = findViewById(R.id.inputMinutesText)
        secondText = findViewById(R.id.inputSecondsText)
        timerTextView = findViewById(R.id.timerTextView)

        startTimerButton.setOnClickListener {
            if(timeInMillis != 0L) {
                if(isPaused) {
                    resumeTimer()
                    isPaused = false
                    startTimerButton.text = "Pause"
                    return@setOnClickListener
                }
                else {
                    pauseTimer()
                    isPaused = true
                    startTimerButton.text = "Resume"
                    return@setOnClickListener
                }
            }
            var minutes = 0L
            if(minuteText.text.toString().isNotEmpty() && minuteText.text.toString().length <= 7) {
                if(checkNumberInput(minuteText.text.toString()))
                    minutes = minuteText.text.toString().toLong()
                else {
                    inputErrorAlert()
                    return@setOnClickListener
                }
            }
            var seconds = 0L
            if(secondText.text.toString().isNotEmpty() && secondText.text.toString().length <= 7) {
                if(checkNumberInput(secondText.text.toString()))
                    seconds = secondText.text.toString().toLong()
                else {
                    inputErrorAlert()
                    return@setOnClickListener
                }
            }
            if(minutes == 0L && seconds == 0L) {
                inputErrorAlert()
                return@setOnClickListener
            }
            val timeInSeconds = (minutes * 60) + seconds
            timeInMillis = timeInSeconds * 1000
            startTimer(timeInMillis) // Convert to milliseconds
            startTimerButton.text = "Pause"
        }

        stopTimerButton = findViewById(R.id.stopTimerButton)
        stopTimerButton.setOnClickListener {
            stopTimer()
            startTimerButton.text = "Start Timer"
        }
    }

    private fun startTimer(timeInMillis: Long) {
        countDownTimer?.cancel() // Cancel any existing timer
        countDownTimer = object : CountDownTimer(timeInMillis, 1) {
            override fun onTick(millisUntilFinished: Long) {
                // Convert to seconds
                storedTimeInMillis = millisUntilFinished
                timeInSeconds = round(millisUntilFinished / 1000.0).toLong()
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

    private fun stopTimer() {
        countDownTimer?.cancel()
        timeInMillis = 0
        timerTextView.text = "Timer Stopped!"
    }

    private fun pauseTimer() {
        countDownTimer?.cancel()
    }

    private fun resumeTimer() {
        startTimer(storedTimeInMillis)
    }

    private fun inputErrorAlert() {
        val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
        alertDialogBuilder.setTitle("Input Error")
        alertDialogBuilder.setMessage("Please enter a valid number.")
        alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            // Do nothing.
        }
        // Optionally, add a negative button or other responses.
        alertDialogBuilder.setCancelable(false) // Makes the dialog non-cancelable, requiring a user response.
        alertDialogBuilder.show()
    }

    private fun checkNumberInput(input: String): Boolean {
        return input.all { it.isDigit() }
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