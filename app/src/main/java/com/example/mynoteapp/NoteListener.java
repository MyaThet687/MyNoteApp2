package com.example.mynoteapp;

import com.example.mynoteapp.Entities.Note;

public interface NoteListener {

    void OnItemClick(Note note, int position);

}
