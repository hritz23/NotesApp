package com.example.notesapp.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.notesapp.Models.Notes


@Dao
interface NoteDao {

    @Insert
    suspend fun insert(notes: Notes)

    @Delete
    suspend fun delete(notes: Notes)

    @Query("select * from notes_table order by id ASC")
    fun getAllNote() : LiveData<List<Notes>>

    @Query("UPDATE notes_table SET title = :title, note = :note , date = :date WHERE id=:id")
    suspend fun update(id : Int?, title : String?, note : String?, date : String?)
}