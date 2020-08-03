package com.azhariharisalhamdi.consumerapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

import static android.provider.MediaStore.Audio.Playlists.Members._ID;
import static com.azhariharisalhamdi.consumerapp.database.DatabaseContract.TABLE_NAME;
import static com.azhariharisalhamdi.consumerapp.database.DatabaseContract.UserColumns.USERNAME;

public class UserHelper {
    private static final String DATABASE_TABLE = TABLE_NAME;
    private static DatabaseHelper dataBaseHelper;
    private static UserHelper INSTANCE;

    private static SQLiteDatabase database;

    private UserHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static UserHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();

        if (database.isOpen())
            database.close();
    }

    public Cursor queryAll() {
        return database.query(DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " DESC");
    }

    public Cursor queryById(String id) {
        return database.query(DATABASE_TABLE
                ,null
                , _ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public long insert(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int update(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, _ID + " = ?", new String[]{id});
    }

    public int deleteById(String id) {
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }

    public int deleteByUsername(String username) {
        return database.delete(DATABASE_TABLE, USERNAME + " = ?", new String[]{username});
    }

    public boolean isUserFavorited(String value) {
//        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + DATABASE_TABLE + " WHERE " + USERNAME + " =?";

        // Add the String you are searching by here.
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = database.rawQuery(selectString, new String[] {value});

        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;

            //region if you had multiple records to check for, use this region.

            int count = 0;
            while(cursor.moveToNext()){
                count++;
            }
            //here, count is records found
//            Log.d(TAG, String.format("%d records found", count));

            //endregion

        }

        cursor.close();          // Dont forget to close your cursor
//        database.close();              //AND your Database!
        return hasObject;
    }

    public static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
}
