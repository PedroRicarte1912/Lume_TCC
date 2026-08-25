package com.univap.lume

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.univap.lume.activity.ActivityMapearTea
import com.univap.lume.activity.ActivityMenuUnidades
import com.univap.lume.activity.ActivityTesteSonoro
import com.univap.lume.activity.AvatarActivity

class MainActivity : AppCompatActivity() {
    private lateinit var txt_bemvindo: TextView
    private lateinit var txt_mensagem1: TextView
    private lateinit var btn_testesensorial: Button
    private lateinit var btn_testetea: Button
    private lateinit var btn_avatar2: Button
    private lateinit var btn_lichoes2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txt_bemvindo = findViewById(R.id.txt_bemvindo)
        txt_mensagem1 = findViewById(R.id.txt_mensagem1)
        btn_testesensorial = findViewById(R.id.btn_testesensorial)
        btn_testetea = findViewById(R.id.btn_testetea)
        btn_avatar2 = findViewById(R.id.btn_avatar2)
        btn_lichoes2 = findViewById(R.id.btn_lichoes2)

        btn_avatar2.setOnClickListener {
            val tela_avatar = Intent(this, AvatarActivity::class.java)
            startActivity(tela_avatar)
        }

        btn_testesensorial.setOnClickListener {
            val tela_testesensorial = Intent(this, ActivityTesteSonoro::class.java)
            startActivity(tela_testesensorial)
        }

        btn_testetea.setOnClickListener {
            val tela_mapear = Intent(this, ActivityMapearTea::class.java)
            startActivity(tela_mapear)
        }

        btn_lichoes2.setOnClickListener {
            // 1. Busca o nível salvo nas SharedPreferences (com padrão 1)
            val prefs = getSharedPreferences(ActivityMenuUnidades.PREFS_NAME, MODE_PRIVATE)
            val nivelSalvoPrefs = prefs.getInt(ActivityMenuUnidades.NIVEL_TEA_KEY, 1)

            // 2. Tenta recuperar do Intent caso a MainActivity tenha recebido da ActivityMapearTea
            val nivelTea = intent.getIntExtra(ActivityMenuUnidades.NIVEL_TEA_KEY, nivelSalvoPrefs)

            // 3. Garante que está salvo atualizado nas SharedPreferences
            prefs.edit().putInt(ActivityMenuUnidades.NIVEL_TEA_KEY, nivelTea).apply()

            // 4. Inicia a ActivityMenuUnidades enviando o valor via Intent Extra
            val tela_licoes = Intent(this, ActivityMenuUnidades::class.java).apply {
                putExtra(ActivityMenuUnidades.NIVEL_TEA_KEY, nivelTea)
            }
            startActivity(tela_licoes)
        }
    }

    override fun onStart() {
        super.onStart()
        val usuarioAtual = FirebaseAuth.getInstance().currentUser
        if (usuarioAtual != null) {
            // Usuário já está logado
        }
    }
}