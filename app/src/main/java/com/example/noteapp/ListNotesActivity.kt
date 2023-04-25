package com.example.noteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.noteapp.databinding.ActivityListNotesBinding
import com.example.noteapp.databinding.NoteBinding
import kotlin.concurrent.thread

class ListNotesActivity : AppCompatActivity() {
    lateinit var binding: ActivityListNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityListNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener{
            val i = Intent(this, NewNoteActivity::class.java)
            startActivity(i)
        }


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

        binding.container.removeAllViews()

        notes.forEach{ currentNote ->
            val noteBinding = NoteBinding.inflate(layoutInflater)

            noteBinding.textTitle.text = currentNote.title
            noteBinding.textDesc.text = currentNote.desc

            noteBinding.imageDelete.setOnClickListener{
                confirmDelete(currentNote)
            }

            noteBinding.root.setOnClickListener{
                val i = Intent(this, NewNoteActivity::class.java)

                i.putExtra("id", currentNote.id)
                i.putExtra("title", currentNote.title)
                i.putExtra("desc", currentNote.desc)

                startActivity(i)
            }

            binding.container.addView(noteBinding.root)
        }
    }

    fun confirmDelete(note: Note){
        AlertDialog.Builder(this)
            .setTitle("Confirmar Exclusão")
            .setMessage("Deseja excluir ${note.title}? permantentemente?")
            .setNegativeButton("Não", null)
            .setPositiveButton("Sim") {dialog, op ->
                delete(note)
            }
            .create()
            .show()
    }

    fun delete(note: Note){
        Thread {
            val db = Room.databaseBuilder(this,
                NoteDatabase::class.java,
                "notes")
                .build()

            db.noteDao().delete(note)
            refreshNotes()
        }.start()

    }
}