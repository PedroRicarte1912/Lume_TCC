package com.univap.lume.activity

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.univap.lume.R
import java.util.Locale

class ActivityU1L1EX1 : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var txt_explicacao1: TextView
    private lateinit var txt_explicacao2: TextView
    private lateinit var btn_audioA: Button
    private lateinit var btn_audioE: Button
    private lateinit var btn_audioI: Button
    private lateinit var btn_audioO: Button
    private lateinit var btn_audioU: Button
    private lateinit var btn_proxlicao1: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade1licao1ex1)
        tts = TextToSpeech(this, this)
        tts.setSpeechRate(0.8f)

        txt_explicacao1 = findViewById(R.id.txt_explicacao1)
        txt_explicacao2 = findViewById(R.id.txt_explicacao2)
        btn_audioA = findViewById(R.id.btn_audioA)
        btn_audioE = findViewById(R.id.btn_audioE)
        btn_audioI = findViewById(R.id.btn_audioI)
        btn_audioO = findViewById(R.id.btn_audioO)
        btn_audioU = findViewById(R.id.btn_audioU)
        btn_proxlicao1 = findViewById(R.id.btn_proxlicao1)

        btn_audioA.setOnClickListener { falar("A") }
        btn_audioE.setOnClickListener { falar("E") }
        btn_audioI.setOnClickListener { falar("I") }
        btn_audioO.setOnClickListener { falar("O") }
        btn_audioU.setOnClickListener { falar("U") }

        btn_proxlicao1.setOnClickListener {
            val prefs = getSharedPreferences("Progresso", MODE_PRIVATE)
            prefs.edit().putInt("U1L1_concluido", 1).apply()
            startActivity(Intent(this, ActivityU1L1EX2::class.java))
            finish() // ✅ fecha o EX1 para não voltar pra cá
        }
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