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

class ActivityU1L1EX4 : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var txt_explicacao5: TextView
    private lateinit var edt_silabaIA: EditText
    private lateinit var edt_silabaIE: EditText
    private lateinit var edt_silabaIO: EditText
    private lateinit var edt_silabaUA: EditText
    private lateinit var btn_silabaIA: Button
    private lateinit var btn_silabaIE: Button
    private lateinit var btn_silabaIO: Button
    private lateinit var btn_silabaUA: Button
    private lateinit var btn_proxlicao4_1: Button
    private lateinit var btn_aboutlicoes: Button
    private lateinit var btn_stoplicoes: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade1licao1ex4)
        tts = TextToSpeech(this, this)

        txt_explicacao5     = findViewById(R.id.txt_explicacao5)
        edt_silabaIA        = findViewById(R.id.edt_silabaIA)
        edt_silabaIE        = findViewById(R.id.edt_silabaIE)
        edt_silabaIO        = findViewById(R.id.edt_silabaIO)
        edt_silabaUA        = findViewById(R.id.edt_silabaUA)
        btn_silabaIA        = findViewById(R.id.btn_silabaIA)
        btn_silabaIE        = findViewById(R.id.btn_silabaIE)
        btn_silabaIO        = findViewById(R.id.btn_silabaIO)
        btn_silabaUA        = findViewById(R.id.btn_silabaUA)
        btn_proxlicao4_1    = findViewById(R.id.btn_proxlicao4_1)
        btn_aboutlicoes     = findViewById(R.id.btn_aboutlicoes)
        btn_stoplicoes      = findViewById(R.id.btn_stoplicoes)

        btn_silabaIA.setOnClickListener { falar("IA") }
        btn_silabaIE.setOnClickListener { falar("IE") }
        btn_silabaIO.setOnClickListener { falar("IO") }
        btn_silabaUA.setOnClickListener { falar("UA") }

        btn_proxlicao4_1.setOnClickListener { verificarExercicio() }

        // Abre o about e volta para cá automaticamente via finish()
        btn_aboutlicoes.setOnClickListener {
            startActivity(Intent(this, ActivityAboutLicoes::class.java))
        }

        // Para a lição e limpa o stack até chegar na MainActivity
        btn_stoplicoes.setOnClickListener {
            startActivity(
                Intent(this, ActivityStopLicoesAbout::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }
    }

    private fun verificarExercicio() {
        val textoIA = edt_silabaIA.text.toString().trim()
        val textoIE = edt_silabaIE.text.toString().trim()
        val textoIO = edt_silabaIO.text.toString().trim()
        val textoUA = edt_silabaUA.text.toString().trim()

        if (textoIA.isBlank() || textoIE.isBlank() || textoIO.isBlank() || textoUA.isBlank()) {
            Toast.makeText(this, "Preencha todos os campos do exercício", Toast.LENGTH_SHORT).show()
            return
        }

        val mensagemErro = "Insira a letra correta (maiúscula ou minúscula)"

        if (textoIA.lowercase() != "ia") { Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT).show(); return }
        if (textoIE.lowercase() != "ie") { Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT).show(); return }
        if (textoIO.lowercase() != "io") { Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT).show(); return }
        if (textoUA.lowercase() != "ua") { Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT).show(); return }

        Toast.makeText(this, "Parabéns, suas respostas estão corretas!!!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, ActivityU1L1EX4_2::class.java))
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
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) return
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