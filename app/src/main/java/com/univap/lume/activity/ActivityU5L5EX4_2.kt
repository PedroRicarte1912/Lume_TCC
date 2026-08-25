package com.univap.lume.activity

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.univap.lume.R
import java.text.Normalizer
import java.util.Locale

class ActivityU5L5EX4_2 : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech

    private lateinit var txtExplicacaoU5L5EX4_2: TextView

    private lateinit var btnFrase4: Button
    private lateinit var btnFrase5: Button
    private lateinit var btnFrase6: Button
    private lateinit var btnProximoU5L5EX4_2: Button

    private lateinit var edtFrase4: EditText
    private lateinit var edtFrase5: EditText
    private lateinit var edtFrase6: EditText

    private val frase4 = "O palhaço deu uma risada bem alta."
    private val frase5 = "Eu gosto de ler livros na escola."
    private val frase6 = "A chuva caiu e molhou as flores do jardim."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade5licao5ex4_2)

        tts = TextToSpeech(this, this)
        tts.setSpeechRate(0.8f)

        txtExplicacaoU5L5EX4_2 = findViewById(R.id.txtExplicacaoU5L5EX4_2)

        btnFrase4 = findViewById(R.id.btnFrase4)
        btnFrase5 = findViewById(R.id.btnFrase5)
        btnFrase6 = findViewById(R.id.btnFrase6)
        btnProximoU5L5EX4_2 = findViewById(R.id.btnProximoU5L5EX4_2)

        edtFrase4 = findViewById(R.id.edtFrase4)
        edtFrase5 = findViewById(R.id.edtFrase5)
        edtFrase6 = findViewById(R.id.edtFrase6)

        btnFrase4.setOnClickListener { falar(frase4) }
        btnFrase5.setOnClickListener { falar(frase5) }
        btnFrase6.setOnClickListener { falar(frase6) }

        btnProximoU5L5EX4_2.setOnClickListener {
            verificarExercicio()
        }
    }

    private fun verificarExercicio() {

        val t4 = edtFrase4.text.toString().trim()
        val t5 = edtFrase5.text.toString().trim()
        val t6 = edtFrase6.text.toString().trim()

        if (t4.isBlank() || t5.isBlank() || t6.isBlank()) {
            Toast.makeText(this, "Preencha todos os campos do exercício", Toast.LENGTH_SHORT).show()
            return
        }

        if (!normalizar(t4).equals(normalizar(frase4), true)) {
            mostrarErro()
            return
        }

        if (!normalizar(t5).equals(normalizar(frase5), true)) {
            mostrarErro()
            return
        }

        if (!normalizar(t6).equals(normalizar(frase6), true)) {
            mostrarErro()
            return
        }

        val prefs = getSharedPreferences("Progresso", MODE_PRIVATE)
        prefs.edit().putInt("U5L5E4_2_concluido", 1).apply()

        Toast.makeText(
            this,
            "Parabéns, suas respostas estão corretas!!!",
            Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this, ActivityU5L5EX5::class.java))
        finish()
    }

    private fun mostrarErro() {
        Toast.makeText(this, "Frase(s) incorreta(s)", Toast.LENGTH_SHORT).show()
    }

    private fun normalizar(texto: String): String {
        val semAcento = Normalizer.normalize(texto, Normalizer.Form.NFD)
            .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")

        val semPontuacao = semAcento.replace(Regex("[^a-zA-Z0-9\\s]"), "")

        return semPontuacao.lowercase(Locale.getDefault())
            .trim()
            .replace(Regex("\\s+"), " ")
    }

    private fun falar(texto: String) {
        tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(
                Locale.Builder()
                    .setLanguage("pt")
                    .setRegion("BR")
                    .build()
            )
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