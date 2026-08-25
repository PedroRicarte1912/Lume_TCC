package com.univap.lume.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.univap.lume.R
import android.speech.tts.TextToSpeech
import android.widget.EditText
import android.widget.Toast
import java.util.Locale

class ActivityU1L1EX2: AppCompatActivity(),TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private lateinit var txt_explicacao3: TextView
    private lateinit var btn_audioA2: Button
    private lateinit var btn_audioE2: Button
    private lateinit var btn_audioI2: Button
    private lateinit var btn_audioO2: Button
    private lateinit var btn_audioU2: Button
    private lateinit var edt_letraA: EditText
    private lateinit var edt_letraE: EditText
    private lateinit var edt_letraI: EditText
    private lateinit var edt_letraO: EditText
    private lateinit var edt_letraU: EditText
    private lateinit var btn_proxlicao2: Button
    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade1licao1ex2)
        tts = TextToSpeech(this, this)
        txt_explicacao3=findViewById<TextView>(R.id.txt_explicacao3)
        btn_audioA2=findViewById<Button>(R.id.btn_audioA2)
        btn_audioE2=findViewById<Button>(R.id.btn_audioE2)
        btn_audioI2=findViewById<Button>(R.id.btn_audioI2)
        btn_audioO2=findViewById<Button>(R.id.btn_audioO2)
        btn_audioU2=findViewById<Button>(R.id.bnt_audioU2)
        btn_proxlicao2=findViewById<Button>(R.id.btn_proxlicao2)
        edt_letraA=findViewById<EditText>(R.id.edt_letraA)
        edt_letraE=findViewById<EditText>(R.id.edt_letraE)
        edt_letraI=findViewById<EditText>(R.id.edt_letraI)
        edt_letraO=findViewById<EditText>(R.id.edt_letraO)
        edt_letraU=findViewById<EditText>(R.id.edt_letraU)

        btn_audioA2.setOnClickListener { falar("A") }
        btn_audioE2.setOnClickListener { falar("E") }
        btn_audioI2.setOnClickListener { falar("I") }
        btn_audioO2.setOnClickListener { falar("O") }
        btn_audioU2.setOnClickListener { falar("U") }
        btn_proxlicao2.setOnClickListener {
            Exercicio2()
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
    private fun Exercicio2() {
        val textoA = edt_letraA.text.toString().trim()
        val textoE = edt_letraE.text.toString().trim()
        val textoI = edt_letraI.text.toString().trim()
        val textoO = edt_letraO.text.toString().trim()
        val textoU = edt_letraU.text.toString().trim()

        // ✅ 1. Verifica campos vazios primeiro
        if (textoA.isBlank() || textoE.isBlank() || textoI.isBlank() ||
            textoO.isBlank() || textoU.isBlank()) {
            Toast.makeText(this, "Preencha todos os campos do exercício", Toast.LENGTH_SHORT).show()
            return // para por aqui
        }

        // ✅ 2. Valida cada letra com && (E)
        val mensagemErro = "Insira a letra correta (maiúscula ou minúscula)"

        if (textoA != "A" && textoA != "a") {
            Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT).show(); return
        }
        if (textoE != "E" && textoE != "e") {
            Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT).show(); return
        }
        if (textoI != "I" && textoI != "i") {
            Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT).show(); return
        }
        if (textoO != "O" && textoO != "o") {
            Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT).show(); return
        }
        if (textoU != "U" && textoU != "u") {
            Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT).show(); return
        }
        val prefs = getSharedPreferences("Progresso", MODE_PRIVATE)
        prefs.edit().putInt("U1L2_concluido", 1).apply()

        Toast.makeText(this, "Parabéns, suas respostas estão corretas!!!", Toast.LENGTH_SHORT).show()
        val tela_ex4 = Intent(this, ActivityU1L1EX4::class.java)
        startActivity(tela_ex4)
        finish()
    }
}