package com.univap.lume.activity

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.univap.lume.MainActivity
import com.univap.lume.R

class ActivityTesteSonoro : AppCompatActivity() {

    private lateinit var txt_avisoSonoro: TextView
    private lateinit var txt_avisoSonoro2: TextView
    private lateinit var btn_selecionarSom: Button
    private lateinit var btn_pularEtapa: Button
    private lateinit var btn_som1: Button
    private lateinit var btn_som2: Button
    private lateinit var btn_som3: Button

    // Variáveis para controlar a reprodução do áudio
    private var mediaPlayer: MediaPlayer? = null
    private var botaoAtual: Button? = null
    private var audioAtualResId: Int? = null

    // Último som tocado ("som1", "som2" ou "som3").
    // Não é zerado quando o áudio termina, para o botão "Selecionar" continuar funcionando.
    private var somSelecionado: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testesonoro)

        txt_avisoSonoro = findViewById(R.id.txt_avisoSonoro)
        txt_avisoSonoro2 = findViewById(R.id.txt_avisoSonoro2)
        btn_selecionarSom = findViewById(R.id.btn_selecionarSom)
        btn_pularEtapa = findViewById(R.id.btn_pularEtapa)
        btn_som1 = findViewById(R.id.btn_som1)
        btn_som2 = findViewById(R.id.btn_som2)
        btn_som3 = findViewById(R.id.btn_som3)

        // Configuração dos cliques para cada áudio da pasta raw
        btn_som1.setOnClickListener { gerenciarAudio(btn_som1, R.raw.som1, "som1") }
        btn_som2.setOnClickListener { gerenciarAudio(btn_som2, R.raw.som2, "som2") }
        btn_som3.setOnClickListener { gerenciarAudio(btn_som3, R.raw.som3, "som3") }

        btn_selecionarSom.setOnClickListener {
            val som = somSelecionado
            if (som != null) {
                PreferenciasRepositorio.salvarSom(this, som)
                Toast.makeText(this, "Som selecionado com sucesso!", Toast.LENGTH_SHORT).show()
                pararAudioAtual()
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Toast.makeText(
                    this,
                    "Por favor, toque em um áudio para ouvir antes de selecionar.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btn_pularEtapa.setOnClickListener {
            pararAudioAtual()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun gerenciarAudio(botao: Button, resId: Int, nomeRes: String) {
        somSelecionado = nomeRes

        // Se clicar no mesmo botão que já está tocando ou pausado
        if (audioAtualResId == resId && mediaPlayer != null) {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer?.pause()
            } else {
                mediaPlayer?.start()
            }
            return
        }

        // Se clicar em um botão diferente, para o áudio anterior antes de iniciar o novo
        pararAudioAtual()

        // Inicia o novo áudio
        mediaPlayer = MediaPlayer.create(this, resId)?.apply {
            setOnCompletionListener {
                // Reseta o player quando o som termina de tocar
                pararAudioAtual()
            }
            start()
        }

        audioAtualResId = resId
        botaoAtual = botao
    }

    private fun pararAudioAtual() {
        mediaPlayer?.release()
        mediaPlayer = null
        audioAtualResId = null
        botaoAtual = null
    }

    override fun onStop() {
        super.onStop()
        // Se o usuário sair da tela, o áudio para
        pararAudioAtual()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Libera a memória do MediaPlayer ao fechar a tela
        pararAudioAtual()
    }
}