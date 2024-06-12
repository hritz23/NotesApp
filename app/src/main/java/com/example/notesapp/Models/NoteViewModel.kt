package com.example.notesapp.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notesapp.Database.NoteDatabase
import com.example.notesapp.Database.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application){

    private val repository : NotesRepository
    val allNotes : LiveData<List<Notes>>

    init {
        val dao = NoteDatabase.getDatabase(application).getNoteDao()
        repository = NotesRepository(dao)
        allNotes = repository.allNotes
    }

    fun deleteNote(notes: Notes) = viewModelScope.launch(Dispatchers.IO) { repository.delete(notes) }

    fun insertNote(notes: Notes) = viewModelScope.launch(Dispatchers.IO) { repository.insert(notes) }
    fun updateNote(notes: Notes) = viewModelScope.launch(Dispatchers.IO) { repository.update(notes) }
}