package com.example.xkcdcomicview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class XKCDExplanationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xkcdexplanation)

        val explanationString = intent.getStringExtra(MainActivity.EXPLANATION_TEXT_INTENT_TAG)
        val explanationView = findViewById<TextView>(R.id.xkcdExplanationView)

        explanationView.text = explanationString
    }
}
