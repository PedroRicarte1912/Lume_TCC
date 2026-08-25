package com.univap.lume.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.univap.lume.R

class ActivityU5L5EX10_2 : AppCompatActivity() {

    private lateinit var txtExplicacaoU5L5EX10_2: TextView
    private lateinit var btnProximoU5L5EX10_2: Button

    // Grupos de botões de pontuação por item
    private lateinit var botoesItem1: List<Button>
    private lateinit var botoesItem2: List<Button>
    private lateinit var botoesItem3: List<Button>

    // Pontuação escolhida pelo aluno em cada item (null = nada escolhido ainda)
    private var pontuacaoEscolhida1: String? = null
    private var pontuacaoEscolhida2: String? = null
    private var pontuacaoEscolhida3: String? = null

    // Frases sem pontuação e a pontuação correta de cada uma
    private val fraseBase1 = "Que susto você me deu"
    private val pontuacaoCorreta1 = "!"

    private val fraseBase2 = "Onde você guardou as chaves do carro"
    private val pontuacaoCorreta2 = "?"

    private val fraseBase3 = "Depois de muito esforço, ele finalmente conseguiu terminar o projeto"
    private val pontuacaoCorreta3 = "."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade5licao5ex10_2)

        txtExplicacaoU5L5EX10_2 = findViewById(R.id.txtExplicacaoU5L5EX10_2)
        btnProximoU5L5EX10_2 = findViewById(R.id.btnProximoU5L5EX10_2)

        val txtFrase1: TextView = findViewById(R.id.txtFraseSemPontuacao1)
        val txtFrase2: TextView = findViewById(R.id.txtFraseSemPontuacao2)
        val txtFrase3: TextView = findViewById(R.id.txtFraseSemPontuacao3)

        txtFrase1.text = fraseBase1
        txtFrase2.text = fraseBase2
        txtFrase3.text = fraseBase3

        val btnPonto1: Button = findViewById(R.id.btnPonto1)
        val btnExclamacao1: Button = findViewById(R.id.btnExclamacao1)
        val btnInterrogacao1: Button = findViewById(R.id.btnInterrogacao1)

        val btnPonto2: Button = findViewById(R.id.btnPonto2)
        val btnExclamacao2: Button = findViewById(R.id.btnExclamacao2)
        val btnInterrogacao2: Button = findViewById(R.id.btnInterrogacao2)

        val btnPonto3: Button = findViewById(R.id.btnPonto3)
        val btnExclamacao3: Button = findViewById(R.id.btnExclamacao3)
        val btnInterrogacao3: Button = findViewById(R.id.btnInterrogacao3)

        botoesItem1 = listOf(btnPonto1, btnExclamacao1, btnInterrogacao1)
        botoesItem2 = listOf(btnPonto2, btnExclamacao2, btnInterrogacao2)
        botoesItem3 = listOf(btnPonto3, btnExclamacao3, btnInterrogacao3)

        configurarGrupo(botoesItem1) { escolha -> pontuacaoEscolhida1 = escolha }
        configurarGrupo(botoesItem2) { escolha -> pontuacaoEscolhida2 = escolha }
        configurarGrupo(botoesItem3) { escolha -> pontuacaoEscolhida3 = escolha }

        btnProximoU5L5EX10_2.setOnClickListener {
            verificarExercicio()
        }
    }

    /**
     * Configura os 3 botões de um item para que apenas um fique
     * selecionado por vez (destaca o escolhido em verde) e guarda
     * a escolha através do callback [aoEscolher].
     */
    private fun configurarGrupo(botoes: List<Button>, aoEscolher: (String) -> Unit) {
        botoes.forEach { botao ->
            botao.setOnClickListener {
                botoes.forEach { it.backgroundTintList = getColorStateList(R.color.lume_azul) }
                botao.backgroundTintList = getColorStateList(R.color.lume_verde)
                aoEscolher(botao.text.toString())
            }
        }
    }

    private fun verificarExercicio() {

        if (pontuacaoEscolhida1 == null || pontuacaoEscolhida2 == null || pontuacaoEscolhida3 == null) {
            Toast.makeText(
                this,
                "Escolha a pontuação de todas as frases antes de continuar",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (pontuacaoEscolhida1 != pontuacaoCorreta1) { mostrarErro(); return }
        if (pontuacaoEscolhida2 != pontuacaoCorreta2) { mostrarErro(); return }
        if (pontuacaoEscolhida3 != pontuacaoCorreta3) { mostrarErro(); return }

        val prefs = getSharedPreferences("Progresso", MODE_PRIVATE)
        prefs.edit().putInt("U5L5E10_2_concluido", 1).apply()

        Toast.makeText(
            this,
            "Parabéns, suas respostas estão corretas!!!",
            Toast.LENGTH_SHORT
        ).show()

         startActivity(Intent(this, ActivityU5L5EX10_3::class.java))
        finish()
    }

    private fun mostrarErro() {
        Toast.makeText(
            this,
            "Uma ou mais pontuações estão erradas.",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}