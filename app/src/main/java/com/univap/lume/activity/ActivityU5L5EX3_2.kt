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

class ActivityU5L5EX3_2 : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var txtExplicacaoU5L5EX3_2: TextView

    private lateinit var btnFrase5: Button
    private lateinit var btnFrase6: Button
    private lateinit var btnFrase7: Button
    private lateinit var btnProximoU5L5EX3_2: Button

    private lateinit var edtFrase5: EditText
    private lateinit var edtFrase6: EditText
    private lateinit var edtFrase7: EditText

    private val frase5 = "A casa é bonita."
    private val frase6 = "O bolo é de coco."
    private val frase7 = "O pato nada no lago."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade5licao5ex3_2)

        tts = TextToSpeech(this, this)
        tts.setSpeechRate(0.8f)

        txtExplicacaoU5L5EX3_2 = findViewById(R.id.txtExplicacaoU5L5EX3_2)

        btnFrase5 = findViewById(R.id.btnFrase5)
        btnFrase6 = findViewById(R.id.btnFrase6)
        btnFrase7 = findViewById(R.id.btnFrase7)

        btnProximoU5L5EX3_2 = findViewById(R.id.btnProximoU5L5EX3_2)

        edtFrase5 = findViewById(R.id.edtFrase5)
        edtFrase6 = findViewById(R.id.edtFrase6)
        edtFrase7 = findViewById(R.id.edtFrase7)

        btnFrase5.setOnClickListener { falar(frase5) }
        btnFrase6.setOnClickListener { falar(frase6) }
        btnFrase7.setOnClickListener { falar(frase7) }

        btnProximoU5L5EX3_2.setOnClickListener {
            verificarExercicio()
        }
    }

    private fun verificarExercicio() {
        val textoFrase5 = edtFrase5.text.toString().trim()
        val textoFrase6 = edtFrase6.text.toString().trim()
        val textoFrase7 = edtFrase7.text.toString().trim()

        if (textoFrase5.isBlank() || textoFrase6.isBlank() || textoFrase7.isBlank()) {
            Toast.makeText(this, "Preencha todos os campos do exercício", Toast.LENGTH_SHORT).show()
            return
        }

        if (!normalizar(textoFrase5).equals(normalizar(frase5), ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(textoFrase6).equals(normalizar(frase6), ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(textoFrase7).equals(normalizar(frase7), ignoreCase = true)) { mostrarErro(); return }

        val prefs = getSharedPreferences("Progresso", MODE_PRIVATE)
        prefs.edit().putInt("U5L5E3_2_concluido", 1).apply()

        Toast.makeText(this, "Parabéns, suas respostas estão corretas!!!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, ActivityU5L5EX4::class.java))
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