package com.example.mynoteapp.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynoteapp.Database.NoteDatabase;
import com.example.mynoteapp.Entities.Note;
import com.example.mynoteapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteWriteActivity extends AppCompatActivity {

    EditText edtTitle, edtNote;
    TextView txtTitle, txtDateTime;
    Note intentNote;
    Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_write);

        edtTitle = findViewById(R.id.edt_title);
        edtNote = findViewById(R.id.edt_note);

        txtTitle = findViewById(R.id.txt_title);
        txtDateTime = findViewById(R.id.txt_date);
        btnDelete = findViewById(R.id.note_delete);

        txtDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a",
                        Locale.getDefault()).format(new Date())
                );

        if (getIntent().getBooleanExtra("isViewOrUpdate", false)){
            btnDelete.setVisibility(View.VISIBLE);
            intentNote = (Note) getIntent().getSerializableExtra("NoteObj");
            setViewOrUpdate();
        }
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote();
            }
        });
    }

    public void deleteNote(){
        class DeleteNoteTask extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids) {
                NoteDatabase.getNoteDatabase(getApplicationContext()).noteDao().deleteNote(intentNote);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                intent.putExtra("isNoteDeleted", true);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
       new DeleteNoteTask().execute();
    }

    public void setViewOrUpdate(){
        edtNote.setText(intentNote.getNoteText());
        edtTitle.setText(intentNote.getTitle());
        txtDateTime.setText(intentNote.getDateTime());
    }

    public void SaveOnClick(View view) {
        saveNote();
    }

    private void saveNote(){
        if (edtTitle.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Title Empty",Toast.LENGTH_SHORT).show();
        }else if (edtNote.getText().toString().trim().isEmpty()){
            Toast.makeText(this,"Empty note text", Toast.LENGTH_SHORT).show();
        }

        final Note note = new Note();
        note.setTitle(edtTitle.getText().toString());
        note.setNoteText(edtNote.getText().toString());
        note.setDateTime(txtDateTime.getText().toString());

        if (intentNote != null){
            note.setId(intentNote.getId());
        }

        class SaveNoteTask extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids) {
                NoteDatabase.getNoteDatabase(getApplicationContext()).noteDao().insertNote(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }

        new SaveNoteTask().execute();

    }

}

