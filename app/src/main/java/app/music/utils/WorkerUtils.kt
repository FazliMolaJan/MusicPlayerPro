package app.music.utils

import androidx.work.OneTimeWorkRequest
import androidx.work.Worker

object WorkerUtils {

    inline fun <reified T : Worker> buildOneTimeWorkRequest(): OneTimeWorkRequest {
        return OneTimeWorkRequest.Builder(T::class.java).build()
    }
}