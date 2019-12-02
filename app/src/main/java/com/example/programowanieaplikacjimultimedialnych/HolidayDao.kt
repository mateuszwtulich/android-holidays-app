package com.example.programowanieaplikacjimultimedialnych

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.programowanieaplikacjimultimedialnych.Model.Location
import com.example.programowanieaplikacjimultimedialnych.Model.MultimediaPath
import com.example.programowanieaplikacjimultimedialnych.Model.Post
import java.nio.file.Path

@Dao
interface HolidayDao {

    @Query("Select * from post_table")
    fun getPosts(): LiveData<List<Post>>

    @Query("Select * from post_table where id=:postId")
    fun getPost(postId: Int): LiveData<Post>

    @Query("Select * from multimediaPath_table where post_id=:postId")
    fun getMultimediaPaths(postId: Int): List<MultimediaPath>

    @Query("Select * from location_table where id=:locationId")
    fun getLocation(locationId: Int): Location

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(path: MultimediaPath)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: Location)

    @Update
    suspend fun updatePost(post: Post)

    @Update
    suspend fun updatePath(path: Path)

    @Update
    suspend fun updateLocation(location: Location)

    @Delete
    suspend fun deletePost(post: Post)

    @Delete
    suspend fun deletePath(path: Path)

    @Delete
    suspend fun deleteLocation(location: Location)

    @Query("DELETE FROM post_table")
    suspend fun deleteAllPosts()

    @Query("DELETE FROM multimediaPath_table")
    suspend fun deletePaths()

    @Query("DELETE FROM location_table")
    suspend fun deleteLocations()
}