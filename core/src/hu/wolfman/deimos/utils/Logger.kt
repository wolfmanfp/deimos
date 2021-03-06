package hu.wolfman.deimos.utils

import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Általános célú naplózó osztály.
 *
 * @author Farkas Péter
 */
object Logger {
  private val filename: String = "deimos.log"

  /**
   * A naplófájl tartalmához hozzáfűz egy dátummal ellátott üzenetet.
   *
   * @param message A naplóban megjelenő üzenet.
   */
  @JvmStatic
  fun log(message: String) = try {
    FileWriter(filename, true).use { writer ->
      val timeLog = SimpleDateFormat("yyyy. MM. dd. HH:mm:ss")
          .format(Calendar.getInstance().time)
      writer.write("[$timeLog] $message\r\n")
    }
  } catch (e: Exception) {
    e.printStackTrace()
  }

}
