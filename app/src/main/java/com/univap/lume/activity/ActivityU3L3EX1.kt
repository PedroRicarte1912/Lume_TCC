package com.univap.lume.activity

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.univap.lume.R
import java.util.Locale

class ActivityU3L3EX1 : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech

    private lateinit var btnBolo: Button
    private lateinit var btnCama: Button
    private lateinit var btnCasa: Button
    private lateinit var btnMesa: Button
    private lateinit var btnVerificar: Button

    private lateinit var edtBolo: EditText
    private lateinit var edtCama: EditText
    private lateinit var edtCasa: EditText
    private lateinit var edtMesa: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade3licao3)

        tts = TextToSpeech(this, this)
        tts.setSpeechRate(0.8f)

        btnBolo = findViewById(R.id.btnBolo)
        btnCama = findViewById(R.id.btnCama)
        btnCasa = findViewById(R.id.btnCasa)
        btnMesa = findViewById(R.id.btnMesa)
        btnVerificar = findViewById(R.id.btnVerificar333)

        edtBolo = findViewById(R.id.edtBolo)
        edtCama = findViewById(R.id.edtCama)
        edtCasa = findViewById(R.id.edtCasa)
        edtMesa = findViewById(R.id.edtMesa)

        btnBolo.setOnClickListener {
            falar("Bolo")
        }

        btnCama.setOnClickListener {
            falar("Cama")
        }

        btnCasa.setOnClickListener {
            falar("Casa")
        }

        btnMesa.setOnClickListener {
            falar("Mesa")
        }

        btnVerificar.setOnClickListener {
            Exercicio1()
        }
    }

    private fun Exercicio1() {

        val textoBolo = edtBolo.text.toString().trim()
        val textoCama = edtCama.text.toString().trim()
        val textoCasa = edtCasa.text.toString().trim()
        val textoMesa = edtMesa.text.toString().trim()

        if (
            textoBolo.isBlank() ||
            textoCama.isBlank() ||
            textoCasa.isBlank() ||
            textoMesa.isBlank()
        ) {
            Toast.makeText(
                this,
                "Preencha todos os campos do exercício",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (textoBolo.lowercase() != "bolo") {
            Toast.makeText(
                this,
                "Palavra(s) incorreta(s)",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (textoCama.lowercase() != "cama") {
            Toast.makeText(
                this,
                "Palavra(s) incorreta(s)",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (textoCasa.lowercase() != "casa") {
            Toast.makeText(
                this,
                "Palavra(s) incorreta(s)",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (textoMesa.lowercase() != "mesa") {
            Toast.makeText(
                this,
                "Palavra(s) incorreta(s)",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        Toast.makeText(
            this,
            "Parabéns, suas respostas estão corretas!!!",
            Toast.LENGTH_SHORT
        ).show()

        val tela = Intent(
            this,
            ActivityU3L3EX1_2::class.java
        )

        startActivity(tela)
        finish()
    }

    private fun falar(texto: String) {
        tts.speak(
            texto,
            TextToSpeech.QUEUE_FLUSH,
            null,
            ""
        )
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