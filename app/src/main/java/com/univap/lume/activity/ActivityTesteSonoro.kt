package com.univap.lume.activity

import android.media.MediaPlayer
import android.net.Uri
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.univap.lume.MainActivity
import com.univap.lume.R
import com.univap.lume.model.MidiaLocalAudio1

class ActivityTesteSonoro : AppCompatActivity() {

    private lateinit var txt_avisoSonoro: TextView
    private lateinit var txt_avisoSonoro2: TextView
    private lateinit var btn_selecionarSom: Button
    private lateinit var btn_pularEtapa: Button
    private lateinit var btn_som1: Button
    private lateinit var btn_som2: Button
    private lateinit var btn_som3: Button

    private val storage = FirebaseStorage.getInstance()
    private val database = FirebaseDatabase.getInstance()

    // Variáveis para controlar a reprodução do áudio
    private var mediaPlayer: MediaPlayer? = null
    private var botaoAtual: Button? = null
    private var audioAtualResId: Int? = null

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
        btn_som1.setOnClickListener {
            gerenciarAudioEUpload(btn_som1, R.raw.som1, "som1", "Áudio Exemplo 1")
        }

        btn_som2.setOnClickListener {
            gerenciarAudioEUpload(btn_som2, R.raw.som2, "som2", "Áudio Exemplo 2")
        }

        btn_som3.setOnClickListener {
            gerenciarAudioEUpload(btn_som3, R.raw.som3, "som3", "Áudio Exemplo 3")
        }
        btn_selecionarSom.setOnClickListener {
            if (audioAtualResId != null) {
                // Ação ao selecionar: o usuário escolheu o áudio que está tocando/selecionado
                Toast.makeText(this, "Som selecionado com sucesso!", Toast.LENGTH_SHORT).show()
                pararAudioAtual()
                val tela_main = Intent(this, MainActivity::class.java)
                startActivity(tela_main)
            } else {
                Toast.makeText(this, "Por favor, toque em um áudio para ouvir antes de selecionar.", Toast.LENGTH_SHORT).show()
            }
        }
        btn_pularEtapa.setOnClickListener {
            val tela_main=Intent(this, MainActivity::class.java)
            startActivity(tela_main)
        }
    }

    private fun gerenciarAudioEUpload(botao: Button, resId: Int, nomeRes: String, titulo: String) {
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
        mediaPlayer = MediaPlayer.create(this, resId).apply {
            setOnCompletionListener {
                // Reseta as variáveis quando o som termina de tocar
                pararAudioAtual()
            }
            start()
        }

        audioAtualResId = resId
        botaoAtual = botao

        // Executa o upload para o Firebase
        uploadAudioERegistrar(resId, nomeRes, titulo)
    }

    private fun pararAudioAtual() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        audioAtualResId = null
        botaoAtual = null
    }

    private fun uploadAudioERegistrar(resId: Int, nomeRes: String, titulo: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "usuario_anonimo"
        val audioRef = storage.reference.child("audios/$userId/$nomeRes.mp3")

        val rawUri = Uri.parse("android.resource://$packageName/$resId")
        val stream = contentResolver.openInputStream(rawUri)

        if (stream != null) {
            val uploadTask = audioRef.putStream(stream)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                audioRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result.toString()

                    val dbRef = database.getReference("midias_audio")
                    val midiaId = dbRef.push().key ?: ""

                    val midiaAudio = MidiaLocalAudio1(
                        id_usuario = userId,
                        id_midiaaudio = midiaId,
                        nome_res_audio = nomeRes,
                        titulo = titulo,
                        preview_url = downloadUri
                    )

                    dbRef.child(midiaId).setValue(midiaAudio)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Áudio salvo com sucesso!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Erro ao salvar no banco.", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Falha no upload do áudio.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Libera a memória do MediaPlayer ao fechar a tela
        pararAudioAtual()
    }
}