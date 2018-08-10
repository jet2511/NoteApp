package com.example.haletuyen.noteapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.haletuyen.noteapp.DatabaseHelper;
import com.example.haletuyen.noteapp.model.Note;
import com.example.haletuyen.noteapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NoteActivity extends AppCompatActivity {

    public static final long NEW_NOTE = -1;
    public static final String ID = "ID";

    private DatabaseHelper db;
    private Note note;
    private EditText editTitle, editContent;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        context = this;
        db = new DatabaseHelper(context);
        connectView();
        getInfo();
    }


    private void connectView() {
        editTitle = (EditText) findViewById(R.id.edit_title);
        editContent = (EditText) findViewById(R.id.edit_content);
    }

    // Tra ve du lieu 1 note
    private void getInfo() {
        long id = getIntent().getLongExtra(ID, NEW_NOTE);
        if (id != NEW_NOTE) {
            String sql = "SELECT * FROM " +
                    DatabaseHelper.TABLE_NOTE +
                    " WHERE " +
                    DatabaseHelper.KEY_ID_NOTE + " = " + id;
            note = db.getNote(sql);
        }

        if (note != null) {
            editTitle.setText(note.getTitle());
            editContent.setText(note.getContent());
        } else {
            editTitle.setText("");
            editContent.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                save();
                break;
            case R.id.menu_delete:
                delete();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void save() {
        String title = editTitle.getText().toString().trim();
        String content = editContent.getText().toString().trim();

        String notify = null;
        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(content)) {
            notify = getResources().getText(R.string.note_emty).toString();
        } else {
            // new note
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            String cut = sdf.format(calendar.getTime());
            if (note == null) {
                Note note = new Note();
                note.setTitle(title).setContent(content).setLastModified(cut);
                if (db.insertNote(note) > 0) {
                    notify = getResources().getText(R.string.add_successful).toString();
                } else {
                    notify = getResources().getText(R.string.add_failed).toString();

                }
            } else { // update note
                note.setTitle(title).setContent(content);
                if (db.updateNote(note)) {
                    notify = getResources().getText(R.string.update_successful).toString();
                } else {
                    notify = getResources().getText(R.string.update_failed).toString();
                }
            }
        }

        Toast.makeText(context, notify, Toast.LENGTH_SHORT).show();
        finish();
    }


    private void delete() {
        String title = editTitle.getText().toString().trim();
        String content = editContent.getText().toString().trim();

        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(content)) {
            finish();
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.delete).setIcon(R.mipmap.ic_launcher)
                    .setMessage(getResources().getText(R.string.messeage_delete).toString());

            builder.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            builder.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteNote();
                }
            });
            builder.show();
        }
    }


    private void deleteNote() {
        if (note != null) {
            String where = DatabaseHelper.KEY_ID_NOTE + " = " + note.getId();
            String notify = getResources().getText(R.string.delete_successful).toString();


            if (!db.deleteNote(where)) {
                notify = getResources().getText(R.string.delete_failed).toString();

            }
            Toast.makeText(context, notify, Toast.LENGTH_SHORT).show();
        }
        finish();
    }


    @Override
    public void onBackPressed() {
        save();
    }

}
