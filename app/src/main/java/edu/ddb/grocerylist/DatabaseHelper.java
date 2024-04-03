package edu.ddb.grocerylist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "DatabaseHelper";
    public static final String DATABASE_NAME = "grocerylist.db";
    public static final int DATABASE_VERSION = 1;

    public static final String CREATE_TEAM_SQL =
            "create table tblGroceryItem(Id integer primary key autoincrement, "
            + "Item text not null, "
            + "IsOnShoppingList bit, "
            + "IsInCart bit)";


    public DatabaseHelper(@Nullable Context context,
                          @Nullable String name,
                          @Nullable SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
        Log.d(TAG, "DatabaseHelper: Constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: " + CREATE_TEAM_SQL);
        // create the table
        db.execSQL(CREATE_TEAM_SQL);
        Log.d(TAG, "onCreate: End");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: ");
        db.execSQL("DROP TABLE IF EXISTS tblTeam");
        onCreate(db);
    }
}
