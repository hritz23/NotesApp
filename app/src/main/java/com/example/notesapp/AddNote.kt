package com.example.notesapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notesapp.Models.Notes
import com.example.notesapp.databinding.ActivityAddNoteBinding
import com.example.notesapp.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.logging.SimpleFormatter

class AddNote : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var note : Notes
    private lateinit var oldNote : Notes
    var isUpdate = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            oldNote = intent.getSerializableExtra("current_note") as Notes
            binding.etTitle.setText(oldNote.title)
            binding.etNote.setText(oldNote.note)
            isUpdate = true
        } catch (e : Exception){
            e.printStackTrace()
        }

        binding.icCheck.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val note_desc = binding.etNote.text.toString()

            if (title.isNotEmpty() && note_desc.isNotEmpty()){
                val formatter = SimpleDateFormat("EEE, d MMM YYYY HH:mm a" )
                if (isUpdate){
                    note = Notes(oldNote.id,title,note_desc,formatter.format(Date()))
                } else{
                    note = Notes(null,title,note_desc,formatter.format(Date()))
                }

                val intent = Intent()
                intent.putExtra("update_note", note)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else{
                Toast.makeText(this@AddNote, "Please Enter Data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
        binding.icBack.setOnClickListener {
            onBackPressed()
        }
    }
}