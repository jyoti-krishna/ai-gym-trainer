package com.example.fitbudi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class DBhelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private static final String DATABASE_NAME="EXERCISE_DATABASE";
    private static final String TABLE_NAME="EXERCISE_TABLE";
    private static final String COL_1="ID";
    private static final String COL_2="FAT";
//    private static final String COL_2="WT";

    public DBhelper(@Nullable Context context) {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,FAT INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int ii) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    public boolean insertdata(String COL_2){
        db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
//        contentValues.put("WT",COL_2);
        contentValues.put("FAT",COL_2);
        long res=db.insert(TABLE_NAME,null,contentValues);
        if(res==-1){
            return false;
        }
        else return true;
    }


    public Cursor getdata(){
        db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM " +TABLE_NAME,null);
        return cursor;
    }
}
