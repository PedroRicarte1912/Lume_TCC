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

class ActivityU5L5EX2 : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var txtExplicacaoU5L5EX2: TextView

    private lateinit var btnSapo: Button
    private lateinit var btnElefante: Button
    private lateinit var btnLivro: Button
    private lateinit var btnBorboleta: Button
    private lateinit var btnCachorro: Button
    private lateinit var btnChuva: Button
    private lateinit var btnProximoU5L5EX2: Button

    private lateinit var edtSapo: EditText
    private lateinit var edtElefante: EditText
    private lateinit var edtLivro: EditText
    private lateinit var edtBorboleta: EditText
    private lateinit var edtCachorro: EditText
    private lateinit var edtChuva: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade5licao5ex2)

        tts = TextToSpeech(this, this)
        tts.setSpeechRate(0.8f)

        txtExplicacaoU5L5EX2 = findViewById(R.id.txtExplicacaoU5L5EX2)

        btnSapo = findViewById(R.id.btnSapo)
        btnElefante = findViewById(R.id.btnElefante)
        btnLivro = findViewById(R.id.btnLivro)
        btnBorboleta = findViewById(R.id.btnBorboleta)
        btnCachorro = findViewById(R.id.btnCachorro)
        btnChuva = findViewById(R.id.btnChuva)

        btnProximoU5L5EX2 = findViewById(R.id.btnProximoU5L5EX2)

        edtSapo = findViewById(R.id.edtSapo)
        edtElefante = findViewById(R.id.edtElefante)
        edtLivro = findViewById(R.id.edtLivro)
        edtBorboleta = findViewById(R.id.edtBorboleta)
        edtCachorro = findViewById(R.id.edtCachorro)
        edtChuva = findViewById(R.id.edtChuva)

        btnSapo.setOnClickListener { falar("Sapo") }
        btnElefante.setOnClickListener { falar("Elefante") }
        btnLivro.setOnClickListener { falar("Livro") }
        btnBorboleta.setOnClickListener { falar("Borboleta") }
        btnCachorro.setOnClickListener { falar("Cachorro") }
        btnChuva.setOnClickListener { falar("Chuva") }

        btnProximoU5L5EX2.setOnClickListener {
            verificarExercicio()
        }
    }

    private fun verificarExercicio() {
        val textoSapo = edtSapo.text.toString().trim()
        val textoElefante = edtElefante.text.toString().trim()
        val textoLivro = edtLivro.text.toString().trim()
        val textoBorboleta = edtBorboleta.text.toString().trim()
        val textoCachorro = edtCachorro.text.toString().trim()
        val textoChuva = edtChuva.text.toString().trim()

        if (textoSapo.isBlank() || textoElefante.isBlank() || textoLivro.isBlank() ||
            textoBorboleta.isBlank() || textoCachorro.isBlank() || textoChuva.isBlank()
        ) {
            Toast.makeText(this, "Preencha todos os campos do exercício", Toast.LENGTH_SHORT).show()
            return
        }

        if (!normalizar(textoSapo).equals("sapo", ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(textoElefante).equals("elefante", ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(textoLivro).equals("livro", ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(textoBorboleta).equals("borboleta", ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(textoCachorro).equals("cachorro", ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(textoChuva).equals("chuva", ignoreCase = true)) { mostrarErro(); return }

        val prefs = getSharedPreferences("Progresso", MODE_PRIVATE)
        prefs.edit().putInt("U5L5E2_concluido", 1).apply()

        Toast.makeText(this, "Parabéns, suas respostas estão corretas!!!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, ActivityU5L5EX3::class.java))
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