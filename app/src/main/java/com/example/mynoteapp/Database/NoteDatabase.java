package com.example.mynoteapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mynoteapp.Dao.NoteDao;
import com.example.mynoteapp.Entities.Note;

@Database(entities = Note.class, version = 1, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase noteDatabase;
    public static synchronized NoteDatabase getNoteDatabase(Context context){
        if (noteDatabase == null){
            noteDatabase = Room.databaseBuilder(
                    context,
                    NoteDatabase.class,
                    "notes db"
            ).build();
        }
        
        return noteDatabase;
    }

    public abstract NoteDao noteDao();

}
