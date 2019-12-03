package com.example.programowanieaplikacjimultimedialnych.DataBase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.programowanieaplikacjimultimedialnych.Model.Post
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

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

    fun getPost(postId: Int): LiveData<PostDto> {
        return Transformations.map(repository.getPost(postId) , {input ->
            PostDto(
                input.id,
                input.title,
                input.text,
                repository.getLocation(input.id),
                SimpleDateFormat("dd/MM/yyyy").parse(input.date),
                repository.getPaths(input.id)
            )
        })

        }

    private fun getPosts() : LiveData<List<PostDto>> {
        return Transformations.map(repository.allPosts, {post -> post.map{ input ->
            PostDto(
                input.id,
                input.title,
                input.text,
                repository.getLocation(input.id),
                SimpleDateFormat("dd/MM/yyyy").parse(input.date),
                repository.getPaths(input.id)
            )
        }})
    }

    //do naprawy
    fun insert(postDto: PostDto) = viewModelScope.launch {
        repository.insertLocation(postDto.location)
        var post = Post(0, postDto.title, postDto.text, postDto.location.id, postDto.date.toString())
        repository.insertPost(post)
        postDto.multimediaPaths.forEach { path -> repository.insertPath(path)
    } }
}
