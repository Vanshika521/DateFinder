package com.android_development.datefinder

class DateExtract {

    private val regexes = listOf(
    //Patterns to match different date formats

        //for:-  dd/MM/yyyy, dd-MM-yyyy, dd.MM.yyyy
        "\\b\\d{1,2}[./-]\\d{1,2}[./-]\\d{4}\\b",

        //for:-  dd/MM/yy, dd-MM-yy, dd.MM.yy
        "\\b\\d{1,2}[./-]\\d{1,2}[./-]\\d{2}\\b",

        //for:-  yyyy/MM/dd, yyyy-MM-dd, yyyy.MM.dd
        "\\b\\d{4}[./-]\\d{1,2}[./-]\\d{1,2}\\b"

            //converts string to actual regex object  (case-insensitive and multiline search)
    ).map { it.toRegex(setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
    }

    //Find First matching date
    fun find(text: String): String? {
        val cleaned = text.replace("[–—−]".toRegex(), "-")
        //try each regex pattern , return first matched date
        for (regex in regexes) {
            regex.find(cleaned)?.let { return it.value }
        }
        return null
    }
}

