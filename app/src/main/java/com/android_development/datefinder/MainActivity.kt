package com.android_development.datefinder

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var tvResult : TextView
    private lateinit var tts : TextToSpeech

    /** ← NEW: single Activity‑Result contract that still opens the Gallery app */
    private val pickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val uri: Uri? = result.data?.data
                uri?.let { processImage(it) }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        tvResult = findViewById(R.id.tvResult)
        findViewById<Button>(R.id.btnPick).setOnClickListener { openGallery() }

        tts = TextToSpeech(this) { tts.language = Locale.US } // German voice
    }

    /** OPEN GALLERY exactly like before, just launch through the contract */
    private fun openGallery() {
        val intent = android.content.Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickImage.launch(intent)     // ← uses new API but same Gallery UI
    }

    // ---------- your helper functions stay the same ----------

    private fun processImage(uri: Uri) {
        val stream  = contentResolver.openInputStream(uri)
        val bitmap  = BitmapFactory.decodeStream(stream)
        val image   = InputImage.fromBitmap(bitmap, 0)
        val recog   = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recog.process(image)
            .addOnSuccessListener { vision ->
                val date  = DateExtract().find(vision.text)
                val msg   = date?.let { formatDate(it) } ?: "No date detected"
                speakAndShow(msg)
            }
            .addOnFailureListener { speakAndShow(null) }
    }

//     private fun formatDate(raw: String): String = try {
//         val input  = java.text.SimpleDateFormat("dd/MM/yy", Locale.US)
//         val output = java.text.SimpleDateFormat("d MMMM yyyy", Locale.US)
//         output.format(input.parse(raw)!!)
//     } catch (e: Exception) { raw }


    /** Converts raw OCR text into spoken “d MMMM yyyy”.
     *  Falls back to the raw text if no pattern matches.               */
    private fun formatDate(raw: String): String {

        // 1. Remove stray spaces / dots / commas the OCR might add.
        val cleaned = raw.trim()
            .replace("[^0-9/\\-]".toRegex(), "")   // keep only digits, / or -

        // 2. Output style: 1 July 2025
        val outFmt = java.text.SimpleDateFormat("d MMMM yyyy", Locale.US)

        // 3. Try patterns in order.
        val patterns = listOf(
            Regex("""^(\d{4})[/-](\d{1,2})[/-](\d{1,2})$"""), // yyyy/MM/dd
            Regex("""^(\d{1,2})[/-](\d{1,2})[/-](\d{4})$"""), // dd/MM/yyyy
            Regex("""^(\d{1,2})[/-](\d{1,2})[/-](\d{2})$""")  // dd/MM/yy
        )

        for (rx in patterns) {
            val m = rx.find(cleaned) ?: continue
            val (a, b, c) = m.destructured          // capture groups

            // 4. Map captures → day / month / year
            val (day, month, year) = when (rx) {
                patterns[0] -> Triple(c.toInt(), b.toInt(), a.toInt())          // yyyy/MM/dd
                patterns[1] -> Triple(a.toInt(), b.toInt(), c.toInt())          // dd/MM/yyyy
                else        -> Triple(a.toInt(), b.toInt(), 2000 + c.toInt())   // dd/MM/yy
            }

            // 5. Build a Calendar & format it.
            return java.util.Calendar.getInstance().apply {
                set(year, month - 1, day)   // Calendar month is 0‑based
            }.let { cal -> outFmt.format(cal.time) }
        }

        // 6. No match → return what OCR produced.
        return raw
    }





    private fun speakAndShow(text: String?) {
        val msg = text ?: "No date detected"
        tvResult.text = msg
        tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {
        tts.shutdown()
        super.onDestroy()
    }
}
