package com.projects.ingilizcekelimecalis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class Database extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "kelime_database";

    private static final String TABLE_NAME = "kelime_listesi";
    private static String ID = "id";
    private static String INGILIZCE = "ingilizce";
    private static String TURKCE = "turkce";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "create table " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + INGILIZCE + " TEXT,"
                + TURKCE + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    public void kelimeSil(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void kelimeEkle(String ingilizce, String turkce) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(INGILIZCE, ingilizce);
        values.put(TURKCE, turkce);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public HashMap<String, String> kelimeDetay(int id) {
        HashMap<String, String> kelimem = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE id=" + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            kelimem.put(INGILIZCE, cursor.getString(1));
            kelimem.put(TURKCE, cursor.getString(2));
        }
        cursor.close();
        db.close();
        // return not
        return kelimem;
    }

    public boolean kelimeVarMi(String id) {
        SQLiteDatabase sqldb = this.getReadableDatabase();
        Cursor cursor = sqldb.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE ingilizce='" + id + "'", null);
        if (cursor.moveToFirst()) {
            return true;
        } else {
        }
        cursor.close();
        return false;
    }

    public ArrayList<HashMap<String, String>> kelimeler() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> kelimelist = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                kelimelist.add(map);
            } while (cursor.moveToNext());
        }

        db.close();
        return kelimelist;
    }

    public ArrayList<Kelime> kelimelerList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Kelime> kelimeler = new ArrayList<Kelime>();

        if (cursor.moveToFirst()) {
            do {
                Kelime kelime = new Kelime();
                /*for(int i = 0; i < cursor.getColumnCount(); i++) {
                    kelime.setId(cursor.getColumnName(i));
                    kelime.setEnglish(cursor.getColumnName(i));
                    kelime.setTurkish(cursor.getColumnName(i));
                    //map.put(cursor.getColumnName(i), cursor.getString(i));
                }*/
                kelime.setId(cursor.getString(0));
                kelime.setEnglish(cursor.getString(1));
                kelime.setTurkish(cursor.getString(2));
                kelimeler.add(kelime);
            } while (cursor.moveToNext());
        }

        db.close();
        return kelimeler;
    }
    /*public void notDuzenle(int id, String not_baslik, String not_icerik, String not_tarihi, String not_saat) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Bu methodda ise var olan veriyi güncelliyoruz(update)
        ContentValues values = new ContentValues();
        values.put(NOT_BASLIK, not_baslik);
        values.put(NOT_ICERIK, not_icerik);
        values.put(NOT_TARIHI, not_tarihi);
        values.put(NOT_SAAT, not_saat);

        db.update(TABLE_NAME, values, NOT_ID + " = ?", new String[]{String.valueOf(id)});
    }*/

    public void resetTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }
}
