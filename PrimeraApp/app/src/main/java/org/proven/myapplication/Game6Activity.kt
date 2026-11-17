package org.proven.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import kotlin.random.Random

class Game6Activity : ComponentActivity() {

    private lateinit var bStart: Button
    private lateinit var ivCube1: ImageView
    private lateinit var ivCube2: ImageView
    private lateinit var ivCube3: ImageView
    private lateinit var ivCube4: ImageView
    private lateinit var ivCube5: ImageView
    private lateinit var ivCube6: ImageView
    private lateinit var tvResult: TextView

    private var playing = false
    private var positionBall = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game6)

        initElements()
        clickElements()
    }

    private fun getPositionBall() {
        // Genera un número aleatorio del 1 al 6
        positionBall = Random.nextInt(from = 1, until = 7)
    }

    private fun initElements() {
        bStart = findViewById(R.id.bStart)
        ivCube1 = findViewById(R.id.ivCube1)
        ivCube2 = findViewById(R.id.ivCube2)
        ivCube3 = findViewById(R.id.ivCube3)
        ivCube4 = findViewById(R.id.ivCube4)
        ivCube5 = findViewById(R.id.ivCube5)
        ivCube6 = findViewById(R.id.ivCube6)
        tvResult = findViewById(R.id.textView1)

        tvResult.text = ""
    }

    private fun initPlaying() {
        playing = true
        getPositionBall()

        bStart.visibility = View.INVISIBLE
        tvResult.text = "¡Elige un coco!"

        val cubes = arrayOf(ivCube1, ivCube2, ivCube3, ivCube4, ivCube5, ivCube6)
        for (cube in cubes) {
            cube.setImageResource(R.drawable.cubilete)
        }
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
        ivCube4.setOnClickListener { checkSelection(4, ivCube4) }
        ivCube5.setOnClickListener { checkSelection(5, ivCube5) }
        ivCube6.setOnClickListener { checkSelection(6, ivCube6) }
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
            4 -> ivCube4.setImageResource(R.drawable.levantaok)
            5 -> ivCube5.setImageResource(R.drawable.levantaok)
            6 -> ivCube6.setImageResource(R.drawable.levantaok)
        }
    }
}