package com.example.haletuyen.noteapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.haletuyen.noteapp.DatabaseHelper;
import com.example.haletuyen.noteapp.ItemNoteAdapter;
import com.example.haletuyen.noteapp.model.Note;
import com.example.haletuyen.noteapp.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ItemNoteAdapter adapter;
    private List<Note> listNote = new ArrayList<>();

    private Context context;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        db = new DatabaseHelper(context);
        connectView();
    }


    private void connectView() {
        findViewById(R.id.fab).setOnClickListener(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_note);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ItemNoteAdapter(context, listNote);
        recyclerView.setAdapter(adapter);
    }


    public void onResume() {
        super.onResume();
        updateListNote();
    }


    private void updateListNote() {
        listNote.clear();
        ArrayList<Note> ls = db.getListNote("SELECT * FROM " + DatabaseHelper.TABLE_NOTE);

        for (int i = ls.size() - 1; i >= 0; i--) {
            listNote.add(ls.get(i));
        }

        adapter.notifyDataSetChanged();
    }

    public static void showNote(Context context, long id) {
        Intent intent = new Intent(context, NoteActivity.class);
        // send id to NoteActivity
        intent.putExtra(NoteActivity.ID, id);

        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                showNote(context, NoteActivity.NEW_NOTE);
                break;
            default:
                break;
        }
    }

}
