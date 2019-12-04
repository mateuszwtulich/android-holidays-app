package com.example.programowanieaplikacjimultimedialnych.database

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.programowanieaplikacjimultimedialnych.model.MultimediaPath
import com.example.programowanieaplikacjimultimedialnych.model.Post
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


// Class extends AndroidViewModel and requires application as a parameter.
class HolidayViewModel(application: Application) : AndroidViewModel(application) {

    // The ViewModel maintains a reference to the repository to get data.
    private val repository: HolidayRepository

    val allPosts: LiveData<List<PostDto>>


    init {
        // Gets reference to WordDao from HolidayRoomDatabase to construct
        // the correct WordRepository.
        val holidayDao = HolidayRoomDatabase.getDatabase(application).holidayDao()
        repository = HolidayRepository(holidayDao)
        allPosts = getPosts()
    }


    //multimedia path to uri
    fun getPost(postId: Int): LiveData<PostDto> {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        return Transformations.map(repository.getPost(postId)) { input ->
            PostDto(
                input.id,
                input.title,
                input.text,
                Pair(input.latitude,input.attitude),
                LocalDate.parse(input.date,formatter),
                uriList
            )
        }

    }

    private fun getPosts() : LiveData<List<PostDto>> {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        return Transformations.map(repository.allPosts) { post -> post.map{ input ->
            PostDto(
                input.id,
                input.title,
                input.text,
                Pair(input.latitude,input.attitude),
                LocalDate.parse(input.date,formatter),
                listUri
            )
        }}
    }

    //do naprawy
    fun insert(postDto: PostDto) = viewModelScope.launch {
        var post = Post(0, postDto.title, postDto.text,
            postDto.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            postDto.location.first,postDto.location.second)
        val id = repository.insertPost(post).toInt()
        postDto.uriList.forEach { path -> repository.insertPath(MultimediaPath(0,path.toString(),id)) }
    }
}
