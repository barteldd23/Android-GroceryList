package edu.ddb.grocerylist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class GroceryListDataSource
{
    SQLiteDatabase database;
    DatabaseHelper dbHelper;
    public static final String TAG = "GroceryListDataSource";

    public GroceryListDataSource(Context context)
    {
        dbHelper = new DatabaseHelper(context, 
                       DatabaseHelper.DATABASE_NAME,
                       null,
                       DatabaseHelper.DATABASE_VERSION);   
    }

    public void open() throws SQLException
    {
        open(false);
    }

    public void open(boolean refresh) throws SQLException
    {
        database = dbHelper.getWritableDatabase();
        Log.d(TAG, "open: " + database.isOpen());
        if(refresh) refreshData();
    }

    public void close()
    {
        dbHelper.close();
    }

    public void refreshData()
    {
        Log.d(TAG, "refreshData: Start");
        ArrayList<GroceryItem> items = new ArrayList<GroceryItem>();

        items.add(new GroceryItem("Pizza"));
        items.add(new GroceryItem("Bread"));
        items.add(new GroceryItem("Cheese", 1, 0));

        // Delete and re-insert all the teams.
        int rowsDeleted = deleteAll();
        int results = 0;
        Log.d(TAG, "refreshData: items count: " + items.size());
        for(GroceryItem item : items)
        {
            results += insert(item);
        }

      Log.d(TAG, "refreshData: End: "+ rowsDeleted + " rows Removed. " + results + " rows Inserted...");
    }

    public GroceryItem get(int id)
    {
        GroceryItem item = null;

        try
        {
            String sql = "select * from tblGroceryitem where Id=" + id;
            Cursor cursor = database.rawQuery(sql,null);

            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                item = new GroceryItem(cursor.getInt(0),
                                       cursor.getString(1),
                                       cursor.getInt(2),
                                       cursor.getInt(3));
                Log.d(TAG, "get: " + item.toString());
                cursor.moveToNext();
            }
        }
        catch(Exception ex)
        {
            Log.d(TAG, "get: Error");
        }

        return item;
    }

    public ArrayList<GroceryItem> get()
    {
        Log.d(TAG, "get: Start");
        ArrayList<GroceryItem> items = new ArrayList<GroceryItem>();
        
        try
        {
            String sql = "Select * from tblGroceryItem";
            Cursor cursor = database.rawQuery(sql, null);
            GroceryItem item;
            Log.d(TAG, "get: " + cursor.getCount());
            while(cursor.moveToNext())
            {
                item = new GroceryItem(cursor.getInt(0),
                                       cursor.getString(1),
                                       cursor.getInt(2),
                                       cursor.getInt(3));


                items.add(item);

                Log.d(TAG, "get: " + item.toString());


            }
        }
        catch(Exception e)
        {
            Log.d(TAG, "get: " + e.getMessage());
            e.printStackTrace();
        }

        Log.d(TAG, "get: End: Items = " + items.size()) ;
        return items;
    }

    public int deleteAll()
    {
        int rows = 0;
        Log.d(TAG, "deleteAll: Start");
        try
        {
            String sql = "select * from tblGroceryItem";
            Cursor cursor = database.rawQuery(sql,null);
            rows = cursor.getCount();

            sql = "delete from tblGroceryItem";
            database.rawQuery(sql,null);
        }
        catch(Exception ex)
        {
            Log.d(TAG, "deleteAll: Error");
        }
        Log.d(TAG, "deleteAll: End");
        return rows;
    }

    public int delete(int id)
    {
        int rows = 0;
        Log.d(TAG, "delete(id): Start id=" + id);
        try
        {
            String where = "id=" + id;
            database.delete("tblGroceryItem",where,null);
            rows = 1;
        }
        catch(Exception ex)
        {
            Log.d(TAG, "delete(id): Error");
        }
        Log.d(TAG, "delete(id): End. rows=" + rows);
        return rows;
    }

    public int insert(GroceryItem item)
    {
        try
        {
            Log.d(TAG, "insert: Start");
            if(database != null)
            {
                ContentValues contentValues = new ContentValues();
                contentValues.put("Item", item.getDescription());
                contentValues.put("IsOnShoppingList", item.getIsOnShoppingList());
                contentValues.put("IsInCart", item.getIsInCart());
                database.insert("tblGroceryItem", null, contentValues);
                Log.d(TAG, "insert: " + item.toString());

                contentValues.clear();
            }

        }
        catch(Exception ex)
        {
            Log.d(TAG, "insert: Error: " + ex.getMessage());
        }
        Log.d(TAG, "insert: End");
        return 1;

    }

    public int update(GroceryItem item)
    {
        Log.d(TAG, "update: Start");
        int rows = 0;
        
        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("Id", item.getId());
            contentValues.put("Item", item.getDescription());
            contentValues.put("IsOnShoppingList", item.getIsOnShoppingList());
            contentValues.put("IsInCart", item.getIsInCart());

            String where = "id=" + item.getId();
            rows = database.update("tblGroceryItem",contentValues,where,null);
            Log.d(TAG, "update: updated " + item.toString());
        }
        catch(Exception ex)
        {
            Log.d(TAG, "update: Error" + ex.getMessage());
        }

        return rows;
    }
}
