package app.music.utils

object ThreadUtils {

    fun startNewStateThread(thread: Thread) {
        if (thread.state == Thread.State.NEW) {
            thread.start()
        }
    }
}
