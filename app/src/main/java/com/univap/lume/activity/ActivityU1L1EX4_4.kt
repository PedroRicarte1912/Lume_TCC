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

class ActivityU1L1EX4_4 : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var txt_explicacao12: TextView
    private lateinit var txt_explicacao13: TextView
    private lateinit var txt_explicacao14: TextView

    private lateinit var btn_hiatoAE: Button
    private lateinit var btn_hiatoAI: Button
    private lateinit var btn_hiatoOE: Button
    private lateinit var btn_hiatoUA: Button
    private lateinit var btn_hiatoIE: Button

    private lateinit var edt_hiatoAE: EditText
    private lateinit var edt_hiatoAI: EditText
    private lateinit var edt_hiatoOE: EditText
    private lateinit var edt_hiatoUA: EditText
    private lateinit var edt_hiatoIE: EditText

    private lateinit var btn_proxunidade1: Button
    private lateinit var btn_aboutlicoes: Button
    private lateinit var btn_stoplicoes: Button

    private lateinit var tts: TextToSpeech
    private var ttsReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade1licao1ex4_4)

        tts = TextToSpeech(this, this)

        txt_explicacao12 = findViewById(R.id.txt_explicacao12)
        txt_explicacao13 = findViewById(R.id.txt_explicacao13)
        txt_explicacao14 = findViewById(R.id.txt_explicacao14)

        btn_hiatoAE = findViewById(R.id.btn_hiatoAE)
        btn_hiatoAI = findViewById(R.id.btn_hiatoAI)
        btn_hiatoOE = findViewById(R.id.btn_hiatoOE)
        btn_hiatoUA = findViewById(R.id.btn_hiatoUA)
        btn_hiatoIE = findViewById(R.id.btn_hiatoIE)

        edt_hiatoAE = findViewById(R.id.edt_hiatoAE)
        edt_hiatoAI = findViewById(R.id.edt_hiatoAI)
        edt_hiatoOE = findViewById(R.id.edt_hiatoOE)
        edt_hiatoUA = findViewById(R.id.edt_hiatoUA)
        edt_hiatoIE = findViewById(R.id.edt_hiatoIE)

        btn_proxunidade1 = findViewById(R.id.btn_proxunidade1)
        btn_aboutlicoes  = findViewById(R.id.btn_aboutlicoes)
        btn_stoplicoes   = findViewById(R.id.btn_stoplicoes)

        btn_hiatoAE.setOnClickListener { falar("AE") }
        btn_hiatoAI.setOnClickListener { falar("AI") }
        btn_hiatoOE.setOnClickListener { falar("OE") }
        btn_hiatoUA.setOnClickListener { falar("UA") }
        btn_hiatoIE.setOnClickListener { falar("IE") }

        btn_proxunidade1.setOnClickListener { Exercicio4_4() }

        // Abre a tela de about e volta para este exercício automaticamente
        btn_aboutlicoes.setOnClickListener {
            val intent = Intent(this, ActivityAboutLicoes::class.java)
            // ActivityAboutLicoes deve apenas chamar finish() para voltar aqui
            startActivity(intent)
        }

        // Para a lição: vai para a tela de confirmação e depois para MainActivity
        btn_stoplicoes.setOnClickListener {
            val intent = Intent(this, ActivityStopLicoesAbout::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    private fun falar(texto: String) {
        if (!ttsReady) return
        tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(
                Locale.Builder().setLanguage("pt").setRegion("BR").build()
            )
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) return
            ttsReady = true
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }

    private fun Exercicio4_4() {
        val textAE = edt_hiatoAE.text.toString().trim()
        val textAI = edt_hiatoAI.text.toString().trim()
        val textOE = edt_hiatoOE.text.toString().trim()
        val textUA = edt_hiatoUA.text.toString().trim()
        val textIE = edt_hiatoIE.text.toString().trim()

        if (textAE.isBlank() || textIE.isBlank() || textUA.isBlank() ||
            textOE.isBlank() || textAI.isBlank()
        ) {
            Toast.makeText(this, "Preencha todos os campos do exercício", Toast.LENGTH_SHORT).show()
            return
        }

        fun erro() {
            Toast.makeText(this, "Insira o hiato correto", Toast.LENGTH_SHORT).show()
        }

        if (textAE.lowercase() != "ae") return erro()
        if (textIE.lowercase() != "ie") return erro()
        if (textOE.lowercase() != "oe") return erro()
        if (textUA.lowercase() != "ua") return erro()
        if (textAI.lowercase() != "ai") return erro()

        val prefs = getSharedPreferences("Progresso", MODE_PRIVATE)
        prefs.edit().putInt("U1L4_concluido", 1).apply()

        val lumeSettings = getSharedPreferences("LumeSettings", MODE_PRIVATE)
        if (!lumeSettings.contains("AVATAR_KEY")) {
            lumeSettings.edit().putString("AVATAR_KEY", "avatar12").apply()
        }

        Toast.makeText(this, "Parabéns, suas respostas estão corretas!!!", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, ActivityParabens::class.java).apply {
            putExtra(ActivityParabens.EXTRA_UNIDADE_CONCLUIDA, 1)
        }
        startActivity(intent)
        finish()
    }
}