package com.example.noteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.example.noteapp.databinding.ActivityNewNoteBinding


class NewNoteActivity : AppCompatActivity() {

    lateinit var binding: ActivityNewNoteBinding
    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getIntExtra("id", 0)
        val title = intent.getStringExtra("title")
        val desc = intent.getStringExtra("desc")

        if(id > 0){
            binding.editTitle.setText(title)
            binding.editDesc.setText(desc)
            binding.buttonInsert.text = "Editar"


        }

        binding.buttonInsert.setOnClickListener {
            insert()
        }

        binding.buttonUltimo.setOnClickListener {
            binding.editTitle.setText(Session.lastTitle)
            binding.editDesc.setText(Session.lastDesc)
        }
    }

    fun insert(){
        //obter uma instância do room (database)
        val db = Room.databaseBuilder(this, NoteDatabase::class.java, "notes").build()

        //Criando uma instancia de entidade
        val note = Note(title = binding.editTitle.text.toString(), desc = binding.editDesc.text.toString())

        if (id > 0){
            note.id = id

        }

        // chamando a função do DAO para inserção

        Thread {
            if(id > 0){
                db.noteDao().update(note)
            }
            else {
                db.noteDao().insert(note)
                Session.lastTitle = note.title
                Session.lastDesc = note.desc
            }
            finish()

        }.start()
    }
}