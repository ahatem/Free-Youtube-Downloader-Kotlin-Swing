package models

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.prefs.Preferences

object UserPreferences {
  private val prefs: Preferences = Preferences.userNodeForPackage(this::class.java)
  private const val KEY_MAX_PARALLEL_DOWNLOADS = "MAX_PARALLEL_DOWNLOADS"
  private const val KEY_FFMPEG_PATH = "FFMPEG_PATH"

  var DOWNLOADS_FOLDER = System.getProperty("user.home") + "\\Downloads\\"

  var MAX_PARALLEL_DOWNLOADS: Int = 3
    get() = prefs.get(KEY_MAX_PARALLEL_DOWNLOADS, "3").toInt()
    set(value) {
      prefs.put(KEY_MAX_PARALLEL_DOWNLOADS, value.toString())
      field = value
    }

  var FFMPEG_PATH = ""
    get() = prefs.get(KEY_FFMPEG_PATH, "")
    set(value) {
      prefs.put(KEY_FFMPEG_PATH, value)
      field = value
    }

  fun isFFmpegAvailable(): Boolean {
    return (isFFmpegInstalledInSystem() || FFMPEG_PATH.isNotEmpty())
  }

  fun isFFmpegInstalledInSystem(): Boolean {
    val processBuilder = ProcessBuilder().redirectErrorStream(true)
    val ffmpegVersionProcess = processBuilder.command(*arrayOf("cmd.exe" , "/c" , "ffmpeg -version")).start()
    ffmpegVersionProcess.waitFor()
    val ffmpegReader = BufferedReader(InputStreamReader(ffmpegVersionProcess.inputStream))
    val ffmpegLine = ffmpegReader.lineSequence().first()
    val ffmpegVersion = Regex("ffmpeg version ([\\d.]+)").find(ffmpegLine)?.groupValues?.get(1)
    ffmpegVersionProcess.destroy()
    return ffmpegVersion != null
  }

}