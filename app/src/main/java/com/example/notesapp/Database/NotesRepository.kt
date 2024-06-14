package com.example.notesapp.Database

import androidx.lifecycle.LiveData
import com.example.notesapp.Models.Notes

class NotesRepository(private val noteDao: NoteDao) {

    val allNotes: LiveData<List<Notes>> = noteDao.getAllNote()

    suspend fun insert(notes: Notes) {
        noteDao.insert(notes)
    }

    suspend fun delete(notes: Notes) {
        noteDao.delete(notes)
    }

    suspend fun update(notes: Notes) {
        noteDao.update(notes.id, notes.title, notes.note, notes.date)
    }
}