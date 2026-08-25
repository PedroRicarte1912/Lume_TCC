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

class ActivityU3L3EX2 : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var txtExplicacao3: TextView

    private lateinit var btnBatata: Button
    private lateinit var btnBanana: Button
    private lateinit var btnSacola: Button
    private lateinit var btnCaneta: Button
    private lateinit var btnMenino: Button
    private lateinit var btnPanela: Button
    private lateinit var btnVerificar3: Button

    private lateinit var edtBanana: EditText
    private lateinit var edtBatata: EditText
    private lateinit var edtSacola: EditText
    private lateinit var edtCaneta: EditText
    private lateinit var edtMenino: EditText
    private lateinit var edtPanela: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade3licao3ex2)

        tts = TextToSpeech(this, this)
        tts.setSpeechRate(0.8f)

        txtExplicacao3 = findViewById(R.id.txtExplicacao44)

        btnBanana = findViewById(R.id.btnBanana)
        btnBatata = findViewById(R.id.btnBatata)
        btnSacola = findViewById(R.id.btnSacola)
        btnCaneta = findViewById(R.id.btnCaneta)
        btnMenino = findViewById(R.id.btnMenino)
        btnPanela = findViewById(R.id.btnPanela)

        btnVerificar3 = findViewById(R.id.btnVerificar3)

        edtBanana = findViewById(R.id.edtBanana)
        edtBatata = findViewById(R.id.edtBatata)
        edtSacola = findViewById(R.id.edtSacola)
        edtCaneta = findViewById(R.id.edtCaneta)
        edtMenino = findViewById(R.id.edtMenino)
        edtPanela = findViewById(R.id.edtPanela)

        btnBatata.setOnClickListener { falar("Batata") }
        btnBanana.setOnClickListener { falar("Banana") }
        btnSacola.setOnClickListener { falar("Sacola") }
        btnCaneta.setOnClickListener { falar("Caneta") }
        btnMenino.setOnClickListener { falar("Menino") }
        btnPanela.setOnClickListener { falar("Panela") }

        btnVerificar3.setOnClickListener {
            verificarExercicio3_2()
        }
    }

    private fun verificarExercicio3_2() {
        val textoBatata = edtBatata.text.toString().trim()
        val textoBanana = edtBanana.text.toString().trim()
        val textoSacola = edtSacola.text.toString().trim()
        val textoCaneta = edtCaneta.text.toString().trim()
        val textoMenino = edtMenino.text.toString().trim()
        val textoPanela = edtPanela.text.toString().trim()

        if (textoBatata.isBlank() || textoBanana.isBlank() || textoSacola.isBlank() ||
            textoCaneta.isBlank() || textoMenino.isBlank() || textoPanela.isBlank()
        ) {
            Toast.makeText(this, "Preencha todos os campos do exercício", Toast.LENGTH_SHORT).show()
            return
        }

        if (textoBatata.lowercase() != "batata") { Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show(); return }
        if (textoBanana.lowercase() != "banana") { Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show(); return }
        if (textoSacola.lowercase() != "sacola") { Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show(); return }
        if (textoCaneta.lowercase() != "caneta") { Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show(); return }
        if (textoMenino.lowercase() != "menino") { Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show(); return }
        if (textoPanela.lowercase() != "panela") { Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show(); return }

        val prefs = getSharedPreferences("Progresso", MODE_PRIVATE)
        prefs.edit().putInt("U3L3E2_concluido", 1).apply()

        Toast.makeText(this, "Parabéns, suas respostas estão corretas!!!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, ActivityU3L3EX2_2::class.java))
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