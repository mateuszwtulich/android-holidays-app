package com.example.programowanieaplikacjimultimedialnych

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.programowanieaplikacjimultimedialnych.Model.Location
import com.example.programowanieaplikacjimultimedialnych.Model.MultimediaPath
import com.example.programowanieaplikacjimultimedialnych.Model.Post
import com.example.programowanieaplikacjimultimedialnych.HolidayDao
import java.nio.file.Path

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class HolidayRepository(private val holidayDao: HolidayDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.

    val allPosts: LiveData<List<Post>> = holidayDao.getPosts()

    fun getPost(postId: Int): LiveData<Post> = holidayDao.getPost(postId)

    fun getPaths(postId: Int): List<MultimediaPath> = holidayDao.getMultimediaPaths(postId)

    fun getLocation(postId: Int): Location = holidayDao.getLocation(postId)

    suspend fun insertPost(post: Post) {
        holidayDao.insert(post)
    }

    suspend fun insertPath(path: MultimediaPath) {
        holidayDao.insert(path)
    }

    suspend fun insertLocation(location: Location) {
        holidayDao.insert(location)
    }

    suspend fun deletePost(post: Post){
        holidayDao.deletePost(post)
    }

    suspend fun deletePath(path: Path){
        holidayDao.deletePath(path)
    }

    suspend fun deleteLocation(location: Location){
        holidayDao.deleteLocation(location)
    }


}