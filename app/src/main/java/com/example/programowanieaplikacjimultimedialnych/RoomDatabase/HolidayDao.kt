package com.example.programowanieaplikacjimultimedialnych.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.programowanieaplikacjimultimedialnych.model.MultimediaPath
import com.example.programowanieaplikacjimultimedialnych.model.Post


@Dao
interface HolidayDao {

    @Query("Select * from post_table")
    fun getPosts(): LiveData<List<Post>>

    @Query("Select * from post_table where id=:postId")
    fun getPost(postId: Int): LiveData<Post>

    @Query("Select * from multimediaPath_table where post_id=:postId")
    fun getMultimediaPaths(postId: Int): LiveData<List<MultimediaPath>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun  insert(post: Post):Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(path: MultimediaPath)

    @Update
    suspend fun updatePost(post: Post)

    @Update
    suspend fun updatePath(path: MultimediaPath)

    @Delete
    suspend fun deletePost(post: Post)

    @Delete
    suspend fun deletePath(path: MultimediaPath)

    @Query("DELETE FROM post_table")
    suspend fun deleteAllPosts()

    @Query("DELETE FROM multimediaPath_table")
    suspend fun deletePaths()

}