package edu.ddb.grocerylist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final String FILENAME = "data.txt";
    public static final String XMLFILENAME = "data.xml";

    // item| IsOnSHoppingList | IsInCart
    ArrayList<GroceryItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GroceryItem item = new GroceryItem("Pizza");
        items.add(item);
        item = new GroceryItem("Bread");
        items.add(item);
        item = new GroceryItem("Cheese",1,0);
        items.add(item);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.show_master_list)
        {
            Log.d(TAG, "onOptionsItemSelected: " + item.getTitle());
            //ReadTextFile();
            //Display on screen.

        } else if (id == R.id.show_shopping_list)
        {
            Log.d(TAG, "onOptionsItemSelected: " + item.getTitle());
            // Display items bool onShoppingList

        } else if (id == R.id.add_item)
        {
            Log.d(TAG, "onOptionsItemSelected: " + item.getTitle());
            // If on master - add to list with item|0|0
            // If on shopping - add to list with item|1|0

        } else if (id == R.id.clear_all)
        {
            Log.d(TAG, "onOptionsItemSelected: " + item.getTitle());
            // Remove all from list
            // write nothing to the file.

        } else if (id == R.id.delete_checked)
        {
            // If on master - remove it from the list
            // If on shopping - uncheck it on master item|0|0
            // re-write to file
        }

        return super.onOptionsItemSelected(item);
    }
}