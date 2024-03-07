package edu.ddb.grocerylist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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

    // item| IsOnSHoppingList | IsInCart
    ArrayList<GroceryItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CreateItems();

        this.setTitle(getString(R.string.master_list));
        RefreshList();

    }

    private View.OnClickListener onClickListener_MasterList = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            CheckBox chkItem = v.findViewById(R.id.chkItem);


//            chkItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    setMasterList(viewHolder, chkItem);
//                }
//            });

            setMasterList(viewHolder, chkItem);

        }

        private void setMasterList(RecyclerView.ViewHolder viewHolder, CheckBox chkItem) {
            // use the index to get an actor
            int position = viewHolder.getAdapterPosition();
            GroceryItem item = items.get(position);
            Log.d(TAG, "onClick: " + item.toString());
            // add code to startActivity of another activity. i.e. Detail screen.

            if(items.get(position).getIsOnShoppingList() == 0)
            {
                items.get(position).setIsOnShoppingList(1);
                chkItem.setChecked(true);

            }
            else
            {
                items.get(position).setIsOnShoppingList(0);
                chkItem.setChecked(false);
            }
        }
    };

    private void RefreshList() {
        // Bind the Recyclerview
        // Make a new list depending on the screen. Contains only description and whether it is checked.
        ArrayList<DisplayItem> displayItems = new ArrayList<DisplayItem>();
        if(this.getTitle() == getString(R.string.master_list))
        {
            for(GroceryItem item : items)
            {
                DisplayItem displayItem = new DisplayItem(item.getDescription(), item.getIsOnShoppingList());
                displayItems.add(displayItem);
            }
        }else
        {
            for(GroceryItem item : items)
            {
                if(item.getIsOnShoppingList() == 1)
                {
                    DisplayItem displayItem = new DisplayItem(item.getDescription(), item.getIsInCart());
                    displayItems.add(displayItem);
                }
            }
        }

        // RecyclerView is a control on the layout
        RecyclerView rvItems = findViewById(R.id.rvItems);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(layoutManager);
        GroceryItemAdapter groceryItemAdapter = new GroceryItemAdapter(items, displayItems, this);

        if(this.getTitle() == getString(R.string.master_list))
        {
            groceryItemAdapter.setOnItemClickListener(onClickListener_MasterList);
        }


        rvItems.setAdapter(groceryItemAdapter);
    }

    private void CreateItems() {
        items = new ArrayList<GroceryItem>();
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
            addItemDialog();
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

    private void addItemDialog()
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
                                String item = etItem.getText().toString();

                                // Need to check if on master list page or not
                                items.add(new GroceryItem(item));
                                //items.add(new GroceryItem(item,1,0));

                                // Need to write to file.

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
}