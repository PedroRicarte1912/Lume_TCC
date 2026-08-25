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

class ActivityU3L3EX3 : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var txtExplicacao44: TextView

    private lateinit var btnCarro: Button
    private lateinit var btnNoite: Button
    private lateinit var btnChave: Button
    private lateinit var btnRoupa: Button
    private lateinit var btnArroz: Button
    private lateinit var btnOnibus: Button
    private lateinit var btnTrabalho: Button
    private lateinit var btnVerificar333: Button

    private lateinit var edtCarro: EditText
    private lateinit var edtNoite: EditText
    private lateinit var edtChave: EditText
    private lateinit var edtRoupa: EditText
    private lateinit var edtArroz: EditText
    private lateinit var edtOnibus: EditText
    private lateinit var edtTrabalho: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade3licao3ex3)

        tts = TextToSpeech(this, this)
        tts.setSpeechRate(0.8f)

        txtExplicacao44 = findViewById(R.id.txtExplicacao44)

        btnCarro = findViewById(R.id.btnCarro)
        btnNoite = findViewById(R.id.btnNoite)
        btnChave = findViewById(R.id.btnChave)
        btnRoupa = findViewById(R.id.btnRoupa)
        btnArroz = findViewById(R.id.btnArroz)
        btnOnibus = findViewById(R.id.btnOnibus)
        btnTrabalho = findViewById(R.id.btnTrabalho)

        btnVerificar333 = findViewById(R.id.btnVerificar333)

        edtCarro = findViewById(R.id.edtCarro)
        edtNoite = findViewById(R.id.edtNoite)
        edtChave = findViewById(R.id.edtChave)
        edtRoupa = findViewById(R.id.edtRoupa)
        edtArroz = findViewById(R.id.edtArroz)
        edtOnibus = findViewById(R.id.edtOnibus)
        edtTrabalho = findViewById(R.id.edtTrabalho)

        btnCarro.setOnClickListener { falar("Carro") }
        btnNoite.setOnClickListener { falar("Noite") }
        btnChave.setOnClickListener { falar("Chave") }
        btnRoupa.setOnClickListener { falar("Roupa") }
        btnArroz.setOnClickListener { falar("Arroz") }
        btnOnibus.setOnClickListener { falar("Ônibus") }
        btnTrabalho.setOnClickListener { falar("Trabalho") }

        btnVerificar333.setOnClickListener {
            verificarExercicio()
        }
    }

    private fun verificarExercicio() {
        val textoCarro = edtCarro.text.toString().trim()
        val textoNoite = edtNoite.text.toString().trim()
        val textoChave = edtChave.text.toString().trim()
        val textoRoupa = edtRoupa.text.toString().trim()
        val textoArroz = edtArroz.text.toString().trim()
        val textoOnibus = edtOnibus.text.toString().trim()
        val textoTrabalho = edtTrabalho.text.toString().trim()

        if (textoCarro.isBlank() || textoNoite.isBlank() || textoChave.isBlank() ||
            textoRoupa.isBlank() || textoArroz.isBlank() || textoOnibus.isBlank() ||
            textoTrabalho.isBlank()
        ) {
            Toast.makeText(this, "Preencha todos os campos do exercício", Toast.LENGTH_SHORT).show()
            return
        }

        if (!normalizar(textoCarro).equals("carro", ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(textoNoite).equals("noite", ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(textoChave).equals("chave", ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(textoRoupa).equals("roupa", ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(textoArroz).equals("arroz", ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(textoOnibus).equals("onibus", ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(textoTrabalho).equals("trabalho", ignoreCase = true)) { mostrarErro(); return }

        val prefs = getSharedPreferences("Progresso", MODE_PRIVATE)
        prefs.edit().putInt("U3L3E3_concluido", 1).apply()

        Toast.makeText(this, "Parabéns, suas respostas estão corretas!!!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, ActivityU3L3EX4::class.java))
        finish()
    }

    private fun mostrarErro() {
        Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show()
    }

    private fun normalizar(texto: String): String {
        val semAcento = Normalizer.normalize(texto, Normalizer.Form.NFD)
            .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
        return semAcento.lowercase(Locale.getDefault())
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