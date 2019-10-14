package app.music.utils.dialog.songoption

import app.music.listener.dialoglistener.DialogSongOptionListener
import app.music.model.BaseMusik

object DialogSongOptionMethodUtils {

    fun showSongOption(dialogSongOptionListener: DialogSongOptionListener, musik: BaseMusik) {
        dialogSongOptionListener.showSongOptionDialog(musik)
    }
}