package com.example.programowanieaplikacjimultimedialnych.view_model

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.programowanieaplikacjimultimedialnych.view_model.dto.PostDtoInput
import com.example.programowanieaplikacjimultimedialnych.repository.HolidayRepository
import com.example.programowanieaplikacjimultimedialnych.room_database.HolidayRoomDatabase
import com.example.programowanieaplikacjimultimedialnych.view_model.dto.PostDtoOutput
import com.example.programowanieaplikacjimultimedialnych.room_database.model.MultimediaPath
import com.example.programowanieaplikacjimultimedialnych.room_database.model.Post
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Class extends AndroidViewModel and requires application as a parameter.
class HolidayViewModel(application: Application) : AndroidViewModel(application) {

    // The ViewModel maintains a reference to the repository to get data.
    private val repository: HolidayRepository
    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    val allPosts: LiveData<List<PostDtoOutput>>


    init {
        // Gets reference to WordDao from HolidayRoomDatabase to construct
        // the correct WordRepository.
        val holidayDao = HolidayRoomDatabase.getDatabase(application).holidayDao()
        repository = HolidayRepository(holidayDao)
        allPosts = getPosts()
    }

    private fun getMultimediaPaths(id: Int): List<MultimediaPath> = runBlocking{
       repository.getMulitmediaPaths(id)
    }

    fun getPost(postId: Int): LiveData<PostDtoOutput> {

        return Transformations.map(repository.getPost(postId)) { input ->
            val uriList = mutableListOf<Uri>()
            val list = getMultimediaPaths(input.id)
            list.forEach {multimediaPath -> uriList.add(Uri.parse(multimediaPath.path))}
            PostDtoOutput(
                input.id,
                input.title,
                input.text,
                Pair(input.latitude, input.attitude),
                LocalDate.parse(input.date, formatter),
                uriList)
        }
    }

     private fun getPosts(): LiveData<List<PostDtoOutput>> {
        return Transformations.map(repository.allPosts) { post ->
            post.map { input ->
                val uriList = mutableListOf<Uri>()
                getMultimediaPaths(input.id).forEach {multimediaPath -> uriList.add(Uri.parse(multimediaPath.path))}
                PostDtoOutput(
                    input.id,
                    input.title,
                    input.text,
                    Pair(input.latitude, input.attitude),
                    LocalDate.parse(input.date, formatter),
                    uriList)
            }
        }
    }

    //run blocking bo live data zaczyna pobierać dane a lsita jeszcze się nie wrzuciłą do bayz XDDDDD
    fun insert(postDto: PostDtoInput) = runBlocking {
        val post = Post(
            0, postDto.title, postDto.text,
            postDto.date.format(formatter),
            postDto.location.first, postDto.location.second
        )
        val id = repository.insertPost(post).toInt()
        postDto.uriList.forEach { path -> repository.insertPath(MultimediaPath(0, path, id)) }
    }
}
