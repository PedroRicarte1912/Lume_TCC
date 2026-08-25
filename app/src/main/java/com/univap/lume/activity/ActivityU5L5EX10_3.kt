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

class ActivityU5L5EX10_3 : AppCompatActivity() {

    private lateinit var txtExplicacaoU5L5EX10_3: TextView
    private lateinit var btnProximoU5L5EX10_3: Button

    private lateinit var edtResposta1: EditText
    private lateinit var edtResposta2: EditText
    private lateinit var edtResposta3: EditText

    // Frase 1 (médio) - erro de concordância verbal
    private val fraseErrada1 = "Os alunos estudam muito porque eles quer passar na prova."
    private val fraseCorreta1 = "Os alunos estudam muito porque eles querem passar na prova."

    // Frase 2 (médio) - erro de concordância nominal (advérbio no lugar de adjetivo)
    private val fraseErrada2 = "As crianças brincavam feliz no parque ontem."
    private val fraseCorreta2 = "As crianças brincavam felizes no parque ontem."

    // Frase 3 (difícil) - erro com verbo impessoal "fazer" indicando tempo
    private val fraseErrada3 = "Fazem dois anos que eu não via meus avós."
    private val fraseCorreta3 = "Faz dois anos que eu não via meus avós."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade5licao5ex10_3)

        txtExplicacaoU5L5EX10_3 = findViewById(R.id.txtExplicacaoU5L5EX10_3)
        btnProximoU5L5EX10_3 = findViewById(R.id.btnProximoU5L5EX10_3)

        val txtFraseErrada1: TextView = findViewById(R.id.txtFraseErradaU5L5EX10_3_1)
        val txtFraseErrada2: TextView = findViewById(R.id.txtFraseErradaU5L5EX10_3_2)
        val txtFraseErrada3: TextView = findViewById(R.id.txtFraseErradaU5L5EX10_3_3)

        txtFraseErrada1.text = fraseErrada1
        txtFraseErrada2.text = fraseErrada2
        txtFraseErrada3.text = fraseErrada3

        edtResposta1 = findViewById(R.id.edtRespostaU5L5EX10_3_1)
        edtResposta2 = findViewById(R.id.edtRespostaU5L5EX10_3_2)
        edtResposta3 = findViewById(R.id.edtRespostaU5L5EX10_3_3)

        btnProximoU5L5EX10_3.setOnClickListener {
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
        prefs.edit().putInt("U5L5E10_3_concluido", 1).apply()

        Toast.makeText(
            this,
            "Parabéns, suas respostas estão corretas!!!",
            Toast.LENGTH_SHORT
        ).show()

        // Este é o último exercício do app: leva o aluno para a tela de conclusão,
        // informando que a Unidade 5 (última) foi concluída
        startActivity(
            Intent(this, ActivityParabens::class.java)
                .putExtra(ActivityParabens.EXTRA_UNIDADE_CONCLUIDA, ActivityParabens.TOTAL_UNIDADES)
        )
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