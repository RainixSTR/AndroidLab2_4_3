package com.rainixstr.androidlab2_4_3

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private var secondsElapsed: Int = 0
    private lateinit var textSecondsElapsed: TextView
    private var onScreen = true
    private lateinit var sharedPref: SharedPreferences

    private var backgroundThread = Thread {
        while (true) {
            if (onScreen) {
                Thread.sleep(1000)
                textSecondsElapsed.post {
                    textSecondsElapsed.text = getString(R.string.seconds_elapsed, secondsElapsed++)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed)
        sharedPref = getSharedPreferences(SECONDS_ELAPSED, Context.MODE_PRIVATE)
        backgroundThread.start()
    }

    override fun onStart() {
        super.onStart()
        secondsElapsed = sharedPref.getInt(SECONDS_ELAPSED, 0)
    }

    override fun onResume() {
        super.onResume()
        onScreen = true
    }

    override fun onPause() {
        super.onPause()
        onScreen = false
    }

    override fun onStop() {
        super.onStop()
        sharedPref.edit().run {
            putInt(SECONDS_ELAPSED, secondsElapsed)
            apply()
        }
    }

    companion object {
        const val SECONDS_ELAPSED = "Seconds elapsed"
    }
}