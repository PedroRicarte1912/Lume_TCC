package com.univap.lume.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.univap.lume.R

class ActivityLicoes5 : AppCompatActivity() {

    private lateinit var btn_aboutlicoes5: Button
    private lateinit var btn_stoplicoes5: Button

    private lateinit var btn_U5L5E1: Button
    private lateinit var btn_U5L5E2: Button
    private lateinit var btn_U5L5E3: Button
    private lateinit var btn_U5L5E4: Button
    private lateinit var btn_U5L5E5: Button
    private lateinit var btn_U5L5E6: Button

    private lateinit var btn_proximaUnidade5: Button

    private lateinit var txtMensagemL5: TextView
    private lateinit var txtProgressoL5: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_licoes5)

        btn_aboutlicoes5 = findViewById(R.id.btn_aboutlicoes5)
        btn_stoplicoes5 = findViewById(R.id.btn_stoplicoes5)

        btn_U5L5E1 = findViewById(R.id.btn_U5L5E1)
        btn_U5L5E2 = findViewById(R.id.btn_U5L5E2)
        btn_U5L5E3 = findViewById(R.id.btn_U5L5E3)
        btn_U5L5E4 = findViewById(R.id.btn_U5L5E4)
        btn_U5L5E5 = findViewById(R.id.btn_U5L5E5)
        btn_U5L5E6 = findViewById(R.id.btn_U5L5E6)

        btn_proximaUnidade5 = findViewById(R.id.btn_proximaUnidade5)

        txtMensagemL5 = findViewById(R.id.txtMensagemL5)
        txtProgressoL5 = findViewById(R.id.txtProgressoL5)

        btn_aboutlicoes5.setOnClickListener {
            startActivity(Intent(this, ActivityAboutLicoes::class.java))
        }

        btn_stoplicoes5.setOnClickListener {
            startActivity(
                Intent(this, ActivityStopLicoesAbout::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }

        btn_proximaUnidade5.setOnClickListener {
            // Quando existir a Unidade 6, altere aqui.
            finish()
        }

        // Exercício 1
        btn_U5L5E1.setOnClickListener {
            startActivity(Intent(this, ActivityU5L5EX1::class.java))
        }

        // Exercício 2
        btn_U5L5E2.setOnClickListener {
            startActivity(Intent(this, ActivityU5L5EX2::class.java))
        }

        // Exercício 3 (abre a parte 2 automaticamente)
        btn_U5L5E3.setOnClickListener {
            startActivity(Intent(this, ActivityU5L5EX3::class.java))
        }

        // Exercício 4 (abre a parte 2 automaticamente)
        btn_U5L5E4.setOnClickListener {
            startActivity(Intent(this, ActivityU5L5EX4::class.java))
        }

        // Exercício 5 (abre a parte 2 automaticamente)
        btn_U5L5E5.setOnClickListener {
            startActivity(Intent(this, ActivityU5L5EX5::class.java))
        }

        // Exercício 6
        btn_U5L5E6.setOnClickListener {
           startActivity(Intent(this, ActivityU5L5EX6::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        atualizarProgresso()
    }

    private fun atualizarProgresso() {

        val prefs = getSharedPreferences("Progresso", MODE_PRIVATE)

        val licoesConcluidas = listOf(

            // Exercício 1
            "U5L5EX1_concluido",

            // Exercício 2
            "U5L5EX2_concluido",

            // Exercício 3 (considera concluído somente após a parte 2)
            "U5L5E3_2_concluido",

            // Exercício 4
            "U5L5E4_2_concluido",

            // Exercício 5
            "U5L5E5_2_concluido",

            // Exercício 6
            "U5L5EX6_concluido"

        ).count { prefs.getInt(it, 0) == 1 }

        txtProgressoL5.text = "Lições concluídas: $licoesConcluidas / 6"

        atualizarBotoes(prefs)
    }

    private fun atualizarBotoes(prefs: android.content.SharedPreferences) {

        btn_U5L5E1.isEnabled = true

        btn_U5L5E2.isEnabled =
            prefs.getInt("U5L5EX1_concluido", 0) == 1

        btn_U5L5E3.isEnabled =
            prefs.getInt("U5L5EX2_concluido", 0) == 1

        btn_U5L5E4.isEnabled =
            prefs.getInt("U5L5E3_2_concluido", 0) == 1

        btn_U5L5E5.isEnabled =
            prefs.getInt("U5L5E4_2_concluido", 0) == 1

        btn_U5L5E6.isEnabled =
            prefs.getInt("U5L5E5_2_concluido", 0) == 1
    }
}