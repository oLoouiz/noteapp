package com.example.noteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.example.noteapp.databinding.ActivityNewNoteBinding


class NewNoteActivity : AppCompatActivity() {

    lateinit var binding: ActivityNewNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonInsert.setOnClickListener {
            insert()
        }
    }

    fun insert(){
        //obter uma instância do room (database)
        val db = Room.databaseBuilder(this, NoteDatabase::class.java, "notes").build()

        //Criando uma instancia de entidade
        val note = Note(title = binding.editTitle.text.toString(), desc = binding.editDesc.text.toString())

        // chamando a função do DAO para inserção

        Thread {
            db.noteDao().insert(note)

        }.start()
    }
}