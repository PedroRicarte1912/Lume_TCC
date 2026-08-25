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

class ActivityU4L4EX3 : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech

    private lateinit var txtExplicacao35: TextView

    private lateinit var btnOportunidade: Button
    private lateinit var btnResponsabilidade: Button
    private lateinit var btnAprendizado: Button
    private lateinit var btnGenerosidade: Button
    private lateinit var btnAcordamento: Button
    private lateinit var btnAniversariante: Button
    private lateinit var btnEstacionamento: Button
    private lateinit var btnVerificar5: Button
    private lateinit var btn_aboutlicoes: Button
    private lateinit var btn_stoplicoes: Button

    private lateinit var edtOportunidade: EditText
    private lateinit var edtResponsabilidade: EditText
    private lateinit var edtAprendizado: EditText
    private lateinit var edtGenerosidade: EditText
    private lateinit var edtAcordamento: EditText
    private lateinit var edtAniversariante: EditText
    private lateinit var edtEstacionamento: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade4licao4ex3)

        tts = TextToSpeech(this, this)
        tts.setSpeechRate(0.8f)

        txtExplicacao35      = findViewById(R.id.txtExplicacao35)
        btnOportunidade      = findViewById(R.id.btnOportunidade)
        btnResponsabilidade  = findViewById(R.id.btnResponsabilidade)
        btnAprendizado       = findViewById(R.id.btnAprendizado)
        btnGenerosidade      = findViewById(R.id.btnGenerosidade)
        btnAcordamento       = findViewById(R.id.btnAcordamento)
        btnAniversariante    = findViewById(R.id.btnAniversariante)
        btnEstacionamento    = findViewById(R.id.btnEstacionamento)
        btnVerificar5        = findViewById(R.id.btnVerificar5)
        btn_aboutlicoes      = findViewById(R.id.btn_aboutlicoes)
        btn_stoplicoes       = findViewById(R.id.btn_stoplicoes)

        edtOportunidade      = findViewById(R.id.edtOportunidade)
        edtResponsabilidade  = findViewById(R.id.edtResponsabilidade)
        edtAprendizado       = findViewById(R.id.edtAprendizado)
        edtGenerosidade      = findViewById(R.id.edtGenerosidade)
        edtAcordamento       = findViewById(R.id.edtAcordamento)
        edtAniversariante    = findViewById(R.id.edtAniversariante)
        edtEstacionamento    = findViewById(R.id.edtEstacionamento)

        btnOportunidade.setOnClickListener     { falar("Oportunidade") }
        btnResponsabilidade.setOnClickListener { falar("Responsabilidade") }
        btnAprendizado.setOnClickListener      { falar("Aprendizado") }
        btnGenerosidade.setOnClickListener     { falar("Generosidade") }
        btnAcordamento.setOnClickListener      { falar("Acordamento") }
        btnAniversariante.setOnClickListener   { falar("Aniversariante") }
        btnEstacionamento.setOnClickListener   { falar("Estacionamento") }

        btnVerificar5.setOnClickListener { verificarExercicio() }

        btn_aboutlicoes.setOnClickListener {
            startActivity(Intent(this, ActivityAboutLicoes::class.java))
        }

        btn_stoplicoes.setOnClickListener {
            startActivity(
                Intent(this, ActivityStopLicoesAbout::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }
    }

    private fun verificarExercicio() {
        val textoOportunidade     = edtOportunidade.text.toString().trim()
        val textoResponsabilidade = edtResponsabilidade.text.toString().trim()
        val textoAprendizado      = edtAprendizado.text.toString().trim()
        val textoGenerosidade     = edtGenerosidade.text.toString().trim()
        val textoAcordamento      = edtAcordamento.text.toString().trim()
        val textoAniversariante   = edtAniversariante.text.toString().trim()
        val textoEstacionamento   = edtEstacionamento.text.toString().trim()

        if (textoOportunidade.isBlank() || textoResponsabilidade.isBlank() ||
            textoAprendizado.isBlank() || textoGenerosidade.isBlank() ||
            textoAcordamento.isBlank() || textoAniversariante.isBlank() ||
            textoEstacionamento.isBlank()) {
            Toast.makeText(this, "Preencha todos os campos do exercício", Toast.LENGTH_SHORT).show()
            return
        }

        if (textoOportunidade.lowercase() != "oportunidade") {
            Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show(); return
        }
        if (textoResponsabilidade.lowercase() != "responsabilidade") {
            Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show(); return
        }
        if (textoAprendizado.lowercase() != "aprendizado") {
            Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show(); return
        }
        if (textoGenerosidade.lowercase() != "generosidade") {
            Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show(); return
        }
        if (textoAcordamento.lowercase() != "acordamento") {
            Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show(); return
        }
        if (textoAniversariante.lowercase() != "aniversariante") {
            Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show(); return
        }
        if (textoEstacionamento.lowercase() != "estacionamento") {
            Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show(); return
        }

        val prefs = getSharedPreferences("Progresso", MODE_PRIVATE)
        prefs.edit().putInt("U4L4EX3_concluido", 1).apply()

        Toast.makeText(this, "Parabéns, suas respostas estão corretas!!!", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, ActivityParabens::class.java).apply {
            putExtra(ActivityParabens.EXTRA_UNIDADE_CONCLUIDA, 4)
        }
        startActivity(intent)
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
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) return
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