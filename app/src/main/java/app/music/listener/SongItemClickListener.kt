package app.music.listener

import app.music.model.BaseMusik

interface SongItemClickListener {
    fun <MusicType : BaseMusik> onSongClick(
            position: Int, musicList: List<MusicType>, isLongClick: Boolean)
}
