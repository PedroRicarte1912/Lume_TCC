package com.univap.lume.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.univap.lume.R

class ActivityLicoes3 : AppCompatActivity() {

    private lateinit var btn_aboutlicoes: Button
    private lateinit var btn_stoplicoes: Button

    private lateinit var btn_U3L3E1: Button
    private lateinit var btn_U3L3E2: Button
    private lateinit var btn_U3L3E3: Button
    private lateinit var btn_U3L3E4: Button
    private lateinit var btn_U3L3E5: Button

    private lateinit var btn_proximaUnidade3: Button

    private lateinit var txt_mensagem9: TextView
    private lateinit var txt_mensagem10: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_licoes3)

        btn_aboutlicoes     = findViewById(R.id.btn_aboutlicoes5)
        btn_stoplicoes      = findViewById(R.id.btn_stoplicoes5)

        btn_U3L3E1          = findViewById(R.id.btn_U3L3E1)
        btn_U3L3E2          = findViewById(R.id.btn_U3L3E2)
        btn_U3L3E3          = findViewById(R.id.btn_U3L3E3)
        btn_U3L3E4          = findViewById(R.id.btn_U3L3E4)
        btn_U3L3E5          = findViewById(R.id.btn_U3L3E5)

        btn_proximaUnidade3 = findViewById(R.id.btn_proximaUnidade3)

        txt_mensagem9       = findViewById(R.id.txtMensagemL3)
        txt_mensagem10      = findViewById(R.id.txtProgressoL3)

        btn_aboutlicoes.setOnClickListener {
            startActivity(Intent(this, ActivityAboutLicoes::class.java))
        }

        btn_stoplicoes.setOnClickListener {
            startActivity(Intent(this, ActivityStopLicoesAbout::class.java))
        }

        btn_proximaUnidade3.setOnClickListener {
            startActivity(Intent(this, ActivityLicoes4::class.java))
        }

        btn_U3L3E1.setOnClickListener {
            startActivity(Intent(this, ActivityU3L3EX1::class.java))
        }

        btn_U3L3E2.setOnClickListener {
            startActivity(Intent(this, ActivityU3L3EX1_2::class.java))
        }

        btn_U3L3E3.setOnClickListener {
            startActivity(Intent(this, ActivityU3L3EX3::class.java))
        }

        btn_U3L3E4.setOnClickListener {
            startActivity(Intent(this, ActivityU3L3EX4::class.java))
        }

        btn_U3L3E5.setOnClickListener {
            startActivity(Intent(this, ActivityLicoes4::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        atualizarProgresso()
    }

    private fun atualizarProgresso() {
        val prefs = getSharedPreferences("Progresso", MODE_PRIVATE)

        val licoesConcluidas = listOf(
            "U3L3E1_concluido",
            "U3L3E2_concluido",
            "U3L3E3_concluido",
            "U3L3E4_concluido",
            "U3L3E5_concluido"
        ).count { prefs.getInt(it, 0) == 1 }

        txt_mensagem10.text = "Lições concluídas: $licoesConcluidas / 5"
    }
}