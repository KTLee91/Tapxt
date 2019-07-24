package com.springsthursday.tapxt.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseManager {
    static final String DB_USER = "User.db";   //DB이름
    static final String TABLE_USER = "User"; //Table 이름

    Context myContext = null;

    private static DatabaseManager myDBManager = null;
    private SQLiteDatabase database = null;
    private String[] coluimns =  new String[] {"_id","phoneNumber", "userToken"};

    //MovieDatabaseManager 싱글톤 패턴으로 구현
    public static DatabaseManager getInstance(Context context)
    {
        if(myDBManager == null)
        {
            myDBManager = new DatabaseManager(context);
        }

        return myDBManager;
    }

    private DatabaseManager(Context context)
    {
        myContext = context;

        //DB Open
        database = context.openOrCreateDatabase(DB_USER, context.MODE_PRIVATE,null);

        //Table 생성
        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USER +
            "(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "phoneNumber TEXT UNIQUE, " +
            "userToken TEXT);");
    }

    public Boolean isExistPhoneNumber(String selection)
    {
        return database.query(TABLE_USER,
            new String[] {"phoneNumber"},
                "phoneNumber =" + "'" +selection + "'",
                null,
                null,
                null,
                null,
                null).getCount() >0;
    }

    public void updateToken(String phoneNumber, String token)
    {
        ContentValues updateRowValue = new ContentValues();
        updateRowValue.put("userToken", token);

        database.update(TABLE_USER, updateRowValue,"phoneNumber = "+ "'" + phoneNumber + "'", null);
    }

    public void insertUserRecord(String phoneNumber, String token)
    {
        ContentValues contentRowValues = new ContentValues();
        contentRowValues.put("phoneNumber", phoneNumber);
        contentRowValues.put("userToken", token);

        database.insert(TABLE_USER, null, contentRowValues);
    }

    public void  updateNullToken(String phoneNumber)
    {
        ContentValues updateRowValue  = new ContentValues();
        updateRowValue.putNull("userToken");

        database.update(TABLE_USER, updateRowValue, "phoneNumber = " + "'" + phoneNumber + "'", null);

    }

    public Cursor getVerificationUser()
    {
        return database.query(TABLE_USER,
                new String[] {"_id","phoneNumber", "userToken"},
                "userToken Is Not NULL", null, null, null, null, null);
    }

    public void displayRecord()
    {
        Cursor cursor = database.query(TABLE_USER, coluimns, null, null, null, null, null);

        while(cursor.moveToNext()) {
            Log.d("id : ", Integer.toString(cursor.getInt(0)));
            Log.d("phoneNumber :",  cursor.getString(1));

            String token = cursor.getString(2);

            if(token == null)
                Log.d("Token : ","toke is Null!!");
            else
                Log.d("Token : " , token);
        }
    }

    public void delete()
    {
        database.delete(TABLE_USER, null, null);
    }
}
