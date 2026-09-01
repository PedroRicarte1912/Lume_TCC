package com.univap.lume.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.univap.lume.R

class ActivityLicoes4 : AppCompatActivity() {

    private lateinit var btn_aboutlicoes4: Button
    private lateinit var btn_stoplicoes4: Button

    private lateinit var btn_U4L4E1: Button
    private lateinit var btn_U4L4E2: Button
    private lateinit var btn_U4L4E3: Button

    private lateinit var btn_proximaUnidade4: Button

    private lateinit var txtMensagemL4: TextView
    private lateinit var txtProgressoL4: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_licoes4)

        btn_aboutlicoes4    = findViewById(R.id.btn_aboutlicoes4)
        btn_stoplicoes4     = findViewById(R.id.btn_stoplicoes4)

        btn_U4L4E1          = findViewById(R.id.btn_U4L4E1)
        btn_U4L4E2          = findViewById(R.id.btn_U4L4E2)
        btn_U4L4E3          = findViewById(R.id.btn_U4L4E3)

        btn_proximaUnidade4 = findViewById(R.id.btn_proximaUnidade4)

        txtMensagemL4       = findViewById(R.id.txtMensagemL4)
        txtProgressoL4      = findViewById(R.id.txtProgressoL4)

        btn_aboutlicoes4.setOnClickListener {
            startActivity(Intent(this, ActivityAboutLicoes::class.java))
        }

        btn_stoplicoes4.setOnClickListener {
            startActivity(
                Intent(this, ActivityStopLicoesAbout::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }

        btn_proximaUnidade4.setOnClickListener {

            val intent = Intent(this, ActivityParabens::class.java).apply {
                putExtra(ActivityParabens.EXTRA_UNIDADE_CONCLUIDA, 4)
            }
            startActivity(intent)
            finish()
        }

        btn_U4L4E1.setOnClickListener {
            startActivity(Intent(this, ActivityU4L4EX1::class.java))
        }

        btn_U4L4E2.setOnClickListener {
            startActivity(Intent(this, ActivityU4L4EX2::class.java))
        }

        btn_U4L4E3.setOnClickListener {
            startActivity(Intent(this, ActivityU4L4EX3::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        atualizarProgresso()
    }

    private fun atualizarProgresso() {
        val prefs = getSharedPreferences("Progresso", MODE_PRIVATE)

        val licoesConcluidas = listOf(
            "U4L4EX1_concluido",
            "U4L4EX2_concluido",
            "U4L4EX3_concluido"
        ).count { prefs.getInt(it, 0) == 1 }

        txtProgressoL4.text = "Lições concluídas: $licoesConcluidas / 3"
    }
}