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

class ActivityU3L3EX3_2 : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var txtExplicacao33: TextView

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

        txtExplicacao33 = findViewById(R.id.txtExplicacao44)

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

        val carro = edtCarro.text.toString().trim().lowercase()
        val noite = edtNoite.text.toString().trim().lowercase()
        val chave = edtChave.text.toString().trim().lowercase()
        val roupa = edtRoupa.text.toString().trim().lowercase()
        val arroz = edtArroz.text.toString().trim().lowercase()

        val onibus = edtOnibus.text.toString()
            .trim()
            .lowercase()
            .replace("ô", "o") // evita erro de acento

        val trabalho = edtTrabalho.text.toString().trim().lowercase()

        if (carro.isBlank() || noite.isBlank() || chave.isBlank() ||
            roupa.isBlank() || arroz.isBlank() || onibus.isBlank() || trabalho.isBlank()
        ) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (carro != "carro") return erro("CARRO")
        if (noite != "noite") return erro("NOITE")
        if (chave != "chave") return erro("CHAVE")
        if (roupa != "roupa") return erro("ROUPA")
        if (arroz != "arroz") return erro("ARROZ")
        if (onibus != "onibus") return erro("ÔNIBUS")
        if (trabalho != "trabalho") return erro("TRABALHO")

        Toast.makeText(this, "Parabéns! Tudo correto!", Toast.LENGTH_SHORT).show()

        startActivity(Intent(this, ActivityU3L3EX4::class.java))
        finish()
    }

    private fun erro(palavra: String) {
        Toast.makeText(this, "$palavra incorreto", Toast.LENGTH_SHORT).show()
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