package com.example.mynoteapp.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mynoteapp.Database.NoteDatabase;
import com.example.mynoteapp.Entities.Note;
import com.example.mynoteapp.NoteListener;
import com.example.mynoteapp.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteListener {

    public static final int REQUEST_CODE_ADD_NOTE = 1;
    RecyclerView recyclerView;
    private List<Note> noteList;
    private NotesAdapter notesAdapter;
    private int onClickPosition = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rec_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        noteList = new ArrayList<>();
        notesAdapter = new NotesAdapter(noteList, this);
        recyclerView.setAdapter(notesAdapter);

    }

    private void getNotes(){
        class GetNoteTask extends AsyncTask<Void, Void, List<Note>>{


            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NoteDatabase.getNoteDatabase(getApplicationContext()).noteDao().getAllNote();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                Log.d("MainActivity", notes.toString());
                if (noteList.size() == 0){
                    noteList.addAll(notes);
                    notesAdapter.notifyDataSetChanged();
                }else{
                    notes.add(notes.get(0));
                    notesAdapter.notifyItemChanged(0);
                }

                recyclerView.smoothScrollToPosition(0);
            }

        }

        new GetNoteTask().execute();
    }



    public void FloatingBtnClick(View view) {
        startActivityForResult(new Intent(getApplicationContext(),NoteWriteActivity.class), REQUEST_CODE_ADD_NOTE);
    }

    @Override
    public void OnItemClick(Note note, int position) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK){
            getNotes();

        }
    }
}