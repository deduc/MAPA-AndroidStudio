package com.proyecto.mapa


import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MarkerDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marker_detail)

        val title = intent.getStringExtra("TITLE")
        val description = intent.getStringExtra("DESCRIPTION")

        findViewById<TextView>(R.id.textTitle).text = title
        findViewById<TextView>(R.id.textDescription).text = description
    }
}
