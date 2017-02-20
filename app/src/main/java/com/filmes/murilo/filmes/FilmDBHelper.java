package com.filmes.murilo.filmes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 18/02/17.
 */

public class FilmDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "filmes";
    public static final String TABLE_NAME = "filme";
    public static final String COLUMN_NAME_ID = "ID";
    public static final String COLUMN_NAME_TITLE = "Titulo";
    public static final String COLUMN_NAME_NUMBER = "Numero";
    public static final String COLUMN_NAME_GENRE = "Genero";
    public static final String COLUMN_NAME_NET = "Net";
    public static final String COLUMN_NAME_ATHOME = "EmCasa";
    private HashMap hp;

    public FilmDBHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"(\""+COLUMN_NAME_ID+"\" INTEGER PRIMARY KEY, \""+COLUMN_NAME_TITLE+"\" VARCHAR, \""+COLUMN_NAME_NUMBER+"\" INTEGER, \""+COLUMN_NAME_GENRE+"\" VARCHAR, \""+COLUMN_NAME_NET+"\" BOOLEAN, \""+COLUMN_NAME_ATHOME+"\" BOOLEAN);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME+";");
        onCreate(db);
    }

    public boolean insertFilm(Filme f){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_TITLE,f.getTitle());
        contentValues.put(COLUMN_NAME_NUMBER,f.getNumber());
        contentValues.put(COLUMN_NAME_GENRE,f.getGenre());
        contentValues.put(COLUMN_NAME_NET,f.isNet());
        contentValues.put(COLUMN_NAME_ATHOME,f.isAthome());
        db.insert(TABLE_NAME,null,contentValues);
        db.close();
        return true;
    }

    public boolean restoreBackup(String sqlBackup){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sqlBackup);
        db.close();
        return true;
    }

    public boolean deleteAllFilms(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME+";");
        db.close();
        return true;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE \""+COLUMN_NAME_ID+"\" = "+id+";",null);
        res.close();
        db.close();
        return res;
    }

    public ArrayList<Filme> getData(String query){
        ArrayList<Filme> films = new ArrayList<Filme>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE UPPER(\""+COLUMN_NAME_TITLE+"\") LIKE '%"+query.toUpperCase()+"%';",null);
        res.moveToFirst();
        while(res.isAfterLast()==false){
            films.add(new Filme(
                    res.getString(res.getColumnIndex(COLUMN_NAME_TITLE)),
                    res.getInt(res.getColumnIndex(COLUMN_NAME_NUMBER)),
                    res.getString(res.getColumnIndex(COLUMN_NAME_GENRE)),
                    res.getInt(res.getColumnIndex(COLUMN_NAME_NET))>0,
                    res.getInt(res.getColumnIndex(COLUMN_NAME_ATHOME))>0));
            res.moveToNext();
        }
        int numberquery = 0;
        try{
            numberquery = Integer.parseInt(query);
            res = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE \""+COLUMN_NAME_NUMBER+"\" = "+numberquery+";",null);
            res.moveToFirst();
            Log.d("number_query",res.getString(res.getColumnIndex(COLUMN_NAME_TITLE)));
            while(res.isAfterLast()==false){
                films.add(new Filme(
                        res.getString(res.getColumnIndex(COLUMN_NAME_TITLE)),
                        res.getInt(res.getColumnIndex(COLUMN_NAME_NUMBER)),
                        res.getString(res.getColumnIndex(COLUMN_NAME_GENRE)),
                        res.getInt(res.getColumnIndex(COLUMN_NAME_NET))>0,
                        res.getInt(res.getColumnIndex(COLUMN_NAME_ATHOME))>0));
                res.moveToNext();
            }
        }catch (Exception e){

        }
        res.close();
        db.close();
        return films;
    }

    public int getNumberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db,TABLE_NAME);
        db.close();
        return numRows;
    }

    public ArrayList<Filme> getAllFilms(){
        ArrayList<Filme> films = new ArrayList<Filme>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME+" ORDER BY \""+COLUMN_NAME_TITLE+"\" ASC;",null);
        if(res.getCount()>0) {
            res.moveToFirst();
            while (res.isAfterLast() == false) {
                films.add(new Filme(res.getString(res.getColumnIndex(COLUMN_NAME_TITLE)), res.getInt(res.getColumnIndex(COLUMN_NAME_NUMBER)), res.getString(res.getColumnIndex(COLUMN_NAME_GENRE)), res.getInt(res.getColumnIndex(COLUMN_NAME_NET)) > 0, res.getInt(res.getColumnIndex(COLUMN_NAME_ATHOME)) > 0));
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return films;
    }
}
