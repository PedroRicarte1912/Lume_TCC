package com.univap.lume.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.univap.lume.R

class ActivityLicoes1 : AppCompatActivity() {

    private lateinit var btnAboutLicoes: Button
    private lateinit var btnStopLicoes: Button
    private lateinit var btnU1L1: Button
    private lateinit var btnU1L2: Button
    private lateinit var btnU1L4: Button
    private lateinit var btnProximaUnidade1: Button
    private lateinit var txtMensagem9: TextView
    private lateinit var txtMensagem10: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_licoes)

        btnStopLicoes = findViewById(R.id.btn_stoplicoes)
        btnAboutLicoes = findViewById(R.id.btn_aboutlicoes)
        btnU1L1 = findViewById(R.id.btn_U1L1)
        btnU1L2 = findViewById(R.id.btn_U1L2)
        btnU1L4 = findViewById(R.id.btn_U1L4)
        btnProximaUnidade1 = findViewById(R.id.btn_proximaUnidade1)
        txtMensagem9 = findViewById(R.id.txt_mensagem9)
        txtMensagem10 = findViewById(R.id.txt_mensagem10)

        btnAboutLicoes.setOnClickListener {
            startActivity(Intent(this, ActivityAboutLicoes::class.java))
        }

        btnStopLicoes.setOnClickListener {
            startActivity(
                Intent(this, ActivityStopLicoesAbout::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }

        btnProximaUnidade1.setOnClickListener {
            startActivity(Intent(this, ActivityU2EX1::class.java))
        }

        btnU1L1.setOnClickListener {
            startActivity(Intent(this, ActivityU1L1EX1::class.java))
        }

        btnU1L2.setOnClickListener {
            startActivity(Intent(this, ActivityU1L1EX2::class.java))
        }

        btnU1L4.setOnClickListener {
            startActivity(Intent(this, ActivityU1L1EX4::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        atualizarProgresso()
    }

    private fun atualizarProgresso() {
        val prefs = getSharedPreferences("Progresso", MODE_PRIVATE)

        val licoesConcluidas = listOf(
            "U1L1_concluido",
            "U1L2_concluido",
            "U1L4_concluido"
        ).count { prefs.getInt(it, 0) == 1 }

        txtMensagem10.text = "Lições concluídas: $licoesConcluidas / 3"
    }
}