package com.olerast.suflyor

import com.olerast.suflyor.doc.Paragraph
import com.olerast.suflyor.doc.ScriptDocument
import com.olerast.suflyor.script.PortugueseNorm
import com.olerast.suflyor.script.ScriptLayout
import com.olerast.suflyor.script.SpeechLang
import com.olerast.suflyor.track.ScriptTracker
import org.junit.Assert.*
import org.junit.Test

class PortugueseTrackerTest {
    private val lang = SpeechLang.PT
    private fun doc(text: String) = ScriptDocument("Teste", "test", listOf(Paragraph(text)))

    @Test fun followsActualVoskPortugueseAudioResults() {
        // Captured from the bundled model decoding a synthetic Brazilian Portuguese voice.
        val text = "Olá pessoal, hoje vamos mostrar como gravar vídeos com qualidade. A câmera do celular permite criar imagens bonitas. Agora vamos falar sobre tecnologia e compartilhar novidades com você."
        val m = ScriptLayout.build(doc(text), true, lang=lang)
        val tracker = ScriptTracker(m.tokens, lang)
        val finals = ArrayDeque<String>()
        val events = checkNotNull(javaClass.getResourceAsStream("/portuguese-asr.tsv"))
            .bufferedReader().use { it.readLines() }
        for (event in events) {
            val words = lang.words(event.substringAfter('\t', ""))
            if (event.startsWith("F")) {
                finals.addAll(words)
                while (finals.size > 16) finals.removeFirst()
                tracker.onHypothesis(finals.toList())
            } else tracker.onHypothesis(finals.toList() + words)
        }
        assertTrue("Audio reached ${tracker.position}/${tracker.size}", tracker.position >= tracker.size - 1)
    }

    @Test fun accentsAndColloquialSpeech() {
        assertEquals(listOf("ola", "voce", "nao", "acao", "coracao"), lang.words("OLÁ, você não: ação coração"))
        assertEquals(lang.normalize("você"), lang.normalize("vc"))
        assertEquals(lang.normalize("para"), lang.normalize("pra"))
        assertEquals("senhor", lang.normalize("Sr"))
        assertEquals(0f, lang.similarity("com", "som"))
        assertEquals(0.2f, lang.weight("para", false))
    }

    @Test fun detectsPortugueseAndPreservesEnglishRussian() {
        assertEquals(lang, SpeechLang.detect(doc("Olá, hoje vamos gravar um vídeo com você em português.")))
        assertEquals(lang, SpeechLang.detect(doc("Hoje vamos mostrar como usar este aplicativo no seu celular")))
        assertEquals(SpeechLang.EN, SpeechLang.detect(doc("Hello, today we show you the camera")))
        assertEquals(SpeechLang.RU, SpeechLang.detect(doc("Привет сегодня расскажем про камеру")))
        assertEquals(SpeechLang.EN, SpeechLang.detect(doc("[Olá você não] Hello there")))
    }

    @Test fun followsPortuguesePartialsAndIgnoresSilence() {
        val m = ScriptLayout.build(doc("Olá pessoal hoje vamos mostrar novidades incríveis sobre tecnologia brasileira. A câmera permite gravar imagens bonitas com qualidade excelente."), false, lang=lang)
        val tracker = ScriptTracker(m.tokens, lang)
        val heard = lang.words("ola pessoal hoje vamos mostrar novidades incriveis sobre tecnologia brasileira a camera permite gravar imagens bonitas com qualidade excelente")
        for (n in 1..heard.size) tracker.onHypothesis(heard.take(n).takeLast(16))
        assertTrue("Reached end: ${tracker.position}", tracker.position >= m.tokens.size - 2)
        val before = tracker.position
        repeat(5) { tracker.onHypothesis(emptyList()) }
        assertEquals(before, tracker.position)
    }

    @Test fun repeatedSentenceMovesBackAndSkippedParagraphCatchesUp() {
        val paragraphs = listOf(
            "Câmeras modernas capturam imagens incríveis durante viagens.",
            "Microfones pequenos melhoram bastante nossas gravações externas.",
            "Baterias resistentes garantem energia suficiente durante aventuras.",
        )
        val m = ScriptLayout.build(ScriptDocument("Teste", "test", paragraphs.map { Paragraph(it) }), false, lang=lang)
        val tracker = ScriptTracker(m.tokens, lang)
        fun say(text: String) {
            val words = lang.words(text)
            for (n in 1..words.size) tracker.onHypothesis(words.take(n))
        }
        say(paragraphs[0])
        say(paragraphs[2])
        assertTrue(tracker.position >= m.tokens.size - 2)
        say(paragraphs[0])
        assertTrue("Returned to first paragraph: ${tracker.position}", tracker.position < 10)
    }

    @Test fun numbersDoNotBlockFollowingAndNotesAreNotSpoken() {
        assertEquals(listOf("custa", "apenas", "hoje"), PortugueseNorm.prepareHypothesis(
            lang.words("custa apenas cento vinte reais hoje"), setOf("custa", "apenas", "hoje"), true))
        val m = ScriptLayout.build(doc("Você pode [sorria para a câmera] gravar vídeos incríveis."), false, lang=lang)
        assertTrue(m.tokens.filter { it.norm == "sorria" || it.norm == "camera" }.all { !it.spoken })
    }
}
