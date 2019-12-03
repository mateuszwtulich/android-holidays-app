package com.example.programowanieaplikacjimultimedialnych

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class NewPostActivity : AppCompatActivity() {

    private lateinit var editTitleView: EditText
    private lateinit var editTextView: EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_word)
        editTitleView = findViewById(R.id.editTitle)
        editTextView = findViewById(R.id.editText)

        val button = findViewById<Button>(R.id.button_save)

        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editTitleView.text) || TextUtils.isEmpty(editTextView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {

                val title = editTitleView.text.toString()
                val text = editTextView.text.toString()

                replyIntent.putExtra("title",title)
                replyIntent.putExtra("text",text)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }

    }

}