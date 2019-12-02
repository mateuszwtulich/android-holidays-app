package com.example.programowanieaplikacjimultimedialnych

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HolidayDao {

    //Crud

    @Query("SELECT * from post_table  post join location_table  loc on loc.id = post.location_id join multimediaPath_table multi on post.id = multi.post_id")
    fun getPosts(): LiveData<List<Post>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word)

    @Query("DELETE FROM word_table")
    suspend fun deleteAll()




}