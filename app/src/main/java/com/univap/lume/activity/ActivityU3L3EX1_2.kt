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
import java.util.Locale

class ActivityU3L3EX1_2 : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech

    private lateinit var txtExplicacao312: TextView

    private lateinit var btnSuco: Button
    private lateinit var btnVovo: Button
    private lateinit var btnCafe: Button
    private lateinit var btnGato: Button
    private lateinit var btnVerificar: Button

    private lateinit var edtSuco: EditText
    private lateinit var edtVovo: EditText
    private lateinit var edtCafe: EditText
    private lateinit var edtGato: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade3licao3ex1_2) // ✅ correto

        tts = TextToSpeech(this, this)
        tts.setSpeechRate(0.8f)

        txtExplicacao312 = findViewById(R.id.txtExplicacao312)

        btnSuco = findViewById(R.id.btnSuco)
        btnVovo = findViewById(R.id.btnVovo)
        btnCafe = findViewById(R.id.btnCafe)
        btnGato = findViewById(R.id.btnGato)
        btnVerificar = findViewById(R.id.btnVerificar312)

        edtSuco = findViewById(R.id.edtSuco)
        edtVovo = findViewById(R.id.edtVovo)
        edtCafe = findViewById(R.id.edtCafe)
        edtGato = findViewById(R.id.edtGato)

        btnSuco.setOnClickListener { falar("Suco") }
        btnVovo.setOnClickListener { falar("Vovó") }
        btnCafe.setOnClickListener { falar("Café") }
        btnGato.setOnClickListener { falar("Gato") }

        btnVerificar.setOnClickListener {
            verificarExercicio1_2()
        }
    }

    private fun verificarExercicio1_2() {

        val textoSuco = edtSuco.text.toString().trim()
        val textoVovo = edtVovo.text.toString().trim()
        val textoCafe = edtCafe.text.toString().trim()
        val textoGato = edtGato.text.toString().trim()

        if (textoSuco.isBlank() || textoVovo.isBlank() || textoCafe.isBlank() || textoGato.isBlank()) {
            Toast.makeText(this, "Preencha todos os campos do exercício", Toast.LENGTH_SHORT).show()
            return
        }

        if (textoSuco.lowercase() != "suco") {
            Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show()
            return
        }

        if (textoVovo.lowercase() != "vovo" && textoVovo.lowercase() != "vovó") {
            Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show()
            return
        }

        if (textoCafe.lowercase() != "cafe" && textoCafe.lowercase() != "café") {
            Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show()
            return
        }

        if (textoGato.lowercase() != "gato") {
            Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(this, "Parabéns, suas respostas estão corretas!!!", Toast.LENGTH_SHORT).show()

        startActivity(Intent(this, ActivityU3L3EX2::class.java)) // ✅ corrigido: vai para EX2
        finish()
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