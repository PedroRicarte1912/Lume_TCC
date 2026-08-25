package com.univap.lume.activity

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.univap.lume.R
import java.util.Locale

class ActivityU3L3EX4 : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech

    private lateinit var imgSopa: ImageView
    private lateinit var imgJanela: ImageView
    private lateinit var imgLeite: ImageView
    private lateinit var imgCachorro: ImageView
    private lateinit var imgDinheiro: ImageView

    private lateinit var tvCachorro: TextView
    private lateinit var tvLeite: TextView
    private lateinit var tvSopa: TextView
    private lateinit var tvDinheiro: TextView
    private lateinit var tvJanela: TextView

    private lateinit var btnEntregar: Button

    private var imagemSelecionada: View? = null
    private var palavraSelecionada: View? = null
    private val ligacoes = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade3licao3ex4)

        tts = TextToSpeech(this, this)
        tts.setSpeechRate(0.8f)

        imgSopa     = findViewById(R.id.imgSopa)
        imgJanela   = findViewById(R.id.imgJanela)
        imgLeite    = findViewById(R.id.imgLeite)
        imgCachorro = findViewById(R.id.imgCachorro)
        imgDinheiro = findViewById(R.id.imgDinheiro)

        tvCachorro  = findViewById(R.id.tvCachorro)
        tvLeite     = findViewById(R.id.tvLeite)
        tvSopa      = findViewById(R.id.tvSopa)
        tvDinheiro  = findViewById(R.id.tvDinheiro)
        tvJanela    = findViewById(R.id.tvJanela)

        btnEntregar = findViewById(R.id.btnEntregar)

        val imagens = listOf(imgSopa, imgJanela, imgLeite, imgCachorro, imgDinheiro)
        val palavras = listOf(tvCachorro, tvLeite, tvSopa, tvDinheiro, tvJanela)

        imagens.forEach { it.setOnClickListener { v -> selecionarImagem(v) } }
        palavras.forEach { it.setOnClickListener { v -> selecionarPalavra(v) } }

        btnEntregar.setOnClickListener { verificarExercicio() }
    }

    private fun selecionarImagem(view: View) {
        val tag = view.tag as String

        if (ligacoes.containsKey(tag)) {
            falar(tag)
            return
        }

        imagemSelecionada?.alpha = 1f
        if (imagemSelecionada == view) {
            imagemSelecionada = null
            return
        }

        imagemSelecionada = view
        view.alpha = 0.5f
        falar(tag)

        if (palavraSelecionada != null) tentarLigar()
    }

    private fun selecionarPalavra(view: View) {
        val tag = view.tag as String

        if (ligacoes.containsValue(tag)) return

        palavraSelecionada?.alpha = 1f
        if (palavraSelecionada == view) {
            palavraSelecionada = null
            return
        }

        palavraSelecionada = view
        view.alpha = 0.5f
        falar(tag)

        if (imagemSelecionada != null) tentarLigar()
    }

    private fun tentarLigar() {
        val img = imagemSelecionada ?: return
        val tv  = palavraSelecionada ?: return

        ligacoes[img.tag as String] = tv.tag as String

        img.alpha = 0.4f
        tv.alpha  = 0.4f
        tv.isClickable = false

        imagemSelecionada = null
        palavraSelecionada = null
    }

    private fun verificarExercicio() {
        if (ligacoes.size < 5) {
            Toast.makeText(this, "Ligue todas as imagens antes de entregar", Toast.LENGTH_SHORT).show()
            return
        }

        val pares = mapOf(
            "sopa"     to "sopa",
            "janela"   to "janela",
            "leite"    to "leite",
            "cachorro" to "cachorro",
            "dinheiro" to "dinheiro"
        )

        for ((imgTag, wordTag) in ligacoes) {
            if (pares[imgTag] != wordTag) {
                Toast.makeText(this, "Ops! Alguma ligação está errada. Tente de novo!", Toast.LENGTH_SHORT).show()
                resetarExercicio()
                return
            }
        }

        val prefs = getSharedPreferences("Progresso", MODE_PRIVATE)
        prefs.edit().putInt("U3L3E4_concluido", 1).apply()

        Toast.makeText(this, "Parabéns, suas repostas estão corretas!!!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, ActivityU3L3EX4_2::class.java))
        finish()
    }

    private fun resetarExercicio() {
        ligacoes.clear()
        imagemSelecionada = null
        palavraSelecionada = null

        listOf(imgSopa, imgJanela, imgLeite, imgCachorro, imgDinheiro).forEach { it.alpha = 1f }
        listOf(tvCachorro, tvLeite, tvSopa, tvDinheiro, tvJanela).forEach {
            it.alpha = 1f
            it.isClickable = true
        }
    }

    private fun falar(texto: String) {
        tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(
                Locale.Builder().setLanguage("pt").setRegion("BR").build()
            )
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                return
            }
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}