package com.dicoding.picodiploma.loginwithanimation.view.front

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityFrontBinding
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity

class front : AppCompatActivity() {
    private lateinit var binding: ActivityFrontBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrontBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        Handler().postDelayed({
            startActivity(Intent(this, WelcomeActivity::class.java))
        }, 5000)
        playAnimation()
    }

    private fun playAnimation() {
        val animasi = ObjectAnimator.ofFloat(binding.logo, View.ALPHA, 1f).setDuration(500)
        AnimatorSet().apply {
            playSequentially(
                animasi,
            )
            startDelay = 500

        }.start()
    }
}