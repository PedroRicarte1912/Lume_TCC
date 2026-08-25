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

class ActivityU4L4EX1 : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech

    private lateinit var txtExplicacao44: TextView
    private lateinit var btnGeladeira: Button
    private lateinit var btnPassarinho: Button
    private lateinit var btnPresentear: Button
    private lateinit var btnUniverso: Button
    private lateinit var btnChocolate: Button
    private lateinit var btnBrincadeira: Button
    private lateinit var btnAnimado: Button
    private lateinit var btnVerificar3: Button

    private lateinit var edtGeladeira: EditText
    private lateinit var edtPassarinho: EditText
    private lateinit var edtPresentear: EditText
    private lateinit var edtUniverso: EditText
    private lateinit var edtChocolate: EditText
    private lateinit var edtBrincadeira: EditText
    private lateinit var edtAnimado: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade4licao4)

        tts = TextToSpeech(this, this)
        tts.setSpeechRate(0.8f)

        txtExplicacao44 = findViewById(R.id.txtExplicacao44)

        btnGeladeira   = findViewById(R.id.btnGeladeira)
        btnPassarinho  = findViewById(R.id.btnPassarinho)
        btnPresentear  = findViewById(R.id.btnPresentear)
        btnUniverso    = findViewById(R.id.btnUniverso)
        btnChocolate   = findViewById(R.id.btnChocolate)
        btnBrincadeira = findViewById(R.id.btnBrincadeira)
        btnAnimado     = findViewById(R.id.btnAnimado)
        btnVerificar3  = findViewById(R.id.btnVerificar3)

        edtGeladeira   = findViewById(R.id.edtGeladeira)
        edtPassarinho  = findViewById(R.id.edtPassarinho)
        edtPresentear  = findViewById(R.id.edtPresentear)
        edtUniverso    = findViewById(R.id.edtUniverso)
        edtChocolate   = findViewById(R.id.edtChocolate)
        edtBrincadeira = findViewById(R.id.edtBrincadeira)
        edtAnimado     = findViewById(R.id.edtAnimado)

        btnGeladeira.setOnClickListener   { falar("Geladeira") }
        btnPassarinho.setOnClickListener  { falar("Passarinho") }
        btnPresentear.setOnClickListener  { falar("Presentear") }
        btnUniverso.setOnClickListener    { falar("Universo") }
        btnChocolate.setOnClickListener   { falar("Chocolate") }
        btnBrincadeira.setOnClickListener { falar("Brincadeira") }
        btnAnimado.setOnClickListener     { falar("Animado") }

        btnVerificar3.setOnClickListener {
            verificarExercicio()
        }
    }

    private fun verificarExercicio() {

        val textoGeladeira   = edtGeladeira.text.toString().trim()
        val textoPassarinho  = edtPassarinho.text.toString().trim()
        val textoPresentear  = edtPresentear.text.toString().trim()
        val textoUniverso    = edtUniverso.text.toString().trim()
        val textoChocolate   = edtChocolate.text.toString().trim()
        val textoBrincadeira = edtBrincadeira.text.toString().trim()
        val textoAnimado     = edtAnimado.text.toString().trim()


        if (textoGeladeira.lowercase() != "geladeira") {
            Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show()
            return
        }

        if (textoPassarinho.lowercase() != "passarinho") {
            Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show()
            return
        }

        if (textoPresentear.lowercase() != "presentear") {
            Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show()
            return
        }

        if (textoUniverso.lowercase() != "universo") {
            Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show()
            return
        }

        if (textoChocolate.lowercase() != "chocolate") {
            Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show()
            return
        }

        if (textoBrincadeira.lowercase() != "brincadeira") {
            Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show()
            return
        }

        if (textoAnimado.lowercase() != "animado") {
            Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(this, "Parabéns, suas respostas estão corretas!!!", Toast.LENGTH_SHORT).show()

        startActivity(Intent(this, ActivityU4L4EX2::class.java))
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