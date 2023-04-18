package com.example.noteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.example.noteapp.databinding.ActivityListNotesBinding
import com.example.noteapp.databinding.NoteBinding
import kotlin.concurrent.thread

class ListNotesActivity : AppCompatActivity() {
    lateinit var binding: ActivityListNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityListNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onResume() {
        super.onResume()

        refreshNotes()
    }

    fun refreshNotes(){
        val db = Room.databaseBuilder(this, NoteDatabase::class.java, "notes").build()

        Thread {
            val notes = db.noteDao().list()
            runOnUiThread{
                refreshUI(notes)
            }
        }.start()
    }

    fun refreshUI(notes: List<Note>){

        notes.forEach{
            val note = NoteBinding.inflate(layoutInflater)

            note.textTitle.text = it.title
            note.textDesc.text = it.desc

            binding.container.addView(note.root)
        }
    }
}