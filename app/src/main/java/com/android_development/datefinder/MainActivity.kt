package com.android_development.datefinder

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import java.util.Locale
import com.google.mlkit.vision.text.latin.TextRecognizerOptions




class MainActivity : AppCompatActivity() {

    private lateinit var tvResult: TextView
    private lateinit var tts: TextToSpeech
    private val pickCode = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        tvResult = findViewById(R.id.tvResult)
        findViewById<Button>(R.id.btnPick).setOnClickListener { openGallery() }

        tts = TextToSpeech(this) { tts.language = Locale.US }
    }

    /** open gallery to pick an image */
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        //********
        startActivityForResult(intent, pickCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickCode && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri -> processImage(uri) }
        }
    }

    private fun formatDate(raw: String): String {
        return try {
            val input = java.text.SimpleDateFormat("dd/MM/yy", Locale.US)
            val output = java.text.SimpleDateFormat("d MMMM yyyy", Locale.US)
            val date = input.parse(raw)
            output.format(date!!)
        } catch (e: Exception) {
            raw  // fallback: just return original if it fails
        }
    }


    /** run ML Kit OCR and extract date */
    private fun processImage(uri: Uri) {
        val stream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(stream)
        val image = InputImage.fromBitmap(bitmap, 0)

        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)


        recognizer.process(image)
            .addOnSuccessListener { visionText ->
              //  val date = DateExtract.find(visionText.text)

                val extractor = DateExtract()
                val date = extractor.find(visionText.text)
                val finalText = date?.let { formatDate(it) } ?: "No date detected"
                speakAndShow(finalText)

                speakAndShow(date)
                /*
                val extractor = DateExtract()
val rawDate = extractor.find(visionText.text)



                 */
            }
            .addOnFailureListener {
                speakAndShow(null)
            }
    }

    /** show result and speak it */
    private fun speakAndShow(date: String?) {
        val msg = date ?: "No date detected"
        tvResult.text = msg
        tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {
        tts.shutdown()
        super.onDestroy()
    }
}
