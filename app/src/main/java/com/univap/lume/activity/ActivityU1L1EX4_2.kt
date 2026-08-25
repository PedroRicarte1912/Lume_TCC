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
import android.os.Handler
import android.os.Looper

class ActivityU1L1EX4_2 : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var txt_explicacao7: TextView
    private lateinit var txt_explicacao8: TextView

    private lateinit var edt_silabaAI: EditText
    private lateinit var edt_silabaEI: EditText
    private lateinit var edt_silabaOI: EditText
    private lateinit var edt_silabaAU: EditText
    private lateinit var edt_silabaEU: EditText
    private lateinit var edt_silabaOU: EditText

    private lateinit var btn_silabaAI: Button
    private lateinit var btn_silabaEI: Button
    private lateinit var btn_silabaOI: Button
    private lateinit var btn_silabaAU: Button
    private lateinit var btn_silabaEU: Button
    private lateinit var btn_silabaOU: Button

    private lateinit var btn_proxlicao4_2: Button
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade1licao1ex4_2)

        tts = TextToSpeech(this, this)

        txt_explicacao7 = findViewById(R.id.txt_explicacao7)
        txt_explicacao8 = findViewById(R.id.txt_explicacao8)

        edt_silabaAI = findViewById(R.id.edt_silabaAI)
        edt_silabaEI = findViewById(R.id.edt_silabaEI)
        edt_silabaOI = findViewById(R.id.edt_silabaOI)
        edt_silabaAU = findViewById(R.id.edt_silabaAU)
        edt_silabaEU = findViewById(R.id.edt_silabaEU)
        edt_silabaOU = findViewById(R.id.edt_silabaOU)

        btn_silabaAI = findViewById(R.id.btn_silabaAI)
        btn_silabaEI = findViewById(R.id.btn_silabaEI)
        btn_silabaOI = findViewById(R.id.btn_silabaOI)
        btn_silabaAU = findViewById(R.id.btn_silabaAU)
        btn_silabaEU = findViewById(R.id.btn_silabaEU)
        btn_silabaOU = findViewById(R.id.btn_silabaOU)

        btn_proxlicao4_2 = findViewById(R.id.btn_proxlicao4_2)

        btn_silabaAI.setOnClickListener { falar("AI") }
        btn_silabaEI.setOnClickListener { falar("EI") }
        btn_silabaOI.setOnClickListener { falar("OI") }
        btn_silabaAU.setOnClickListener { falar("AU") }
        btn_silabaEU.setOnClickListener { falar("EU") }
        btn_silabaOU.setOnClickListener { falar("OU") }

        btn_proxlicao4_2.setOnClickListener {
            Exercicio4_2()
        }
    }

    private fun Exercicio4_2() {

        val textAI = edt_silabaAI.text.toString().trim()
        val textEI = edt_silabaEI.text.toString().trim()
        val textEU = edt_silabaEU.text.toString().trim()
        val textAU = edt_silabaAU.text.toString().trim()
        val textOI = edt_silabaOI.text.toString().trim()
        val textOU = edt_silabaOU.text.toString().trim()

        if (textAI.isBlank() || textEI.isBlank() || textEU.isBlank() ||
            textAU.isBlank() || textOI.isBlank() || textOU.isBlank()
        ) {
            Toast.makeText(this, "Preencha todos os campos do exercício", Toast.LENGTH_SHORT).show()
            return
        }

        val mensagemErro = "Insira a sílaba correta (maiúscula ou minúscula)"

        if (textAI != "AI" && textAI != "ai") {
            Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT).show(); return
        }
        if (textEI != "EI" && textEI != "ei") {
            Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT).show(); return
        }
        if (textEU != "EU" && textEU != "eu") {
            Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT).show(); return
        }
        if (textOI != "OI" && textOI != "oi") {
            Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT).show(); return
        }
        if (textOU != "OU" && textOU != "ou") {
            Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT).show(); return
        }
        if (textAU != "AU" && textAU != "au") {
            Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT).show(); return
        }

        Toast.makeText(this, "Parabéns, suas respostas estão corretas!!!", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, ActivityU1L1EX4_3::class.java)) // ✅ CORRIGIDO
            finish()
        }, 1500)
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