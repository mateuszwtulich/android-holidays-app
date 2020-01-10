package com.example.programowanieaplikacjimultimedialnych.controller_ui

import android.Manifest
import android.app.Activity
import android.app.Application
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import com.example.programowanieaplikacjimultimedialnych.R
import com.example.programowanieaplikacjimultimedialnych.view_model.HolidayViewModel
import com.example.programowanieaplikacjimultimedialnych.view_model.dto.PostDtoInput
import kotlinx.android.synthetic.main.fragment_new_post.*
import kotlinx.android.synthetic.main.fragment_new_post.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class NewPostFragment : Fragment() {

    private val holidayViewModel: HolidayViewModel = HolidayViewModel(application = Application())
    private var imagesPaths: ArrayList<String> = ArrayList()
    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_post, container, false)

        view.savePost.setOnClickListener {
            savePost()
        }

        view.addImage.setOnClickListener {
            addMultimedia()
        }

        view.images_viewpager.setOnClickListener {
            addMultimedia()
        }

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)


        view.text_input_date.setStartIconOnClickListener {
            addDate(calendar, year, month, day)
        }

        view.text_input_location.setStartIconOnClickListener {
            val intent = Intent (getActivity(), LocationSearch::class.java)
            getActivity()!!.startActivity(intent)
        }
        return view
    }

    fun savePost() {
        var resultCode: Int
        if (TextUtils.isEmpty(view!!.text_input_title.editText.toString()) ||
            TextUtils.isEmpty(view!!.text_input_description.editText.toString())
        ) {
            Toast.makeText(
                context, R.string.no_chars,
                Toast.LENGTH_LONG
            ).show()
            resultCode = Activity.RESULT_CANCELED
        } else {
            resultCode = Activity.RESULT_OK

            if (resultCode == Activity.RESULT_OK && !TextUtils.isEmpty(view!!.text_input_date.editText!!.text.toString())) {
                val title = view!!.text_input_title.editText!!.text.toString()

                val text = view!!.text_input_description.editText!!.text.toString()

                val localDate = LocalDate.parse(view!!.text_input_date.editText!!.text.toString(), formatter)
                //TODO DaTeTimeParseException
                val uri = imagesPaths

                    val post = PostDtoInput(
                        id = 0,
                        title = title,
                        text = text,
                        date = localDate,
                        uriList = uri
                    )

                    GlobalScope.launch { holidayViewModel.insert(post) }

            } else {
                Toast.makeText(
                    context, R.string.empty_not_saved,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun addMultimedia(){
        //check runtime permission
        if (checkSelfPermission(this.requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_DENIED
        ) {
            //permission denied
            val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            //show popup to request runtime permission
            requestPermissions(permissions, NewPostFragment.PERMISSION_CODE)
        } else {
            //permission already granted
            pickImageFromGallery()
        }
    }

    fun addDate(calendar: Calendar, year: Int, month: Int, day: Int){
        val datePicker = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { viewT, Tyear, Tmonth, Tday ->
//            view!!.text_input_date.dateText.setText("$Tday/$Tmonth/$Tyear")
            calendar.set(Tyear, Tmonth, Tday)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            view!!.text_input_date.dateText.setText(dateFormat.format(calendar.time))
        }, year, month, day)

        datePicker.show()
    }

    fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, NewPostFragment.IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imagesPaths.clear()
        if (resultCode == Activity.RESULT_OK && requestCode == NewPostFragment.IMAGE_PICK_CODE) {
            val count = data?.clipData?.itemCount
            if(count != null){
                for (i in 0..(count - 1) ){
                    imagesPaths.add(data.clipData?.getItemAt(i)?.uri.toString())
                }
            }
            if(count == null){
                imagesPaths.add(data?.data.toString())
            }
        }
        val adapter = ViewPagerAdapter(requireContext(), imagesPaths.map { path -> Uri.parse(path)},null,1)

        view!!.images_viewpager.adapter = adapter

        if(imagesPaths.count() > 1){                    //visibility może inaczej ?
            view!!.indicator.visibility = View.VISIBLE
            view!!.indicator.attachToPager(view!!.images_viewpager)
        }
        else
            view!!.indicator.visibility = View.INVISIBLE
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            NewPostFragment.PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        //image pick code
        private const val IMAGE_PICK_CODE = 1000
        //Permission code
        private const val PERMISSION_CODE = 1001
        @JvmStatic
        fun newInstance() = NewPostFragment()
    }
}
