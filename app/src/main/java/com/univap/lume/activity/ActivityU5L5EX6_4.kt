package com.univap.lume.activity

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.univap.lume.R
import java.text.Normalizer
import java.util.Locale

class ActivityU5L5EX6_4 : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var txtExplicacaoU5L5EX6_4: TextView

    private lateinit var btnFrase1: Button
    private lateinit var btnFrase2: Button
    private lateinit var btnFrase3: Button
    private lateinit var btnFrase4: Button
    private lateinit var btnProximoU5L5EX6_4: Button

    private lateinit var edtFrase1: EditText
    private lateinit var edtFrase2: EditText
    private lateinit var edtFrase3: EditText
    private lateinit var edtFrase4: EditText

    private val frase1 = "O sapo pulou no rio."
    private val frase2 = "Aline comprou uma boneca nova na loja de brinquedos."
    private val frase3 = "A professora explicou que ler livros todos os dias ajuda a nossa mente a imaginar mundos completamente novos."
    private val frase4 = "O boné é do Dudu."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade5licao5ex6_4)

        tts = TextToSpeech(this, this)
        tts.setSpeechRate(0.8f)

        txtExplicacaoU5L5EX6_4 = findViewById(R.id.txtExplicacaoU5L5EX6_4)

        btnFrase1 = findViewById(R.id.btnFrase1)
        btnFrase2 = findViewById(R.id.btnFrase2)
        btnFrase3 = findViewById(R.id.btnFrase3)
        btnFrase4 = findViewById(R.id.btnFrase4)

        btnProximoU5L5EX6_4 = findViewById(R.id.btnProximoU5L5EX6_4)

        edtFrase1 = findViewById(R.id.edtFrase1)
        edtFrase2 = findViewById(R.id.edtFrase2)
        edtFrase3 = findViewById(R.id.edtFrase3)
        edtFrase4 = findViewById(R.id.edtFrase4)

        btnFrase1.setOnClickListener { falar(frase1) }
        btnFrase2.setOnClickListener { falar(frase2) }
        btnFrase3.setOnClickListener { falar(frase3) }
        btnFrase4.setOnClickListener { falar(frase4) }

        btnProximoU5L5EX6_4.setOnClickListener {
            verificarExercicio()
        }
    }

    private fun verificarExercicio() {
        val textoFrase1 = edtFrase1.text.toString().trim()
        val textoFrase2 = edtFrase2.text.toString().trim()
        val textoFrase3 = edtFrase3.text.toString().trim()
        val textoFrase4 = edtFrase4.text.toString().trim()

        if (textoFrase1.isBlank() || textoFrase2.isBlank() ||
            textoFrase3.isBlank() || textoFrase4.isBlank()
        ) {
            Toast.makeText(this, "Preencha todos os campos do exercício", Toast.LENGTH_SHORT).show()
            return
        }

        if (!normalizar(textoFrase1).equals(normalizar(frase1), ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(textoFrase2).equals(normalizar(frase2), ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(textoFrase3).equals(normalizar(frase3), ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(textoFrase4).equals(normalizar(frase4), ignoreCase = true)) { mostrarErro(); return }

        val prefs = getSharedPreferences("Progresso", MODE_PRIVATE)
        prefs.edit().putInt("U5L5E6_4_concluido", 1).apply()

        Toast.makeText(this, "Parabéns, suas respostas estão corretas!!!", Toast.LENGTH_SHORT).show()

        // Altere para a próxima Activity do seu fluxo
        startActivity(Intent(this, ActivityU5L5EX7::class.java))
        finish()
    }

    private fun mostrarErro() {
        Toast.makeText(this, "Frase(s) incorreta(s)", Toast.LENGTH_SHORT).show()
    }

    private fun normalizar(texto: String): String {
        val semAcento = Normalizer.normalize(texto, Normalizer.Form.NFD)
            .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
        val semPontuacao = semAcento.replace(Regex("[^a-zA-Z0-9\\s]"), "")
        return semPontuacao.lowercase(Locale.getDefault()).trim().replace(Regex("\\s+"), " ")
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