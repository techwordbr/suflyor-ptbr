package com.olerast.suflyor.script

import java.text.Normalizer
import java.util.Locale

/** Portuguese comparison rules. Displayed text always keeps its original accents. */
object PortugueseNorm {
    val WORD = Regex("[\\p{L}\\p{N}\\p{M}]+")
    val STOP = "a o as os um uma uns umas de da do das dos em no na nos nas e ou que se para por com ao aos eu voce voces ele ela nos eles elas meu minha seu sua isso esse essa este esta sao e foi ser ter tem nao sim".split(" ").toSet()
    val FILLERS = setOf("hum", "hmm", "ahn", "uh", "unk")
    private val SPOKEN = mapOf("vc" to "voce", "vcs" to "voces", "pra" to "para", "pro" to "para", "ta" to "esta", "to" to "estou", "sr" to "senhor", "sra" to "senhora", "dr" to "doutor", "dra" to "doutora")
    private val NUMBERS = "zero um uma dois duas tres quatro cinco seis sete oito nove dez onze doze treze catorze quatorze quinze dezesseis dezassete dezessete dezoito dezenove vinte trinta quarenta cinquenta sessenta setenta oitenta noventa cem cento duzentos trezentos quatrocentos quinhentos seiscentos setecentos oitocentos novecentos mil milhao milhoes bilhao bilhoes virgula reais real centavos".split(" ").toSet()

    fun normalizeWord(raw: String): String {
        val plain = Normalizer.normalize(raw.lowercase(Locale.ROOT), Normalizer.Form.NFD)
            .replace(Regex("\\p{M}+"), "")
        return SPOKEN[plain] ?: plain
    }
    fun words(text: String) = WORD.findAll(text).map { normalizeWord(it.value) }.toList()
    fun weight(norm: String, foreign: Boolean): Float = when {
        foreign || norm.isEmpty() || norm.first().isDigit() -> 0f
        norm in STOP -> 0.2f
        norm.length <= 2 -> 0.3f
        norm.length == 3 -> 0.6f
        else -> 1f
    }
    fun similarity(a: String, b: String): Float {
        if (a == b) return 1f
        if (minOf(a.length, b.length) < 4) return 0f
        return 1f - TextNorm.levenshtein(a, b).toFloat() / maxOf(a.length, b.length)
    }
    fun prepareHypothesis(hyp: List<String>, vocab: Set<String>, scriptHasDigits: Boolean): List<String> =
        if (!scriptHasDigits) hyp else hyp.filterNot {
            it.firstOrNull()?.isDigit() == true || (it in NUMBERS && it !in vocab)
        }
}
