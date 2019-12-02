package com.example.programowanieaplikacjimultimedialnych

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.programowanieaplikacjimultimedialnych.Model.Post
import com.example.programowanieaplikacjimultimedialnych.PostDto
import kotlinx.coroutines.launch

// Class extends AndroidViewModel and requires application as a parameter.
class WordViewModel(application: Application) : AndroidViewModel(application) {

    // The ViewModel maintains a reference to the repository to get data.
    private val repository: HolidayRepository

    val allPosts: LiveData<List<PostDto>>

    init {
        // Gets reference to WordDao from HolidayRoomDatabase to construct
        // the correct WordRepository.
        val holidayDao = HolidayRoomDatabase.getDatabase(application).holidayDao()
        repository = HolidayRepository(holidayDao)
        allPosts = getAlPosts()
    }

    fun getPost(postId: Int): LiveData<PostDto> {
        return Transformations.map(repository.getPost(postId) , {input -> PostDto(
            input.id,
            input.title,
            input.text,
            repository.getLocation(input.id),
            input.date,
            repository.getPaths(input.id))})

        }

    private fun getAlPosts() : LiveData<List<PostDto>> {
        return Transformations.map(repository.allPosts, {post -> post.map{ input -> PostDto(
            input.id,
            input.title,
            input.text,
            repository.getLocation(input.id),
            input.date,
            repository.getPaths(input.id))}})
    }

    fun insert(postDto: PostDto) = viewModelScope.launch {
        var post = Post(postDto.id, postDto.title, postDto.text, postDto.location.id, postDto.date)
        repository.insertPost(post)
        repository.insertLocation(postDto.location)
        postDto.multimediaPaths.forEach { path -> repository.insertPath(path)
    } }
}
