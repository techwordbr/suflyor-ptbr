package com.olerast.suflyor.speech

import android.content.Context
import com.olerast.suflyor.App
import com.olerast.suflyor.script.SpeechLang
import org.json.JSONObject
import org.vosk.Model
import org.vosk.Recognizer
import java.io.File
import java.util.zip.ZipInputStream

/** Offline Portuguese streaming ASR sharing the existing microphone and session lifecycle. */
class VoskAsr private constructor(private val model: Model, override val hotwordsSetting: Boolean) : AsrEngine {
    override val lang = SpeechLang.PT
    override val description = "Vosk português 0.3, offline"
    private var recognizer: Recognizer? = null
    private var rate = 0
    private var lastText = ""
    private var released = false

    @Synchronized
    override fun accept(samples: FloatArray, sampleRate: Int): AsrUpdate? {
        if (released) return null
        if (recognizer == null || rate != sampleRate) {
            recognizer?.close()
            recognizer = Recognizer(model, sampleRate.toFloat())
            rate = sampleRate
            lastText = ""
        }
        // AudioCapture supplies normalized floats; Vosk's short overload expects signed PCM16.
        val pcm = ShortArray(samples.size) { (samples[it] * 32768f).toInt().coerceIn(-32768, 32767).toShort() }
        val rec = checkNotNull(recognizer)
        val final = rec.acceptWaveForm(pcm, pcm.size)
        val text = JSONObject(if (final) rec.result else rec.partialResult)
            .optString(if (final) "text" else "partial").trim()
        if (final) {
            lastText = ""
            return AsrUpdate(text, true)
        }
        if (text == lastText) return null
        lastText = text
        return AsrUpdate(text, false)
    }

    // Keep full vocabulary: a restricted grammar can force unrelated speech into script words.
    override fun setBiasWords(words: List<String>) = Unit

    @Synchronized
    override fun reset() {
        if (released) return
        recognizer?.reset()
        lastText = ""
    }

    @Synchronized
    override fun release() {
        if (released) return
        released = true
        recognizer?.close()
        recognizer = null
        model.close()
    }

    companion object {
        @Synchronized
        fun create(context: Context): VoskAsr {
            val dir = File(context.filesDir, "vosk-pt-0.3")
            val ready = File(dir, ".ready")
            if (!ready.exists()) {
                dir.mkdirs()
                ZipInputStream(context.assets.open("asr-pt.zip")).use { zip ->
                    while (true) {
                        val entry = zip.nextEntry ?: break
                        val relative = entry.name.substringAfter('/', "")
                        if (relative.isEmpty()) continue
                        val target = File(dir, relative)
                        require(target.canonicalPath.startsWith(dir.canonicalPath + File.separator))
                        if (entry.isDirectory) target.mkdirs() else {
                            target.parentFile?.mkdirs()
                            target.outputStream().use { zip.copyTo(it) }
                        }
                    }
                }
                ready.writeText("0.3")
            }
            return VoskAsr(Model(dir.absolutePath), App.instance.settings.useHotwords)
        }
    }
}
