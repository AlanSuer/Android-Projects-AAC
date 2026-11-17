package org.proven.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import kotlin.random.Random

class GameActivity : ComponentActivity() {

    private lateinit var bStart: Button
    private lateinit var ivCube1: ImageView
    private lateinit var ivCube2: ImageView
    private lateinit var ivCube3: ImageView
    private lateinit var tvResult: TextView

    private var playing = false
    private var positionBall = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        initElements()
        clickElements()
    }

    private fun getPositionBall() {
        positionBall = Random.nextInt(from = 1, until = 4)
    }

    private fun initElements() {
        bStart = findViewById(R.id.bStart)
        ivCube1 = findViewById(R.id.imageViewCube1)
        ivCube2 = findViewById(R.id.imageViewCube2)
        ivCube3 = findViewById(R.id.imageViewCube3)
        tvResult = findViewById(R.id.textView1)

        tvResult.text = ""
    }

    private fun initPlaying() {
        playing = true
        getPositionBall()

        bStart.visibility = View.INVISIBLE
        tvResult.text = "¡Elige un coco!"

        ivCube1.setImageResource(R.drawable.cubilete)
        ivCube2.setImageResource(R.drawable.cubilete)
        ivCube3.setImageResource(R.drawable.cubilete)
    }

    private fun clickElements() {
        bStart.setOnClickListener {
            if (!playing) {
                initPlaying()
            }
        }

        ivCube1.setOnClickListener { checkSelection(1, ivCube1) }
        ivCube2.setOnClickListener { checkSelection(2, ivCube2) }
        ivCube3.setOnClickListener { checkSelection(3, ivCube3) }
    }

    private fun checkSelection(selectedPosition: Int, clickedImageView: ImageView) {
        if (!playing) return

        playing = false

        if (selectedPosition == positionBall) {
            tvResult.text = "You win!!!"
            clickedImageView.setImageResource(R.drawable.levantaok)
        } else {
            tvResult.text = "You lose!!!"
            clickedImageView.setImageResource(R.drawable.levantafail)
            showCorrectBall()
        }

        bStart.visibility = View.VISIBLE
    }

    private fun showCorrectBall() {
        when (positionBall) {
            1 -> ivCube1.setImageResource(R.drawable.levantaok)
            2 -> ivCube2.setImageResource(R.drawable.levantaok)
            3 -> ivCube3.setImageResource(R.drawable.levantaok)
        }
    }
}