package com.example.pokemongo_od;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Pokedex {

    private static Pokedex mInstance = null;
    private DBHelper dbHelper;

    protected Pokedex(Context context) {
        dbHelper = new DBHelper(context);
        if (!databaseExists(context, DBHelper.DATABASE_NAME)) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                InputStream dbAssets = context.getAssets().open("Pokedex.db");
                String dbPathName = context.getApplicationInfo().dataDir + "/databases/" + DBHelper.DATABASE_NAME;
                copyFile(context, dbAssets, dbPathName);
            } catch (IOException e) {
                Log.e("Exception: %s", e.getMessage());
            }
        }
    }

    // Checks if database exists
    private boolean databaseExists(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    // Used to copy database from assets to internal storage
    private void copyFile(Context context, InputStream toCopy, String destPath) {
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(destPath);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = toCopy.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            toCopy.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Pokedex getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Pokedex(context);
        }
        return mInstance;
    }

    public String getPokemonInfo(Integer number, String field) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                field,
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = DBContract.PokedexDB._ID + " = ?";
        String[] selectionArgs = { number.toString() };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = field + " DESC";

        Cursor cursor = db.query(
                DBContract.PokedexDB.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
        cursor.moveToFirst();
        int colIndex = cursor.getColumnIndex(field);
        String result = cursor.getString(colIndex);
        cursor.close();
        return result;
    }

    public int setPokemonInfo(Integer number, String field, String value) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(field, value);

        // Which row to update, based on the title
        String selection = DBContract.PokedexDB._ID + " = ?";
        String[] selectionArgs = { number.toString() };

        int count = db.update(
                DBContract.PokedexDB.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        return count;
    }

    public boolean wasSeen(Integer number) {
        String catchState = getPokemonInfo(number, DBContract.PokedexDB.CATCH_STATE);
        return !catchState.equals("UNSEEN");
    }

}
