package com.example.mynoteapp.View;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynoteapp.Entities.Note;
import com.example.mynoteapp.NoteListener;
import com.example.mynoteapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHoder> {

    private List<Note> note_list;
    private NoteListener noteListener;
    private List<Note> searchNote;
    Timer timer;

    public NotesAdapter(List<Note> note_list, NoteListener noteListener) {
        this.note_list = note_list;
        this.noteListener = noteListener;
        searchNote = note_list;
    }

    @NonNull
    @Override
    public NoteViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view, parent, false);
        return new NoteViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHoder holder, int position) {

        holder.setNote(note_list.get(position));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteListener.OnItemClick(note_list.get(position), position);
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return note_list.size();
    }

    static class NoteViewHoder extends RecyclerView.ViewHolder{

        TextView txtTitle, txtDate, txtNote;
        CardView cardView;

        public NoteViewHoder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txt_note_title);
            txtDate = itemView.findViewById(R.id.txt_note_date);
            txtNote = itemView.findViewById(R.id.txt_note_text);
            cardView = itemView.findViewById(R.id.card_view);

        }

        void setNote(Note note) {
            txtTitle.setText(note.getTitle());
            txtDate.setText(note.getDateTime());
            txtNote.setText(note.getNoteText());
        }
    }
    public void SearchNote(final String searchString){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(searchString.trim().isEmpty()){
                    note_list = searchNote;
                }else{
                    ArrayList<Note> temp = new ArrayList<>();
                    for (Note note:searchNote){
                        if (note.getTitle().toLowerCase().contains(searchString.toLowerCase()) || note.getNoteText().toLowerCase().contains(searchString.toLowerCase())){
                            temp.add(note);
                        }
                        note_list = temp;
                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    });
                }
            }
        }, 500);
    }

    public void cancelTimer(){
        if(timer != null){
            timer.cancel();
        }
    }

}
