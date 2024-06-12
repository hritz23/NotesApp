package com.example.notesapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.Models.Notes
import com.example.notesapp.R
import kotlin.random.Random

class NotesAdapter(private val context: Context, val listener: NotesClickListener) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val noteList= ArrayList<Notes>()
    private val fullList= ArrayList<Notes>()

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notesLayout: CardView = itemView.findViewById(R.id.cvItemCardView)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvNote: TextView = itemView.findViewById(R.id.tvNote)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = noteList[position]
        holder.tvTitle.text = currentNote.title
        holder.tvTitle.isSelected = true
        holder.tvNote.text = currentNote.note
        holder.tvDate.text = currentNote.date
        holder.tvDate.isSelected = true

        holder.notesLayout.setCardBackgroundColor(holder.itemView.resources.getColor(randomColor(), null))
        holder.notesLayout.setOnClickListener {
            listener.onItemClicked(noteList[holder.adapterPosition])
        }
        holder.notesLayout.setOnLongClickListener {
            listener.onLongItemClicked(noteList[holder.adapterPosition], holder.notesLayout)
            true
        }
    }

    private fun randomColor(): Int {
        val colorList = ArrayList<Int>()
        colorList.add(R.color.NoteColor1)
        colorList.add(R.color.NoteColor2)
        colorList.add(R.color.NoteColor3)
        colorList.add(R.color.NoteColor4)
        colorList.add(R.color.NoteColor5)
        colorList.add(R.color.NoteColor6)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(colorList.size)

        return colorList[randomIndex]
    }

    fun updateList(newList : List<Notes>){
        fullList.clear()
        fullList.addAll(newList)

        noteList.clear()
        noteList.addAll(fullList)

        notifyDataSetChanged()
    }

    fun filterList(s : String){
        noteList.clear()
        for (item in fullList){
            if (item.title?.lowercase()?.contains(s.lowercase())==true ||
                item.note?.lowercase()?.contains(s.lowercase())==true){
                noteList.add(item)
            }
        }
        notifyDataSetChanged()
    }

    interface NotesClickListener {
        fun onItemClicked(notes: Notes)
        fun onLongItemClicked(notes: Notes, cardView: CardView)
    }
}