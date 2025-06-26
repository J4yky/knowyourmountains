package com.example.knowyourmountains

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val lottieAnimationView: LottieAnimationView = findViewById(R.id.lottie_animation_view)

        lottieAnimationView.addAnimatorUpdateListener { animation ->
            if (animation.animatedFraction == 1f) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}