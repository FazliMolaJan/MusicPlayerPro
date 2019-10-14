package app.music.listener

interface FirstDetailFragmentListener {
    fun changeCenterCoverArt(byteArray: ByteArray)

    fun changeCenterCoverArt(coverArtLink: String)

    fun resumeAnimation()

    fun pauseAnimation()

    fun startAnimation()

    fun changePlayButtonActivate(buttonActivate: Boolean)
}
