package com.univap.lume.network

data class SpotifySearchResponse(
    val query: String,
    val tracks: List<SpotifyTrackDto>,
    val artists: List<SpotifyArtistDto>,
    val albums: List<SpotifyAlbumDto>
)

data class SpotifyTrackDto(
    val id: String,
    val name: String,
    val artist: String,
    val album: String?,
    val image: String?,
    val spotifyUrl: String?,
    val previewUrl: String?   // <- precisa existir no server.js (ver observação abaixo)
)

data class SpotifyArtistDto(
    val id: String,
    val name: String,
    val image: String?,
    val spotifyUrl: String?
)

data class SpotifyAlbumDto(
    val id: String,
    val name: String,
    val artist: String,
    val image: String?,
    val spotifyUrl: String?
)