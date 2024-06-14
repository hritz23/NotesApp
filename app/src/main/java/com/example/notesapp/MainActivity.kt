package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.Adapter.NotesAdapter
import com.example.notesapp.Database.NoteDatabase
import com.example.notesapp.Models.NoteViewModel
import com.example.notesapp.Models.Notes
import com.example.notesapp.databinding.ActivityMainBinding
import org.w3c.dom.Node

class MainActivity : AppCompatActivity() , NotesAdapter.NotesClickListener, PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database : NoteDatabase
    lateinit var viewModel: NoteViewModel
    lateinit var adapter: NotesAdapter
    lateinit var selectNode : Notes

    private val updateNote =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
            if (result.resultCode== RESULT_OK){
                val note = result.data?.getSerializableExtra("update_note") as? Notes
                if (note!=null){
                    viewModel.updateNote(note)
                }
            }

        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.main)

        initUi()

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModel::class.java)

        viewModel.allNotes.observe(this){ list->
            list?.let { adapter.updateList(list) }
        }

        database = NoteDatabase.getDatabase(this)

    }

    private fun initUi() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter = NotesAdapter(this, this)
        binding.recyclerView.adapter = adapter

        val getContent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val note = result.data?.getSerializableExtra("note") as? Notes
                    if (note != null) {
                        viewModel.insertNote(note)
                    }
                }
            }

        binding.fab.setOnClickListener {
            val intent = Intent(this, AddNote::class.java)
                getContent.launch(intent)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!=null){
                    adapter.filterList(newText)
                }
                return true
            }

        } )
    }

    override fun onItemClicked(notes: Notes) {
        val intent = Intent(this@MainActivity, AddNote::class.java)
        intent.putExtra("current_note", notes)
        updateNote.launch(intent)
    }

    override fun onLongItemClicked(notes: Notes, cardView: CardView) {
        selectNode = notes
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView){
        val popup = PopupMenu(this, cardView)
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.inflate(R.menu.popup_menu)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId==R.id.delete){
            viewModel.deleteNote(selectNode)
            return true
        }
        return false
    }
}