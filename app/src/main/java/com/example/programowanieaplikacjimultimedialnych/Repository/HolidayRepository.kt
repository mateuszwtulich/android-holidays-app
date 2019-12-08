package com.example.programowanieaplikacjimultimedialnych.database

import androidx.lifecycle.LiveData
import com.example.programowanieaplikacjimultimedialnych.model.MultimediaPath
import com.example.programowanieaplikacjimultimedialnych.model.Post


// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class HolidayRepository(private val holidayDao: HolidayDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.

    val allPosts: LiveData<List<Post>> = holidayDao.getPosts()

    fun getPost(postId: Int): LiveData<Post> = holidayDao.getPost(postId)

    suspend fun getMulitmediaPaths(postId: Int): List<MultimediaPath> = holidayDao.getMulitmediaPaths(postId)

    suspend fun insertPost(post: Post):Long {
        return holidayDao.insert(post)
    }

    suspend fun insertPath(path: MultimediaPath) {
        holidayDao.insert(path)
    }

    suspend fun deletePost(post: Post){
        holidayDao.deletePost(post)
    }

    suspend fun deletePath(path: MultimediaPath){
        holidayDao.deletePath(path)
    }

    suspend fun deleteAllPost(){
        holidayDao.deleteAllPosts()
    }

    suspend fun deleteAllPath(){
        holidayDao.deleteAllPaths()
    }


}