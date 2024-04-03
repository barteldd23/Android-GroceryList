package edu.ddb.grocerylist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final String FILENAME = "data.txt";
    public static final String XMLFILENAME = "data.xml";

    GroceryListDataSource dataSource = new GroceryListDataSource(this);



    // item| IsOnSHoppingList | IsInCart
    ArrayList<GroceryItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Start Create");



        // This is to reset the datatable
        //dataSource.open(true);
        dataSource.open();

        //ResetTable();
        //CreateItems();
        //WriteTextFile();

        //GetItems();

        //ReadTextFile();

        this.setTitle(getString(R.string.master_list));

        // dataSource.get() is in refreshList()
        RefreshList();

        Log.d(TAG, "onCreate: End Create");

    }





    private View.OnClickListener onCheckedClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CheckBox chkBox = v.findViewById(R.id.chkItem);
            //boolean isChecked = chkBox.isChecked();
            //chkBox.setChecked(isChecked);
            //WriteTextFile();

            // Update database
        }
    };

    private void RefreshList() {
        // Bind the Recyclerview
        // Make a new list depending on the screen. Contains only description and whether it is checked.
        items = dataSource.get();
        ArrayList<DisplayItem> displayItems = new ArrayList<DisplayItem>();
        if(this.getTitle() == getString(R.string.master_list))
        {
            for(GroceryItem item : items)
            {
                DisplayItem displayItem = new DisplayItem(item.getDescription(), item.getIsOnShoppingList(),items.indexOf(item));
                displayItems.add(displayItem);
            }
        }else
        {
            for(GroceryItem item : items)
            {
                if(item.getIsOnShoppingList() == 1)
                {
                    DisplayItem displayItem = new DisplayItem(item.getDescription(), item.getIsInCart(),items.indexOf(item));
                    displayItems.add(displayItem);
                }
            }
        }

        // RecyclerView is a control on the layout
        String screen = this.getTitle().toString();
        RecyclerView rvItems = findViewById(R.id.rvItems);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(layoutManager);
        GroceryItemAdapter groceryItemAdapter = new GroceryItemAdapter(items, displayItems, this, screen);

        //This listener listens for a tap on the checkbox. then it manually changes the checkbox
        //That triggers the checkchange that is created in the adapter.
        //Then this clickListener writes to the file.
        groceryItemAdapter.setOnItemChkBoxClickListener(onCheckedClickListener);

        rvItems.setAdapter(groceryItemAdapter);
    }

    private void CreateItems() {
        //GroceryItem item = new GroceryItem("Pizza");
        //items.add(item);
        //item = new GroceryItem("Bread");
        //items.add(item);
        //item = new GroceryItem("Cheese",1,0);
        //items.add(item);
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
            this.setTitle(getString(R.string.master_list));
            RefreshList();

        } else if (id == R.id.show_shopping_list)
        {
            Log.d(TAG, "onOptionsItemSelected: " + item.getTitle());
            // Display items bool onShoppingList
            this.setTitle(getString(R.string.shopping_list));
            RefreshList();

        } else if (id == R.id.add_item)
        {
            Log.d(TAG, "onOptionsItemSelected: " + item.getTitle());
            // If on master - add to list with item|0|0
            addItemDialog(this.getTitle().toString());
            // If on shopping - add to list with item|1|0

        } else if (id == R.id.clear_all)
        {
            Log.d(TAG, "onOptionsItemSelected: " + item.getTitle());
            // If on Master make all items : item|0|0
            if(this.getTitle() == getString(R.string.master_list))
            {
                for(GroceryItem groceryItem : items)
                {
                    groceryItem.setIsOnShoppingList(0);
                    groceryItem.setIsInCart(0);
                }
            }
            else // If on Shopping List, change all those items to item|1|0
            {
                for(GroceryItem groceryItem : items)
                {
                    groceryItem.setIsInCart(0);
                }
            }

            // write nothing to the file.
            WriteTextFile();
            RefreshList();

        } else if (id == R.id.delete_checked)
        {
            Log.d(TAG, "onOptionsItemSelected: " + item.getTitle());
            // If on master - remove it from the list
            if(this.getTitle() == getString(R.string.master_list))
            {
                Log.d(TAG, "onOptionsItemSelected: in the if Master");
                for(int i = 0; i < items.size(); i++)
                {
                    GroceryItem groceryItem = items.get(i);
                    if(groceryItem.getIsOnShoppingList() == 1)
                    {

                        items.remove(groceryItem);
                        i--;
                        Log.d(TAG, "onOptionsItemSelected: removed: " + groceryItem.toString());

                    }
                }
            }
            else // If on shopping - uncheck it on master item|0|0
            {
                for(GroceryItem groceryItem : items)
                {
                    if(groceryItem.getIsInCart() == 1)
                    {
                        groceryItem.setIsInCart(0);
                        groceryItem.setIsOnShoppingList(0);
                    }
                }
            }

            // re-write to file
            WriteTextFile();
            RefreshList();
        }

        return super.onOptionsItemSelected(item);
    }

    private void addItemDialog(String screen)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View addItemView = layoutInflater.inflate(R.layout.add_item_view, null);

        // show the dialog to the user modularly.
        new AlertDialog.Builder(this)
                .setTitle(R.string.add_item)
                .setView(addItemView)
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Get
                                Log.d(TAG, "onClick: OK");
                                // need this addItemView before.findView because the view we made has the edit text we want.
                                EditText etItem = addItemView.findViewById(R.id.etAddItem);
                                String itemDescription = etItem.getText().toString();

                                // Need to check if on master list page or not
                                if(screen == getString(R.string.master_list))
                                {
                                    GroceryItem item = new GroceryItem(itemDescription);
                                    dataSource.insert(item);
                                }
                                else
                                {
                                    GroceryItem item = new GroceryItem(itemDescription,1,0);
                                    dataSource.insert(item);
                                }





                                // Need to write to file.
                                // WriteTextFile();
                                // Need to repopulate the screen.

                                RefreshList();

                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "onClick: Cancel");
                            }
                        }).show();
    }

    private void WriteXMLFile()
    {
        try
        {
            Log.d(TAG, "WriteXMLFile: Start");
            FileIO fileIO = new FileIO();
            fileIO.WriteXMLFile(XMLFILENAME, this, items);
            Log.d(TAG, "WriteXMLFile: End");

        }
        catch(Exception e)
        {
            Log.d(TAG, "WriteXMLFile: " + e.getMessage());
        }

    }

    private void ReadXMLFile()
    {
        try
        {
            FileIO fileIO = new FileIO();
            items = fileIO.ReadFromXMLFile(XMLFILENAME, this);
            Log.d(TAG, "ReadXMLFile: GroceryItems " + items.size());
        }
        catch(Exception e)
        {
            Log.d(TAG, "ReadXMLFile: " + e.getMessage());
        }

    }

    private void WriteTextFile()
    {
        try {

            FileIO fileIO = new FileIO();
            int counter = 0;
            String[] data = new String[items.size()];

            for(GroceryItem item : items)
            {
                data[counter++] = item.toString();
            }
            fileIO.writeFile(FILENAME, this, data);
            Log.d(TAG, "WriteTextFile: " + data.length);

        }
        catch(Exception e)
        {
            Log.d(TAG, "WriteTextFile: " + e.getMessage());
        }
    }

    private void ReadTextFile()
    {
        try {
            FileIO fileIO = new FileIO();
            ArrayList<String> strData = fileIO.readFile(FILENAME, this);

            items = new ArrayList<GroceryItem>();



            for(String s : strData)
            {
                String[] data = s.split("\\|");
                items.add(new GroceryItem(
                        data[0],
                        Integer.parseInt(data[1]),
                        Integer.parseInt(data[2])));
                Log.d(TAG, "ReadTextFile: " + items.get(items.size()-1).getDescription());
            }
            Log.d(TAG, "ReadTextFile: " + items.size());

        }
        catch(Exception e){
            Log.d(TAG, "ReadTextFile: " + e.getMessage());
        }
    }
}