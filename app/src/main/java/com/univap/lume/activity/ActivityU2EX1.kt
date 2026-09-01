package com.univap.lume.activity

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.univap.lume.MainActivity
import com.univap.lume.R
import java.util.Locale


class ActivityU2EX1 : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech

    private lateinit var txtExplicacao21: TextView
    private lateinit var btnVoltar: Button
    private lateinit var btnProxEtapa2: Button

    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button
    private lateinit var btnD: Button
    private lateinit var btnE: Button
    private lateinit var btnF: Button
    private lateinit var btnG: Button
    private lateinit var btnH: Button
    private lateinit var btnI: Button
    private lateinit var btnJ: Button
    private lateinit var btnK: Button
    private lateinit var btnL: Button
    private lateinit var btnM: Button
    private lateinit var btnN: Button
    private lateinit var btnO: Button
    private lateinit var btnP: Button
    private lateinit var btnQ: Button
    private lateinit var btnR: Button
    private lateinit var btnS: Button
    private lateinit var btnT: Button
    private lateinit var btnU: Button
    private lateinit var btnV: Button
    private lateinit var btnW: Button
    private lateinit var btnX: Button
    private lateinit var btnY: Button
    private lateinit var btnZ: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade2)

        tts = TextToSpeech(this, this)
        tts.setSpeechRate(0.8f)

        txtExplicacao21 = findViewById(R.id.txt_explicacao21)

        btnVoltar    = findViewById(R.id.btnVoltar)
        btnProxEtapa2 = findViewById(R.id.btnproxEtapa2)

        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)
        btnD = findViewById(R.id.btnD)
        btnE = findViewById(R.id.btnE)
        btnF = findViewById(R.id.btnF)
        btnG = findViewById(R.id.btnG)
        btnH = findViewById(R.id.btnH)
        btnI = findViewById(R.id.btnI)
        btnJ = findViewById(R.id.btnJ)
        btnK = findViewById(R.id.btnK)
        btnL = findViewById(R.id.btnL)
        btnM = findViewById(R.id.btnM)
        btnN = findViewById(R.id.btnN)
        btnO = findViewById(R.id.btnO)
        btnP = findViewById(R.id.btnP)
        btnQ = findViewById(R.id.btnQ)
        btnR = findViewById(R.id.btnR)
        btnS = findViewById(R.id.btnS)
        btnT = findViewById(R.id.btnT)
        btnU = findViewById(R.id.btnU)
        btnV = findViewById(R.id.btnV)
        btnW = findViewById(R.id.btnW)
        btnX = findViewById(R.id.btnX)
        btnY = findViewById(R.id.btnY)
        btnZ = findViewById(R.id.btnZ)

        btnA.setOnClickListener { falar("A") }
        btnB.setOnClickListener { falar("B") }
        btnC.setOnClickListener { falar("C") }
        btnD.setOnClickListener { falar("D") }
        btnE.setOnClickListener { falar("E") }
        btnF.setOnClickListener { falar("F") }
        btnG.setOnClickListener { falar("G") }
        btnH.setOnClickListener { falar("H") }
        btnI.setOnClickListener { falar("I") }
        btnJ.setOnClickListener { falar("J") }
        btnK.setOnClickListener { falar("K") }
        btnL.setOnClickListener { falar("L") }
        btnM.setOnClickListener { falar("M") }
        btnN.setOnClickListener { falar("N") }
        btnO.setOnClickListener { falar("O") }
        btnP.setOnClickListener { falar("P") }
        btnQ.setOnClickListener { falar("Q") }
        btnR.setOnClickListener { falar("R") }
        btnS.setOnClickListener { falar("S") }
        btnT.setOnClickListener { falar("T") }
        btnU.setOnClickListener { falar("U") }
        btnV.setOnClickListener { falar("V") }
        btnW.setOnClickListener { falar("W") }
        btnX.setOnClickListener { falar("X") }
        btnY.setOnClickListener { falar("Y") }
        btnZ.setOnClickListener { falar("Z") }

        btnVoltar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btnProxEtapa2.setOnClickListener {
            val prefs = getSharedPreferences("Progresso", MODE_PRIVATE)
            prefs.edit()
                .putInt("U2L1_concluido", 1)
                .apply()

            startActivity(Intent(this, ActivityU2EX2::class.java))
            finish()
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
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {
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