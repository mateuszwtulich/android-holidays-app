package com.example.programowanieaplikacjimultimedialnych.controller_ui

import android.Manifest
import android.app.Activity
import android.app.Application
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.programowanieaplikacjimultimedialnych.view_model.HolidayViewModel
import com.example.programowanieaplikacjimultimedialnych.view_model.dto.Location
import com.example.programowanieaplikacjimultimedialnych.view_model.dto.PostDtoInput
import com.example.programowanieaplikacjimultimedialnych.view_model.dto.PostDtoOutput
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_new_post.*
import kotlinx.android.synthetic.main.fragment_new_post.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class EditFragment : Fragment() {

    private lateinit var postDtoOutput: PostDtoOutput
    private val holidayViewModel: HolidayViewModel = HolidayViewModel(application = Application())
    private var imagesPaths: ArrayList<String> = ArrayList()
    private var markerOptions: MarkerOptions = MarkerOptions()
    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(com.example.programowanieaplikacjimultimedialnych.R.layout.fragment_new_post, container, false)

        if (arguments != null) {
            postDtoOutput = arguments?.getParcelable("post")!!

            view!!.text_input_title.editText!!.setText(postDtoOutput.title, TextView.BufferType.EDITABLE)
            view.text_input_description.editText!!.setText(postDtoOutput.text, TextView.BufferType.EDITABLE)
            view.text_input_date.editText!!.setText(formatter.format(postDtoOutput.date))

            val adapter = ViewPagerAdapter(requireContext(), postDtoOutput.uriList, null, 1,0)
            view.images_viewpager.adapter = adapter
            val list = ArrayList<String>()
            postDtoOutput.uriList.forEach{  elem  -> list.add(elem.toString())}
            imagesPaths = list

            val  marker = MarkerOptions()
            marker.position(LatLng(postDtoOutput.location.latitude,postDtoOutput.location.longitude))
            markerOptions = marker

            val geocoder = Geocoder(context)
            val adresses = geocoder.getFromLocation(postDtoOutput.location.latitude, postDtoOutput.location.longitude, 1)[0]

            view.text_input_location.editText!!.setText(adresses.locality.toString())

            if (imagesPaths.count() > 1) {
                view.indicator.visibility = View.VISIBLE
                view.indicator.attachToPager(view.images_viewpager)
            } else
                view.indicator.visibility = View.INVISIBLE


            view.savePost.setOnClickListener {
                savePost()
            }

            view.addImage.setOnClickListener {
                addMultimedia()
            }

            view.select_image.setOnClickListener {
                addMultimedia()
            }

            val calendar = Calendar.getInstance()
            val date = postDtoOutput.date
            val year = date.year
            val month = date.monthValue
            val day = date.dayOfMonth

            view.text_input_date.setStartIconOnClickListener {
                addDate(calendar, year, month, day)
            }

            view.text_input_location.setStartIconOnClickListener {
                val intent = Intent(getActivity(), LocationSearch::class.java)

                if(markerOptions.title != null) intent.putExtra("markerOptions", markerOptions)
                startActivityForResult(intent, EditFragment.LOCATION_CODE)
            }

        }

        return view
    }

    fun savePost() {
        if (!TextUtils.isEmpty(view!!.text_input_date.editText!!.text.toString()) && !TextUtils.isEmpty(view!!.text_input_title.editText!!.text.toString())
            && !TextUtils.isEmpty(view!!.text_input_description.editText!!.text.toString()) && !TextUtils.isEmpty(view!!.text_input_location.editText!!.text.toString())
            && imagesPaths.size > 0
        ) {
            val title = view!!.text_input_title.editText!!.text.toString()
            val text = view!!.text_input_description.editText!!.text.toString()
            val localDate = LocalDate.parse(view!!.text_input_date.editText!!.text.toString(), formatter)
            val uri = imagesPaths
            val location = Location(markerOptions.position.latitude, markerOptions.position.longitude)

            val post = PostDtoInput(
                id = postDtoOutput.id,
                title = title,
                text = text,
                date = localDate,
                uriList = uri,
                location = location
            )
            GlobalScope.launch {
                holidayViewModel.update(post)

            }

            (activity as MainActivity).popStacks(2)

        } else {
            Toast.makeText(
                context, com.example.programowanieaplikacjimultimedialnych.R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun addMultimedia() {
        if (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_DENIED
        ) {
            val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permissions, EditFragment.PERMISSION_CODE)
        } else {
            pickImageFromGallery()
        }
    }

    fun addDate(calendar: Calendar, year: Int, month: Int, day: Int) {
        val datePicker = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { viewT, Tyear, Tmonth, Tday ->
            calendar.set(Tyear, Tmonth, Tday)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            view!!.text_input_date.dateText.setText(dateFormat.format(calendar.time))
        }, year, month, day)

        datePicker.show()
    }

    fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, EditFragment.IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == EditFragment.LOCATION_CODE) {
            markerOptions = data!!.getParcelableExtra("localization")
            text_input_location.locationText.setText(markerOptions.title.toString())
        }
        if (resultCode == Activity.RESULT_OK && requestCode == EditFragment.IMAGE_PICK_CODE) {
            imagesPaths.clear()
            val count = data?.clipData?.itemCount
            if (count != null) {
                for (i in 0..(count - 1)) {
                    imagesPaths.add(data.clipData?.getItemAt(i)?.uri.toString())
                }
            }
            if (count == null) {
                imagesPaths.add(data?.data.toString())
            }
            val adapter = ViewPagerAdapter(requireContext(), imagesPaths.map { path -> Uri.parse(path) }, null, 1,images_viewpager.currentItem)

            view!!.images_viewpager.adapter = adapter

            if (imagesPaths.count() > 1) {
                view!!.indicator.visibility = View.VISIBLE
                view!!.indicator.attachToPager(view!!.images_viewpager)
            } else
                view!!.indicator.visibility = View.INVISIBLE
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            EditFragment.PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
        private const val LOCATION_CODE = 1002
        @JvmStatic
        fun newInstance() = EditFragment()
    }
}