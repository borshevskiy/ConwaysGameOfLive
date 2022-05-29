package com.borshevskiy.conwaysgameoflive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.borshevskiy.conwaysgameoflive.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var clicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {
            startStop.setOnClickListener {
                clicked = if (clicked) {
                    gameOfLife.stop()
                    false
                } else {
                    gameOfLife.start()
                    true
                }
            }
            random.setOnClickListener {
                clicked = false
                gameOfLife.randomize()
            }
        }
    }
}