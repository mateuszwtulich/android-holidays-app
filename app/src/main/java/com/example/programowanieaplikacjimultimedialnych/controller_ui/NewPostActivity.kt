package com.example.programowanieaplikacjimultimedialnych.controller_ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.example.programowanieaplikacjimultimedialnych.R
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_new_post.*


class NewPostActivity : AppCompatActivity() {

    private lateinit var textInputTitle: TextInputLayout
    private lateinit var textInputDescripton: TextInputLayout
//    private lateinit var adapter: ImageListAdapter
    private var imagePath: ArrayList<String> = ArrayList()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_new_post)
        textInputDescripton = findViewById(R.id.text_input_description)
        textInputTitle = findViewById(R.id.text_input_title)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        savePost.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(textInputTitle.editText.toString()) || TextUtils.isEmpty(textInputDescripton.editText.toString())) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val title = textInputTitle.editText.toString()
                val text = textInputDescripton.editText.toString()

                replyIntent.putExtra("title", title)
                replyIntent.putExtra("text", text)
                replyIntent.putStringArrayListExtra("image",imagePath)
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
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                //show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE)
            } else {
                //permission already granted
                pickImageFromGallery()
            }
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent,IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private const val IMAGE_PICK_CODE = 1000
        //Permission code
        private const val PERMISSION_CODE = 1001
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
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
        imagePath.clear()
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val count = data?.clipData?.itemCount
            if(count != null){
                for (i in 0..(count - 1) ){
                    imagePath.add(data.clipData?.getItemAt(i)?.uri.toString())
                }
            }
            if(count == null){
                    imagePath.add(data?.data.toString())
            }
        }
    }
}