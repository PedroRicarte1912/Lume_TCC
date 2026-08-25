package com.univap.lume.activity

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.univap.lume.R
import java.util.Locale

class ActivityU3L3EX5 : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech

    private lateinit var btnRemedio: Button
    private lateinit var btnComida: Button
    private lateinit var btnAgua: Button
    private lateinit var btnMercado: Button
    private lateinit var btnPreco: Button
    private lateinit var btnBanco: Button
    private lateinit var btnVerificar555: Button
    private lateinit var btn_aboutlicoes: Button
    private lateinit var btn_stoplicoes: Button

    private lateinit var edtRemedio: EditText
    private lateinit var edtComida: EditText
    private lateinit var edtAgua: EditText
    private lateinit var edtMercado: EditText
    private lateinit var edtPreco: EditText
    private lateinit var edtBanco: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade3licao3ex5)

        tts = TextToSpeech(this, this)
        tts.setSpeechRate(0.8f)

        btnRemedio      = findViewById(R.id.btnRemedio)
        btnComida       = findViewById(R.id.btnComida)
        btnAgua         = findViewById(R.id.btnAgua)
        btnMercado      = findViewById(R.id.btnMercado)
        btnPreco        = findViewById(R.id.btnPreco)
        btnBanco        = findViewById(R.id.btnBanco)
        btnVerificar555 = findViewById(R.id.btnVerificar555)
        btn_aboutlicoes = findViewById(R.id.btn_aboutlicoes)
        btn_stoplicoes  = findViewById(R.id.btn_stoplicoes)

        edtRemedio = findViewById(R.id.edtRemedio)
        edtComida  = findViewById(R.id.edtComida)
        edtAgua    = findViewById(R.id.edtAgua)
        edtMercado = findViewById(R.id.edtMercado)
        edtPreco   = findViewById(R.id.edtPreco)
        edtBanco   = findViewById(R.id.edtBanco)

        btnRemedio.setOnClickListener { falar("Remédio") }
        btnComida.setOnClickListener  { falar("Comida") }
        btnAgua.setOnClickListener    { falar("Água") }
        btnMercado.setOnClickListener { falar("Mercado") }
        btnPreco.setOnClickListener   { falar("Preço") }
        btnBanco.setOnClickListener   { falar("Banco") }

        btnVerificar555.setOnClickListener { verificarExercicio() }

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
        val textoRemedio = edtRemedio.text.toString().trim()
        val textoComida  = edtComida.text.toString().trim()
        val textoAgua    = edtAgua.text.toString().trim()
        val textoMercado = edtMercado.text.toString().trim()
        val textoPreco   = edtPreco.text.toString().trim()
        val textoBanco   = edtBanco.text.toString().trim()

        if (textoRemedio.isBlank() || textoComida.isBlank() || textoAgua.isBlank() ||
            textoMercado.isBlank() || textoPreco.isBlank() || textoBanco.isBlank()) {
            Toast.makeText(this, "Preencha todos os campos do exercício", Toast.LENGTH_SHORT).show()
            return
        }

        if (textoRemedio.lowercase() != "remédio" && textoRemedio.lowercase() != "remedio") {
            Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show(); return
        }
        if (textoComida.lowercase() != "comida") {
            Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show(); return
        }
        if (textoAgua.lowercase() != "água" && textoAgua.lowercase() != "agua") {
            Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show(); return
        }
        if (textoMercado.lowercase() != "mercado") {
            Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show(); return
        }
        if (textoPreco.lowercase() != "preço" && textoPreco.lowercase() != "preco") {
            Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show(); return
        }
        if (textoBanco.lowercase() != "banco") {
            Toast.makeText(this, "Palavra(s) incorreta(s)", Toast.LENGTH_SHORT).show(); return
        }

        val prefs = getSharedPreferences("Progresso", MODE_PRIVATE)
        prefs.edit().putInt("U3L3E5_concluido", 1).apply()

        Toast.makeText(this, "Parabéns, suas respostas estão corretas!!!", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, ActivityParabens::class.java).apply {
            putExtra(ActivityParabens.EXTRA_UNIDADE_CONCLUIDA, 3)
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