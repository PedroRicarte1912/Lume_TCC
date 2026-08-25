package com.univap.lume.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.univap.lume.R
import java.text.Normalizer
import java.util.Locale

class ActivityU5L5EX8 : AppCompatActivity() {

    private lateinit var txtExplicacaoU5L5EX8: TextView
    private lateinit var btnProximoU5L5EX8: Button

    private lateinit var chipGroupBanco1: ChipGroup
    private lateinit var chipGroupResposta1: ChipGroup
    private lateinit var chipGroupBanco2: ChipGroup
    private lateinit var chipGroupResposta2: ChipGroup
    private lateinit var chipGroupBanco3: ChipGroup
    private lateinit var chipGroupResposta3: ChipGroup

    // Frase 1 (fácil)
    private val frase1 = "A bola é azul."
    // Frase 2 (médio)
    private val frase2 = "O passarinho fez um ninho na árvore do quintal."
    // Frase 3 (difícil)
    private val frase3 = "A chave do carro estava em cima da mesa grande."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade5licao5ex8)

        txtExplicacaoU5L5EX8 = findViewById(R.id.txtExplicacaoU5L5EX8)
        btnProximoU5L5EX8 = findViewById(R.id.btnProximoU5L5EX8)

        chipGroupBanco1 = findViewById(R.id.chipGroupBanco1)
        chipGroupResposta1 = findViewById(R.id.chipGroupResposta1)
        chipGroupBanco2 = findViewById(R.id.chipGroupBanco2)
        chipGroupResposta2 = findViewById(R.id.chipGroupResposta2)
        chipGroupBanco3 = findViewById(R.id.chipGroupBanco3)
        chipGroupResposta3 = findViewById(R.id.chipGroupResposta3)

        montarItem(frase1, chipGroupBanco1, chipGroupResposta1)
        montarItem(frase2, chipGroupBanco2, chipGroupResposta2)
        montarItem(frase3, chipGroupBanco3, chipGroupResposta3)

        btnProximoU5L5EX8.setOnClickListener {
            verificarExercicio()
        }
    }

    /**
     * Cria os chips embaralhados no banco de palavras e configura
     * o comportamento de toque (mover para a resposta e vice-versa).
     */
    private fun montarItem(frase: String, banco: ChipGroup, resposta: ChipGroup) {
        val palavras = frase.split(" ").filter { it.isNotBlank() }.shuffled().toMutableList()

        banco.removeAllViews()
        resposta.removeAllViews()

        palavras.forEach { palavra ->
            val chip = criarChip(palavra)
            chip.setOnClickListener {
                banco.removeView(chip)
                resposta.addView(chip)
                chip.setOnClickListener {
                    resposta.removeView(chip)
                    banco.addView(chip)
                    montarClickParaVoltar(chip, banco, resposta)
                }
            }
            banco.addView(chip)
        }
    }

    private fun montarClickParaVoltar(chip: Chip, banco: ChipGroup, resposta: ChipGroup) {
        chip.setOnClickListener {
            banco.removeView(chip)
            resposta.addView(chip)
            montarClickParaVoltar(chip, banco, resposta) // reaplica o listener de ida
            chip.setOnClickListener {
                resposta.removeView(chip)
                banco.addView(chip)
                montarClickParaVoltar(chip, banco, resposta)
            }
        }
    }

    private fun criarChip(texto: String): Chip {
        val chip = Chip(this)
        chip.text = texto
        chip.isClickable = true
        chip.isCheckable = false
        chip.chipBackgroundColor = getColorStateList(R.color.lume_azul)
        chip.setTextColor(getColor(R.color.lume_texto))
        return chip
    }

    private fun frasesMontadas(chipGroup: ChipGroup): String {
        val palavras = mutableListOf<String>()
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            palavras.add(chip.text.toString())
        }
        return palavras.joinToString(" ")
    }

    private fun verificarExercicio() {

        if (chipGroupResposta1.childCount == 0 ||
            chipGroupResposta2.childCount == 0 ||
            chipGroupResposta3.childCount == 0
        ) {
            Toast.makeText(
                this,
                "Monte todas as frases antes de continuar",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val resposta1 = frasesMontadas(chipGroupResposta1)
        val resposta2 = frasesMontadas(chipGroupResposta2)
        val resposta3 = frasesMontadas(chipGroupResposta3)

        if (!normalizar(resposta1).equals(normalizar(frase1), ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(resposta2).equals(normalizar(frase2), ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(resposta3).equals(normalizar(frase3), ignoreCase = true)) { mostrarErro(); return }

        val prefs = getSharedPreferences("Progresso", MODE_PRIVATE)
        prefs.edit().putInt("U5L5E8_concluido", 1).apply()

        Toast.makeText(
            this,
            "Parabéns, suas respostas estão corretas!!!",
            Toast.LENGTH_SHORT
        ).show()

        // Altere para a próxima Activity do seu fluxo
        startActivity(Intent(this, ActivityU5L5EX9::class.java))
        finish()
    }

    private fun mostrarErro() {
        Toast.makeText(
            this,
            "Uma ou mais frases estão na ordem errada.",
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