package com.jwhh.notekeeper

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notekeeper.DataManager
import com.example.notekeeper.NOTE_POSITION
import com.example.notekeeper.NoteActivity
import com.example.notekeeper.R

class NoteRecyclerAdapter(val context: Context) : RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder>()
{
    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_note, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return DataManager.notes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = DataManager.notes[position]
        holder.textCourse.text = note.course?.title
        holder.textTitle.text = note.title
        holder.currentPosition = position
        holder.color.setBackgroundColor(note.color)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textCourse: TextView = itemView.findViewById(R.id.textCourse)
        val textTitle: TextView = itemView.findViewById(R.id.textTitle)
        var currentPosition = 0
        var color: View = itemView.findViewById(R.id.noteColor)

        init {
            itemView.setOnClickListener {
                val intent = Intent(context, NoteActivity::class.java)
                intent.putExtra(NOTE_POSITION, currentPosition)
                context.startActivity(intent)
            }
        }
    }
}