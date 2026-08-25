package com.univap.lume.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.univap.lume.R
import java.text.Normalizer
import java.util.Locale

class ActivityU5L5EX5_2 : AppCompatActivity() {

    private lateinit var txtExplicacaoU5L5EX5_2: TextView

    private lateinit var edtFrase4: EditText
    private lateinit var edtFrase5: EditText
    private lateinit var edtFrase6: EditText

    private lateinit var btnProximoU5L5EX5_2: Button

    private val frase4 = "O palhaço deu uma risada bem alta."
    private val frase5 = "A chuva caiu e molhou as flores do jardim."
    private val frase6 = "Hoje o dia está perfeito para brincar."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade5licao5ex5_2)

        txtExplicacaoU5L5EX5_2 = findViewById(R.id.txtExplicacaoU5L5EX5_2)

        edtFrase4 = findViewById(R.id.edtFrase4)
        edtFrase5 = findViewById(R.id.edtFrase5)
        edtFrase6 = findViewById(R.id.edtFrase6)

        btnProximoU5L5EX5_2 = findViewById(R.id.btnProximoU5L5EX5_2)

        btnProximoU5L5EX5_2.setOnClickListener {
            verificarExercicio()
        }
    }

    private fun verificarExercicio() {

        val textoFrase4 = edtFrase4.text.toString().trim()
        val textoFrase5 = edtFrase5.text.toString().trim()
        val textoFrase6 = edtFrase6.text.toString().trim()

        if (textoFrase4.isBlank() ||
            textoFrase5.isBlank() ||
            textoFrase6.isBlank()
        ) {
            Toast.makeText(
                this,
                "Preencha todos os campos do exercício",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (!normalizar(textoFrase4).equals(normalizar(frase4), ignoreCase = true)) {
            mostrarErro()
            return
        }

        if (!normalizar(textoFrase5).equals(normalizar(frase5), ignoreCase = true)) {
            mostrarErro()
            return
        }

        if (!normalizar(textoFrase6).equals(normalizar(frase6), ignoreCase = true)) {
            mostrarErro()
            return
        }

        val prefs = getSharedPreferences("Progresso", MODE_PRIVATE)
        prefs.edit().putInt("U5L5E5_2_concluido", 1).apply()

        Toast.makeText(
            this,
            "Parabéns, suas respostas estão corretas!!!",
            Toast.LENGTH_SHORT
        ).show()

        // Altere para a próxima Activity do seu fluxo
        startActivity(Intent(this, ActivityU5L5EX6::class.java))
        finish()
    }

    private fun mostrarErro() {
        Toast.makeText(
            this,
            "Uma ou mais frases estão incorretas.",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun normalizar(texto: String): String {

        val semAcento = Normalizer.normalize(texto, Normalizer.Form.NFD)
            .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")

        val semPontuacao = semAcento.replace(
            Regex("[^a-zA-Z0-9\\s]"),
            ""
        )

        return semPontuacao
            .lowercase(Locale.getDefault())
            .trim()
            .replace(Regex("\\s+"), " ")
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}