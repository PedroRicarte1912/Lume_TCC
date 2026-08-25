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

class ActivityU3L3EX4_2 : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech

    private lateinit var imgLua: ImageView
    private lateinit var imgFogo: ImageView
    private lateinit var imgEscola: ImageView
    private lateinit var imgPorta: ImageView

    private lateinit var tvEscola: TextView
    private lateinit var tvLua: TextView
    private lateinit var tvPorta: TextView
    private lateinit var tvFogo: TextView

    private lateinit var btnEntregar: Button

    private var imagemSelecionada: View? = null
    private var palavraSelecionada: View? = null
    private val ligacoes = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade3licao3ex4_2)

        tts = TextToSpeech(this, this)
        tts.setSpeechRate(0.8f)

        imgLua    = findViewById(R.id.imgLua)
        imgFogo   = findViewById(R.id.imgFogo)
        imgEscola = findViewById(R.id.imgEscola)
        imgPorta  = findViewById(R.id.imgPorta)

        tvEscola = findViewById(R.id.tvEscola)
        tvLua    = findViewById(R.id.tvLua)
        tvPorta  = findViewById(R.id.tvPorta)
        tvFogo   = findViewById(R.id.tvFogo)

        btnEntregar = findViewById(R.id.btnEntregar)

        val imagens = listOf(imgLua, imgFogo, imgEscola, imgPorta)
        val palavras = listOf(tvEscola, tvLua, tvPorta, tvFogo)

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
        if (ligacoes.size < 4) {
            Toast.makeText(this, "Ligue todas as imagens antes de entregar", Toast.LENGTH_SHORT).show()
            return
        }

        val pares = mapOf(
            "lua"    to "lua",
            "fogo"   to "fogo",
            "escola" to "escola",
            "porta"  to "porta"
        )

        for ((imgTag, wordTag) in ligacoes) {
            if (pares[imgTag] != wordTag) {
                Toast.makeText(this, "Ops! Alguma ligação está errada. Tente de novo!", Toast.LENGTH_SHORT).show()
                resetarExercicio()
                return
            }
        }

        val prefs = getSharedPreferences("Progresso", MODE_PRIVATE)
        prefs.edit().putInt("U3L3EX4_2", 1).apply()

        Toast.makeText(this, "Parabéns, suas respostas estão corretas!!!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, ActivityU3L3EX5::class.java))
        finish()
    }

    private fun resetarExercicio() {
        ligacoes.clear()
        imagemSelecionada = null
        palavraSelecionada = null

        listOf(imgLua, imgFogo, imgEscola, imgPorta).forEach { it.alpha = 1f }
        listOf(tvEscola, tvLua, tvPorta, tvFogo).forEach {
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