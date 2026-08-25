package com.univap.lume.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.univap.lume.R

class ActivityStopLicoesAbout: AppCompatActivity() {
    private lateinit var txt_texto1: TextView
    private lateinit var txt_texto2: TextView
    private lateinit var txt_texto3: TextView
    private lateinit var btn_stoplicao2: Button
    private lateinit var btn_voltarLicoes: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stoplicoesabout)
        txt_texto1=findViewById(R.id.txt_texto1)
        txt_texto2=findViewById(R.id.txt_texto2)
        txt_texto3=findViewById(R.id.txt_texto3)
        btn_stoplicao2=findViewById(R.id.btn_stoplicao2)
        btn_voltarLicoes=findViewById(R.id.btn_voltarLicoes)

        btn_voltarLicoes.setOnClickListener {
            val tela_licoes= Intent(this, ActivityLicoes1::class.java)
            startActivity(tela_licoes)
        }





    }




}