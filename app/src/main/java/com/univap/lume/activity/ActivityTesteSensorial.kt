package com.univap.lume.activity

import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.univap.lume.MainActivity
import com.univap.lume.R
import com.univap.lume.model.MidiaLocalAudio1
import com.univap.lume.model.SpotifyTrack1
import com.univap.lume.network.RetrofitClient
import com.univap.lume.network.SpotifyTrackDto
import kotlinx.coroutines.launch

// ======================================================
// Activity
// ======================================================
// Observação: o antigo "object SpotifyAuthManager" com
// CLIENT_ID/CLIENT_SECRET foi REMOVIDO. A busca agora passa
// pelo backend hospedado no Render (RetrofitClient), que é
// quem guarda as credenciais do Spotify. O app nunca mais
// toca no Client Secret.
// ======================================================
class ActivityTesteSensorial : AppCompatActivity() {

    // Views
    private lateinit var txt_mensagem3: TextView
    private lateinit var txt_mensagem5: TextView

    private lateinit var btn_selecao: Button
    private lateinit var btn_spotify: Button
    private lateinit var btn_pularetapa: Button

    // Estado
    private var usuarioIdRecebido: String? = null
    private var i = 0

    // Spotify
    private var mpSpotify: MediaPlayer? = null
    private var spotifyTrackSelecionada: SpotifyTrack1? = null

    // ======================================================
    // onCreate
    // ======================================================
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_testesensorial)

        val sharedPreferences =
            getSharedPreferences("LumeSettings", MODE_PRIVATE)

        usuarioIdRecebido =
            sharedPreferences.getString("USER_ID_KEY", null)

        // Bind
        txt_mensagem3 = findViewById(R.id.txt_mensagem3)
        txt_mensagem5 = findViewById(R.id.txt_mensagem5)

        btn_pularetapa = findViewById(R.id.btn_pularetapa)
        btn_spotify = findViewById(R.id.btn_spotify)
        btn_selecao = findViewById(R.id.btn_selecao)

        configurarBotaoSpotify()
        configurarBotaoSelecao()

        btn_pularetapa.setOnClickListener {
            val tela_main = Intent(this, MainActivity::class.java)
            startActivity(tela_main)
            finish()
        }
    }

    // ======================================================
    // Destroy
    // ======================================================
    override fun onDestroy() {
        super.onDestroy()
        liberarTodosPlayers()
    }

    // ======================================================
    // Áudios locais
    // ======================================================
    private fun tocarAudioLocal(player: MediaPlayer?) {

        player?.let {
            if (it.isPlaying) {
                it.seekTo(0)
            } else {
                it.start()
            }
        }
    }

    // ======================================================
    // Spotify — abrir diálogo de busca
    // ======================================================
    private fun configurarBotaoSpotify() {

        btn_spotify.setOnClickListener {
            mostrarDialogoBuscaSpotify()
        }
    }

    private fun mostrarDialogoBuscaSpotify() {

        val input = EditText(this).apply {
            hint = "Ex: relaxing piano"
            setPadding(48, 24, 48, 24)
        }

        AlertDialog.Builder(this)
            .setTitle("🎵 Buscar no Spotify")
            .setMessage("Digite o nome da música:")
            .setView(input)
            .setPositiveButton("Buscar") { _, _ ->

                val query = input.text.toString().trim()

                if (query.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Digite algo para buscar!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    buscarFaixasSpotify(query)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // ======================================================
    // Buscar músicas — agora via backend no Render
    // ======================================================
    private fun buscarFaixasSpotify(query: String) {

        atualizarMensagem("Buscando no Spotify...")

        lifecycleScope.launch {

            try {

                // Chama o backend (Node.js/Express no Render), que
                // guarda o Client ID/Secret e devolve os dados prontos.
                val response =
                    RetrofitClient.spotifyApi.searchSpotify(query)

                if (response.tracks.isEmpty()) {

                    Toast.makeText(
                        this@ActivityTesteSensorial,
                        "Nenhuma música encontrada.",
                        Toast.LENGTH_LONG
                    ).show()

                    atualizarMensagem("")

                } else {

                    mostrarDialogoResultados(response.tracks)
                }

            } catch (e: Exception) {

                Toast.makeText(
                    this@ActivityTesteSensorial,
                    "Erro: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()

                atualizarMensagem("")
            }
        }
    }

    // ======================================================
    // Resultados
    // ======================================================
    private fun mostrarDialogoResultados(
        tracks: List<SpotifyTrackDto>
    ) {

        val labels = tracks.map {

            val preview =
                if (it.previewUrl.isNullOrEmpty())
                    "Sem preview"
                else
                    "Com preview"

            "${it.name} — ${it.artist} ($preview)"
        }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Escolha uma música")
            .setItems(labels) { _, index ->

                val track = tracks[index]

                // Converte o DTO do backend para o modelo usado no
                // resto do app (Firebase, tela de Parabéns etc.)
                val trackConvertida = SpotifyTrack1(
                    id = track.id,
                    name = track.name,
                    artist = track.artist,
                    previewUrl = track.previewUrl ?: ""
                )

                spotifyTrackSelecionada = trackConvertida

                tocarPreviewSpotify(trackConvertida)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // ======================================================
    // Tocar preview
    // ======================================================
    private fun tocarPreviewSpotify(track: SpotifyTrack1) {

        spotifyTrackSelecionada = track

        // IMPORTANTE
        i = 4

        atualizarMensagem(
            "♫ Spotify: ${track.name} — ${track.artist}"
        )

        // Caso não tenha preview
        if (track.previewUrl.isEmpty()) {

            AlertDialog.Builder(this)
                .setTitle(track.name)
                .setMessage(
                    "Essa música não possui preview.\n\n" +
                            "Deseja abrir no Spotify?"
                )
                .setPositiveButton("Abrir") { _, _ ->

                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                            "https://open.spotify.com/track/${track.id}"
                        )
                    )

                    startActivity(intent)
                }
                .setNegativeButton("Cancelar", null)
                .show()

            return
        }

        // Para preview anterior
        pararSpotify()

        // Toca preview
        mpSpotify = MediaPlayer().apply {

            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )

            setDataSource(track.previewUrl)

            prepareAsync()

            setOnPreparedListener {
                start()
            }

            setOnErrorListener { _, _, _ ->

                Toast.makeText(
                    this@ActivityTesteSensorial,
                    "Erro ao reproduzir preview.",
                    Toast.LENGTH_SHORT
                ).show()

                true
            }
        }
    }

    // ======================================================
    // Parar spotify
    // ======================================================
    private fun pararSpotify() {

        mpSpotify?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }

        mpSpotify = null
    }

    // ======================================================
    // Salvar seleção
    // ======================================================
    private fun configurarBotaoSelecao() {

        btn_selecao.setOnClickListener {
            val audioNow = i
            registraAudio(audioNow)
        }
    }

    private fun registraAudio(indiceAudio: Int) {

        if (indiceAudio == 0) {
            Toast.makeText(
                this,
                "Selecione um áudio primeiro!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val idUser =
            usuarioIdRecebido
                ?: FirebaseAuth.getInstance().currentUser?.uid
                ?: ""

        val nomeArquivo = when (indiceAudio) {
            4 -> "spotify:${spotifyTrackSelecionada?.id}"
            else -> "audio$indiceAudio"
        }

        if (idUser.isNotEmpty()) {

            val referencia = FirebaseDatabase
                .getInstance()
                .getReference("midias_audio")

            val novoAudio = MidiaLocalAudio1(

                id_usuario = idUser,
                id_midiaaudio = idUser,
                nome_res_audio = nomeArquivo,

                titulo =
                    if (indiceAudio == 4)
                        "Spotify: ${spotifyTrackSelecionada?.name}"
                    else
                        "Audio de Preferência",

                preview_url =
                    if (indiceAudio == 4)
                        spotifyTrackSelecionada?.previewUrl ?: ""
                    else
                        ""
            )

            referencia
                .child(idUser)
                .setValue(novoAudio)
                .addOnSuccessListener {

                    Toast.makeText(
                        this,
                        "Preferência salva!",
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(
                        Intent(this, MainActivity::class.java)
                    )

                    finish()
                }
                .addOnFailureListener {

                    Toast.makeText(
                        this,
                        "Erro: ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

        } else {

            Toast.makeText(
                this,
                "Usuário não encontrado.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // ======================================================
    // Helpers
    // ======================================================
    private fun atualizarMensagem(msg: String) {
        txt_mensagem3.text = msg
    }

    private fun liberarTodosPlayers() {
        pararSpotify()
    }
}