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

class ActivityU3L3EX2_2 : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var txtExplicacao322: TextView

    private lateinit var btnPai: Button
    private lateinit var btnMae: Button
    private lateinit var btnPao: Button
    private lateinit var btnDia: Button
    private lateinit var btnRua: Button
    private lateinit var btnSol: Button
    private lateinit var btnVerificar322: Button

    private lateinit var edtPai: EditText
    private lateinit var edtMae: EditText
    private lateinit var edtPao: EditText
    private lateinit var edtDia: EditText
    private lateinit var edtRua: EditText
    private lateinit var edtSol: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade3licao3ex2_2)

        tts = TextToSpeech(this, this)
        tts.setSpeechRate(0.8f)

        txtExplicacao322 = findViewById(R.id.txtExplicacao322)

        btnPai = findViewById(R.id.btnPai)
        btnMae = findViewById(R.id.btnMae)
        btnPao = findViewById(R.id.btnPao)
        btnDia = findViewById(R.id.btnDia)
        btnRua = findViewById(R.id.btnRua)
        btnSol = findViewById(R.id.btnSol)

        btnVerificar322 = findViewById(R.id.btnVerificar322)

        edtPai = findViewById(R.id.edtPai)
        edtMae = findViewById(R.id.edtMae)
        edtPao = findViewById(R.id.edtPao)
        edtDia = findViewById(R.id.edtDia)
        edtRua = findViewById(R.id.edtRua)
        edtSol = findViewById(R.id.edtSol)

        btnPai.setOnClickListener { falar("Pai") }
        btnMae.setOnClickListener { falar("Mãe") }
        btnPao.setOnClickListener { falar("Pão") }
        btnDia.setOnClickListener { falar("Dia") }
        btnRua.setOnClickListener { falar("Rua") }
        btnSol.setOnClickListener { falar("Sol") }

        btnVerificar322.setOnClickListener {
            verificarExercicio3_2_2()
        }
    }

    private fun verificarExercicio3_2_2() {
        val textoPai = edtPai.text.toString().trim()
        val textoMae = edtMae.text.toString().trim()
        val textoPao = edtPao.text.toString().trim()
        val textoDia = edtDia.text.toString().trim()
        val textoRua = edtRua.text.toString().trim()
        val textoSol = edtSol.text.toString().trim()

        if (textoPai.isBlank() || textoMae.isBlank() || textoPao.isBlank() ||
            textoDia.isBlank() || textoRua.isBlank() || textoSol.isBlank()
        ) {
            Toast.makeText(this, "Preencha todos os campos do exercício", Toast.LENGTH_SHORT).show()
            return
        }

        if (!normalizar(textoPai).equals("pai", ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(textoMae).equals("mae", ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(textoPao).equals("pao", ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(textoDia).equals("dia", ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(textoRua).equals("rua", ignoreCase = true)) { mostrarErro(); return }
        if (!normalizar(textoSol).equals("sol", ignoreCase = true)) { mostrarErro(); return }

        val prefs = getSharedPreferences("Progresso", MODE_PRIVATE)
        prefs.edit().putInt("U3L3E2_2_concluido", 1).apply()

        Toast.makeText(this, "Parabéns, suas respostas estão corretas!!!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, ActivityU3L3EX3::class.java))
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