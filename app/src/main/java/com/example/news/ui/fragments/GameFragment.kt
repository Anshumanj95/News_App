package com.example.news.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.news.R
import com.example.news.databinding.FragmentGameBinding


class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding?=null
    private val binding get() = _binding!!
    var gameActive = true
    var activePlayer = 0
    var gameState = intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2)
    var winPositions = arrayOf(
        intArrayOf(0, 1, 2),
        intArrayOf(3, 4, 5),
        intArrayOf(6, 7, 8),
        intArrayOf(0, 3, 6),
        intArrayOf(1, 4, 7),
        intArrayOf(2, 5, 8),
        intArrayOf(0, 4, 8),
        intArrayOf(2, 4, 6)
    )
    var counter = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentGameBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView0.setOnClickListener {
            playerTap(it)
        }
        binding.imageView1.setOnClickListener {
            playerTap(it)
        }
        binding.imageView2.setOnClickListener {
            playerTap(it)
        }
        binding.imageView3.setOnClickListener {
            playerTap(it)
        }
        binding.imageView4.setOnClickListener {
            playerTap(it)
        }
        binding.imageView5.setOnClickListener {
            playerTap(it)
        }
        binding.imageView6.setOnClickListener {
            playerTap(it)
        }
        binding.imageView7.setOnClickListener {
            playerTap(it)
        }
        binding.imageView8.setOnClickListener {
            playerTap(it)
        }






    }
    @SuppressLint("SetTextI18n")
    fun playerTap(view: View) {
        val img: ImageView = view as ImageView
        val tappedImage: Int = img.tag.toString().toInt()
        if (!gameActive) {
            gameReset(view)
        }
        if (gameState.get(tappedImage) === 2) {
            counter++

            if (counter === 9) {
                gameActive = false
            }

            gameState[tappedImage] = activePlayer
            img.setTranslationY(-1000f)
            if (activePlayer === 0) {
                img.setImageResource(R.drawable.x)
                activePlayer = 1
                val status: TextView = binding.status
                status.text = "O's Turn - Tap to play"
            } else {
                // set the image of o
                img.setImageResource(R.drawable.o)
                activePlayer = 0
                val status: TextView = binding.status
                status.text = "X's Turn - Tap to play"
            }
            img.animate().translationYBy(1000f).setDuration(300)
        }
        var flag = 0
        for (winPosition in winPositions) {
            if (gameState.get(winPosition[0]) === gameState.get(winPosition[1]) && gameState.get(
                    winPosition[1]
                ) === gameState.get(winPosition[2]) && gameState.get(winPosition[0]) !== 2
            ) {
                flag = 1
                var winnerStr: String
                gameActive = false
                winnerStr = if (gameState.get(winPosition[0]) === 0) {
                    "X has won"
                } else {
                    "O has won"
                }
                val status: TextView = binding.status
                status.text = winnerStr
            }
        }
        if (counter === 9 && flag == 0) {
            val status: TextView = binding.status
            status.text = "Match Draw"
        }
    }
    fun gameReset(view: View?) {
        gameActive = true
        activePlayer = 0
        for (i in gameState.indices) {
            gameState[i] = 2
        }
        binding.imageView0.setImageResource(0)
        binding.imageView1.setImageResource(0)
        binding.imageView2.setImageResource(0)
        binding.imageView3.setImageResource(0)
        binding.imageView4.setImageResource(0)
        binding.imageView5.setImageResource(0)
        binding.imageView6.setImageResource(0)
        binding.imageView7.setImageResource(0)
        binding.imageView8.setImageResource(0)
        val status: TextView = binding.status
        status.text = "X's Turn - Tap to play"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}