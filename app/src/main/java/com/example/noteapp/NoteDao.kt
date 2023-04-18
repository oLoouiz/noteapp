package com.example.noteapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface NoteDao {
    @Insert
    fun insert(note: Note)

    @Query("SELECT * FROM Note")
    fun list():List<Note>

    /*
    @Delete
    fun delete(note: Note)

    @Query("SELECT * FROM Note WHERE :id LIMIT 1")
    fun get(id: Int): Note

    */
}
