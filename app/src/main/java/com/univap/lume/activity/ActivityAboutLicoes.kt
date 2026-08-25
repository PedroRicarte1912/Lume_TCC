package com.univap.lume.activity

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.univap.lume.R

class ActivityAboutLicoes : AppCompatActivity() {
    private lateinit var btn_voltalicoes: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aboutlicoes)

        btn_voltalicoes = findViewById(R.id.btn_voltalicoes)

        // finish() volta para qualquer Activity que tiver aberto esta,
        // sem precisar saber qual é — funciona para todos os exercícios
        btn_voltalicoes.setOnClickListener {
            finish()
        }
    }
}