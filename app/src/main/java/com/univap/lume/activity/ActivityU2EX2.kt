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


class ActivityU2EX2 : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech

    private lateinit var txtExplicacao: TextView

    private lateinit var btnVoltar: Button
    private lateinit var btnProxima: Button

    // Row 1 - Agudos
    private lateinit var btnAgudo1: Button  // Á - Água
    private lateinit var btnAgudo2: Button  // É - Remédio
    private lateinit var btnAgudo3: Button  // Ó - Avó

    // Row 2 - Circunflexos
    private lateinit var btnCirc1: Button   // Ê - Você
    private lateinit var btnCirc2: Button   // Ô - Avô
    private lateinit var btnCirc3: Button   // Â - Ânimo

    // Row 3 - Til e Cedilha
    private lateinit var btnTil1: Button    // Ã - Irmã
    private lateinit var btnTil2: Button    // Õ - Limões
    private lateinit var btnCedilha: Button // Ç - Preço

    // Row 4 - Extras
    private lateinit var btnEx1: Button     // Ú - Saúde
    private lateinit var btnEx2: Button     // Í - País
    private lateinit var btnEx3: Button     // Ó - História

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade2_1)

        tts = TextToSpeech(this, this)
        tts.setSpeechRate(0.8f)

        txtExplicacao = findViewById(R.id.txt_explicacao21)

        btnVoltar  = findViewById(R.id.btnVoltar)
        btnProxima = findViewById(R.id.btnproxUnidade3)

        btnAgudo1  = findViewById(R.id.btnAgudo1)
        btnAgudo2  = findViewById(R.id.btnAgudo2)
        btnAgudo3  = findViewById(R.id.btnAgudo3)

        btnCirc1   = findViewById(R.id.btnCirc1)
        btnCirc2   = findViewById(R.id.btnCirc2)
        btnCirc3   = findViewById(R.id.btnCirc3)

        btnTil1    = findViewById(R.id.btnTil1)
        btnTil2    = findViewById(R.id.btnTil2)
        btnCedilha = findViewById(R.id.btnCedilha)

        btnEx1     = findViewById(R.id.btnEx1)
        btnEx2     = findViewById(R.id.btnEx2)
        btnEx3     = findViewById(R.id.btnEx3)

        // Row 1
        btnAgudo1.setOnClickListener { falar("Á. Exemplo: Água") }
        btnAgudo2.setOnClickListener { falar("É. Exemplo: Remédio") }
        btnAgudo3.setOnClickListener { falar("Ó. Exemplo: Avó") }

        // Row 2
        btnCirc1.setOnClickListener { falar("Ê. Exemplo: Você") }
        btnCirc2.setOnClickListener { falar("Ô. Exemplo: Avô") }
        btnCirc3.setOnClickListener { falar("Â. Exemplo: Ânimo") }

        // Row 3
        btnTil1.setOnClickListener    { falar("Ã. Exemplo: Irmã") }
        btnTil2.setOnClickListener    { falar("Õ. Exemplo: Limões") }
        btnCedilha.setOnClickListener { falar("Ç. Exemplo: Preço") }

        // Row 4
        btnEx1.setOnClickListener { falar("Ú. Exemplo: Saúde") }
        btnEx2.setOnClickListener { falar("Í. Exemplo: País") }
        btnEx3.setOnClickListener { falar("Ó. Exemplo: História") }

        btnVoltar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btnProxima.setOnClickListener {
            val prefs = getSharedPreferences("Progresso", MODE_PRIVATE)
            prefs.edit()
                .putInt("U2EXAcentuacao_concluido", 1)
                .apply()

            val intent = Intent(this, ActivityParabens::class.java).apply {
                putExtra(ActivityParabens.EXTRA_UNIDADE_CONCLUIDA, 2)
            }
            startActivity(intent)
            finish()
        }
    }

    private fun falar(texto: String) {
        tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(
                Locale.Builder()
                    .setLanguage("pt")
                    .setRegion("BR")
                    .build()
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