package com.android_development.datefinder

class DateExtract {

        private val regexes = listOf(
            "\\b\\d{1,2}[/-]\\d{1,2}[/-]\\d{2,4}\\b",
            "\\b\\d{4}[/-]\\d{1,2}[/-]\\d{1,2}\\b"
        ).map { it.toRegex() }



        fun find(text: String): String? {
            regexes.forEach { r ->
                r.find(text)?.let { return it.value }
            }
            return null
        }

        companion object {
            // helper for quick static-style access
            fun find(text: String): String? = DateExtract().find(text)
        }

}