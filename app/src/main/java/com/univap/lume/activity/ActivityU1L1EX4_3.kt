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

class ActivityU1L1EX4_3: AppCompatActivity(),TextToSpeech.OnInitListener {
    private lateinit var txt_explicacao9: TextView
    private lateinit var txt_explicacao10: TextView
    private lateinit var txt_explicacao11: TextView
    private lateinit var btn_silabaUAI: Button
    private lateinit var btn_silabaUEI: Button
    private lateinit var btn_silabaUAO: Button
    private lateinit var btn_silabaUOE: Button
    private lateinit var edt_silabaUAI: EditText
    private lateinit var edt_silabaUEI: EditText
    private lateinit var edt_silabaUAO: EditText
    private lateinit var edt_silabaUOE: EditText
    private lateinit var btn_proxlicao4_3: Button
    private lateinit var tts: TextToSpeech
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade1_licao1_ex4_3)
        tts = TextToSpeech(this, this)
        txt_explicacao11=findViewById(R.id.txt_explicacao11)
        txt_explicacao9=findViewById(R.id.txt_explicacao9)
        txt_explicacao10=findViewById(R.id.txt_explicacao10)
        btn_silabaUAI=findViewById(R.id.btn_silabaUAI)
        btn_silabaUEI=findViewById(R.id.btn_silabaUEI)
        btn_silabaUAO=findViewById(R.id.btn_silabaUAO)
        btn_silabaUOE=findViewById(R.id.btn_silabaUOE)
        btn_proxlicao4_3=findViewById(R.id.btn_proxlicao4_3)

        edt_silabaUAI = findViewById(R.id.edt_silabaUAI)
        edt_silabaUEI = findViewById(R.id.edt_silabaUEI)
        edt_silabaUAO = findViewById(R.id.edt_silabaUAO)
        edt_silabaUOE = findViewById(R.id.edt_silabaUOE)

        btn_silabaUAI.setOnClickListener { falar("UAI") }
        btn_silabaUEI.setOnClickListener { falar("UEI") }
        btn_silabaUAO.setOnClickListener { falar("UAO") }
        btn_silabaUOE.setOnClickListener { falar("UOE") }
        btn_proxlicao4_3.setOnClickListener {
            Exercicio4_3()
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
    private fun Exercicio4_3(){
        val textUAI=edt_silabaUAI.text.toString().trim()
        val textUEI=edt_silabaUEI.text.toString().trim()
        val textUAO=edt_silabaUAO.text.toString().trim()
        val textUOE=edt_silabaUOE.text.toString().trim()
        if(textUAI.isBlank()|| textUEI.isBlank()|| textUAO.isBlank()|| textUOE.isBlank()){
            Toast.makeText(this, "Preencha todos os campos do exercício", Toast.LENGTH_SHORT).show()
            return
        }
        val mensagemErro = "Insira a sílaba correta (maiúscula ou minúscula)"
        if(textUAI!="UAI"&&textUAI!="uai"){
            Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT).show(); return
        }
        if(textUEI!="UEI"&&textUEI!="uei"){
            Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT).show(); return
        }
        if(textUAO!="UAO"&&textUAO!="uao"){
            Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT).show(); return
        }
        if(textUOE!="UOE"&&textUOE!="uoe"){
            Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT).show(); return
        }
        Toast.makeText(this, "Parabéns, suas respostas estão corretas!!!", Toast.LENGTH_SHORT).show()
        val tela_U1L1EX4_4=Intent(this, ActivityU1L1EX4_4::class.java)
        startActivity(tela_U1L1EX4_4)
        finish()

    }

}
