package com.android_development.datefinder

class DateExtract {

    private val regexes = listOf(
        // dd/MM/yyyy, dd-MM-yyyy, dd.MM.yyyy
        "\\b\\d{1,2}[./-]\\d{1,2}[./-]\\d{4}\\b",

        // dd/MM/yy, dd-MM-yy, dd.MM.yy
        "\\b\\d{1,2}[./-]\\d{1,2}[./-]\\d{2}\\b",

        // yyyy/MM/dd, yyyy-MM-dd, yyyy.MM.dd
        "\\b\\d{4}[./-]\\d{1,2}[./-]\\d{1,2}\\b"
    ).map {
        it.toRegex(setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
    }

    fun find(text: String): String? {
        val cleaned = text.replace("[–—−]".toRegex(), "-")  // normalize dashes
        for (regex in regexes) {
            regex.find(cleaned)?.let { return it.value }
        }
        return null
    }
}
