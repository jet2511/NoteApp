package com.example.haletuyen.noteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.haletuyen.noteapp.model.Note;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "note.db";
    public static final String TABLE_NOTE = "tb_note";
    public static final String KEY_ID_NOTE = "id";
    public static final String KEY_TITLE_NOTE = "title";
    public static final String KEY_CONTENT_NOTE = "content";
    public static final String KEY_LAST_MODIFIED_NOTE = "last_modified";

    public static final String CREATE_TABLE_NOTE =
            "CREATE TABLE " + TABLE_NOTE + "(" +
                    KEY_ID_NOTE + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL" +
                    ", " + KEY_TITLE_NOTE + " TEXT NOT NULL" +
                    "," + KEY_CONTENT_NOTE + " TEXT NOT NULL" +
                    "," + KEY_LAST_MODIFIED_NOTE + " TEXT DEFAULT \'\'" + ")";


    public static final int DATA_VERSION = 2;

    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATA_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_NOTE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Cap nhat db khi db_version <2
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_NOTE + " ADD COLUMN " + KEY_LAST_MODIFIED_NOTE + " TEXT DEFAULT \'\'");
        }
    }


    public void open() {
        try {
            db = getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void close() {
        if (db != null && db.isOpen()) {
            try {
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // lay tat ca note, tra ve cursor
    public Cursor getAll(String sql) {
        open();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        close();
        return cursor;
    }


    public long insert(String table, ContentValues values) {
        open();
        long index = db.insert(table, null, values);
        close();
        return index;
    }


    public boolean update(String table, ContentValues values, String where) {
        open();
        long index = db.update(table, values, where, null);
        close();
        return index > 0;
    }


    public boolean delete(String table, String where) {
        open();
        long index = db.delete(table, where, null);
        close();
        return index > 0;
    }


    // tra ve 1 note
    public Note getNote(String sql) {
        Note note = null;
        Cursor cursor = getAll(sql);
        if (cursor != null) {
            note = cursorToNote(cursor);
            cursor.close();
        }
        return note;
    }

    // tra ra danh sanh note
    public ArrayList<Note> getListNote(String sql) {
        ArrayList<Note> list = new ArrayList<>();
        Cursor cursor = getAll(sql);

        while (!cursor.isAfterLast()) {
            list.add(cursorToNote(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public long insertNote(Note note) {
        return insert(TABLE_NOTE, noteToValues(note));
    }


    public boolean updateNote(Note note) {
        return update(TABLE_NOTE, noteToValues(note), KEY_ID_NOTE + " = " + note.getId());
    }

    public boolean deleteNote(String where) {
        return delete(TABLE_NOTE, where);
    }


    // Chuyen du lieu thu note sang value
    private ContentValues noteToValues(Note note) {
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE_NOTE, note.getTitle());
        values.put(KEY_CONTENT_NOTE, note.getContent());
        values.put(KEY_LAST_MODIFIED_NOTE, note.getLastModified());
        return values;
    }

    // Chuyen du lieu tu cursor sang note
    private Note cursorToNote(Cursor cursor) {
        Note note = new Note();
        note.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_NOTE)))
                .setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE_NOTE)))
                .setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT_NOTE)))
                .setLastModified(cursor.getString(cursor.getColumnIndex(KEY_LAST_MODIFIED_NOTE)));
        return note;
    }
}
