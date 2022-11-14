package io.h4kt.pivosound.queue

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import java.util.*

class QueuedAudioPlayer(
    private val handle: AudioPlayer
) {

    var repeatMode = RepeatMode.NONE

    var isPaused: Boolean
        get() = handle.isPaused
        set(value) { handle.isPaused = value }

    var volume: Int
        get() = handle.volume
        set(value) { handle.volume = value }

    val currentTrack: AudioTrack
        get() = handle.playingTrack

    val queue = LinkedList<AudioTrack>()

    init {

        handle.addListener(object : AudioEventAdapter() {

            override fun onTrackEnd(
                player: AudioPlayer,
                track: AudioTrack,
                endReason: AudioTrackEndReason
            ) {

                if (endReason != AudioTrackEndReason.FINISHED) {
                    return
                }

                if (repeatMode == RepeatMode.CURRENT_TRACK) {
                    player.playTrack(track)
                    return
                }

                queue.pop()?.let { player.playTrack(it) }

            }

        })

    }

    fun provide() = handle.provide()

    fun enqueue(track: AudioTrack) {

        if (handle.playingTrack == null) {
            handle.playTrack(track)
        } else {
            queue += track
        }

    }

    fun stop() = handle.stopTrack()

    fun destroy() {
        queue.clear()
        handle.destroy()
    }

}