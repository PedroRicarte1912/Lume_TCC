package com.univap.lume.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.univap.lume.R
import java.util.Locale

class ActivityU5L5EX9 : AppCompatActivity() {

    private lateinit var txtExplicacaoU5L5EX9: TextView
    private lateinit var btnProximoU5L5EX9: Button

    private lateinit var edtResposta1: EditText
    private lateinit var edtResposta2: EditText
    private lateinit var edtResposta3: EditText

    // Frase 1 (fácil) - erro de concordância de gênero
    private val fraseErrada1 = "A menina bonito chegou."
    private val fraseCorreta1 = "A menina bonita chegou."

    // Frase 2 (médio) - erro de concordância verbal
    private val fraseErrada2 = "Nós vai à praia no sábado."
    private val fraseCorreta2 = "Nós vamos à praia no sábado."

    // Frase 3 (difícil) - erro de concordância verbal
    private val fraseErrada3 = "Ele fazem os deveres de casa todos os dias."
    private val fraseCorreta3 = "Ele faz os deveres de casa todos os dias."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade5licao5ex9)

        txtExplicacaoU5L5EX9 = findViewById(R.id.txtExplicacaoU5L5EX9)
        btnProximoU5L5EX9 = findViewById(R.id.btnProximoU5L5EX9)

        val txtFraseErrada1: TextView = findViewById(R.id.txtFraseErradaU5L5EX9_1)
        val txtFraseErrada2: TextView = findViewById(R.id.txtFraseErradaU5L5EX9_2)
        val txtFraseErrada3: TextView = findViewById(R.id.txtFraseErradaU5L5EX9_3)

        txtFraseErrada1.text = fraseErrada1
        txtFraseErrada2.text = fraseErrada2
        txtFraseErrada3.text = fraseErrada3

        edtResposta1 = findViewById(R.id.edtRespostaU5L5EX9_1)
        edtResposta2 = findViewById(R.id.edtRespostaU5L5EX9_2)
        edtResposta3 = findViewById(R.id.edtRespostaU5L5EX9_3)

        btnProximoU5L5EX9.setOnClickListener {
            verificarExercicio()
        }
    }

    private fun verificarExercicio() {

        val resposta1 = edtResposta1.text.toString()
        val resposta2 = edtResposta2.text.toString()
        val resposta3 = edtResposta3.text.toString()

        if (resposta1.isBlank() || resposta2.isBlank() || resposta3.isBlank()) {
            Toast.makeText(
                this,
                "Corrija todas as frases antes de continuar",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (!normalizar(resposta1).equals(normalizar(fraseCorreta1), ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(resposta2).equals(normalizar(fraseCorreta2), ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(resposta3).equals(normalizar(fraseCorreta3), ignoreCase = true)) { mostrarErro(); return }

        val prefs = getSharedPreferences("Progresso", MODE_PRIVATE)
        prefs.edit().putInt("U5L5E9_concluido", 1).apply()

        Toast.makeText(
            this,
            "Parabéns, suas respostas estão corretas!!!",
            Toast.LENGTH_SHORT
        ).show()

        // Altere para a próxima Activity do seu fluxo
        startActivity(Intent(this, ActivityU5L5EX10::class.java))
        finish()
    }

    private fun mostrarErro() {
        Toast.makeText(
            this,
            "Uma ou mais correções estão erradas. Tente novamente.",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun normalizar(texto: String): String {
        return texto
            .trim()
            .lowercase(Locale.getDefault())
            .replace(Regex("\\s+"), " ")
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}