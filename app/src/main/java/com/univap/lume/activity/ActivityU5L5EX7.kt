package com.univap.lume.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.univap.lume.MainActivity
import com.univap.lume.R
import java.text.Normalizer
import java.util.Locale

class ActivityU5L5EX7 : AppCompatActivity() {

    private lateinit var txtExplicacaoU5L5EX7: TextView

    private lateinit var edtLacuna1: EditText
    private lateinit var edtLacuna2: EditText
    private lateinit var edtLacuna3: EditText
    private lateinit var edtLacuna4: EditText

    private lateinit var btnProximoU5L5EX7: Button
    private lateinit var btnAboutLicoes: Button
    private lateinit var btnStopLicoes: Button

    private val resposta1 = "pulou"
    private val resposta2 = "late"
    private val resposta3 = "chocolate"
    private val resposta4 = "ouro"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade5licao5ex7)

        txtExplicacaoU5L5EX7 = findViewById(R.id.txtExplicacaoU5L5EX7)

        edtLacuna1 = findViewById(R.id.edtLacuna1)
        edtLacuna2 = findViewById(R.id.edtLacuna2)
        edtLacuna3 = findViewById(R.id.edtLacuna3)
        edtLacuna4 = findViewById(R.id.edtLacuna4)

        btnProximoU5L5EX7 = findViewById(R.id.btnProximoU5L5EX7)
        btnAboutLicoes = findViewById(R.id.btn_aboutlicoes)
        btnStopLicoes = findViewById(R.id.btn_stoplicoes)

        btnProximoU5L5EX7.setOnClickListener {
            verificarExercicio()
        }

        btnAboutLicoes.setOnClickListener {
            startActivity(Intent(this, ActivityAboutLicoes::class.java))
        }

        btnStopLicoes.setOnClickListener {
            startActivity(
                Intent(this, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }
    }

    private fun verificarExercicio() {

        val texto1 = edtLacuna1.text.toString().trim()
        val texto2 = edtLacuna2.text.toString().trim()
        val texto3 = edtLacuna3.text.toString().trim()
        val texto4 = edtLacuna4.text.toString().trim()

        if (texto1.isBlank() || texto2.isBlank() ||
            texto3.isBlank() || texto4.isBlank()
        ) {
            Toast.makeText(
                this,
                "Preencha todos os campos do exercício",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (!normalizar(texto1).equals(normalizar(resposta1), ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(texto2).equals(normalizar(resposta2), ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(texto3).equals(normalizar(resposta3), ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(texto4).equals(normalizar(resposta4), ignoreCase = true)) { mostrarErro(); return }

        val prefs = getSharedPreferences("Progresso", MODE_PRIVATE)
        prefs.edit().putInt("U5L5E7_concluido", 1).apply()

        Toast.makeText(
            this,
            "Parabéns, suas respostas estão corretas!!!",
            Toast.LENGTH_SHORT
        ).show()

        // Altere para a próxima Activity do seu fluxo
        startActivity(Intent(this, ActivityU5L5EX8::class.java))
        finish()
    }

    private fun mostrarErro() {
        Toast.makeText(
            this,
            "Uma ou mais palavras estão incorretas.",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun normalizar(texto: String): String {
        val semAcento = Normalizer.normalize(texto, Normalizer.Form.NFD)
            .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
        val semPontuacao = semAcento.replace(Regex("[^a-zA-Z0-9\\s]"), "")
        return semPontuacao
            .lowercase(Locale.getDefault())
            .trim()
            .replace(Regex("\\s+"), " ")
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}