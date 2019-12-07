package com.example.programowanieaplikacjimultimedialnych.ControllerUI

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.programowanieaplikacjimultimedialnych.R
import kotlinx.android.synthetic.main.activity_new_word.*


class NewPostActivity : AppCompatActivity() {

    private lateinit var editTitleView: EditText
    private lateinit var editTextView: EditText
    private var imagePath: Uri? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_word)
        editTitleView = findViewById(R.id.editTitle)
        editTextView = findViewById(R.id.editText)

        val button = findViewById<Button>(R.id.button_save)

        button_save.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editTitleView.text) || TextUtils.isEmpty(editTextView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {

                val title = editTitleView.text.toString()
                val text = editTextView.text.toString()

                replyIntent.putExtra("title", title)
                replyIntent.putExtra("text", text)
                replyIntent.putExtra("image",imagePath.toString())
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }

        addImage.setOnClickListener {
            //check runtime permission
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED
            ) {
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                //show popup to request runtime permission
                requestPermissions(permissions,
                    PERMISSION_CODE
                );
            } else {
                //permission already granted
                pickImageFromGallery();
            }
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,
            IMAGE_PICK_CODE
        )
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imagePath = data?.data
        }
    }
}