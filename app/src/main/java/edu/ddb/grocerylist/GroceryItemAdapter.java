package edu.ddb.grocerylist;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class  GroceryItemAdapter extends RecyclerView.Adapter {
    private ArrayList<GroceryItem> masterListData;
    private ArrayList<DisplayItem> displayData;
    public String screen;
    private View.OnClickListener onItemChkBoxClickListener;

    public static final String TAG = "GroceryItemAdapter";

    private Context parentContext;

    public class GroceryViewHolder extends RecyclerView.ViewHolder{
        public TextView tvDescription;
        public CheckBox chkOnShoppingList;


        private View.OnClickListener onClickListener;

        public GroceryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvItem);
            chkOnShoppingList = itemView.findViewById(R.id.chkItem);

            // code involving with clicking an item in the list.
            itemView.setTag(this);


            chkOnShoppingList.setOnClickListener(onItemChkBoxClickListener);


        }

        public TextView getTvDescription()
        {
            return tvDescription;
        }
        public CheckBox getChkOnShoppingList()
        {
            return chkOnShoppingList;
        }

    }

    public GroceryItemAdapter(ArrayList<GroceryItem> data, ArrayList<DisplayItem> displayList, Context context, String currentScreen)
    {
        masterListData = data;
        displayData = displayList;
        screen = currentScreen;
        Log.d(TAG, "GrocryItemAdapter: " + displayData.size());
        parentContext = context;
    }

    public void setOnItemChkBoxClickListener(View.OnClickListener itemClickListener)
    {
        Log.d(TAG, "setOnItemClickListener: ");
        onItemChkBoxClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.complex_item_view, parent, false);
        return new GroceryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        Log.d(TAG, "onBindViewHolder: " + displayData.get(position));


        // Maybe Check for title to do logic for Master or Shopping list.
        GroceryViewHolder GroceryViewHolder = (GroceryViewHolder) holder;
        GroceryViewHolder.getTvDescription().setText(displayData.get(position).getDescription());


        if(displayData.get(position).getChecked() == 1)
        {
            GroceryViewHolder.getChkOnShoppingList().setChecked(true);
        } else
        {
            GroceryViewHolder.getChkOnShoppingList().setChecked(false);
        }

        //Make event listener for when on Master List
        if(Objects.equals(screen, parentContext.getString(R.string.master_list)))
        {
            GroceryViewHolder.getChkOnShoppingList().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {
                        displayData.get(position).setChecked(1);
                        masterListData.get(displayData.get(position).getMasterIndex()).setIsOnShoppingList(1);
                    }else
                    {
                        displayData.get(position).setChecked(0);
                        masterListData.get(displayData.get(position).getMasterIndex()).setIsOnShoppingList(0);

                    }
                    GroceryListDataSource dataSource = new GroceryListDataSource(parentContext);
                    dataSource.open();
                    dataSource.update(masterListData.get(displayData.get(position).getMasterIndex()));
                }
            });
        }
        // Make event listener for when on Shopping List
        else
        {
            GroceryViewHolder.getChkOnShoppingList().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {
                        displayData.get(position).setChecked(1);
                        masterListData.get(displayData.get(position).getMasterIndex()).setIsInCart(1);
                    }else
                    {
                        displayData.get(position).setChecked(0);
                        masterListData.get(displayData.get(position).getMasterIndex()).setIsInCart(0);
                    }
                    GroceryListDataSource dataSource = new GroceryListDataSource(parentContext);
                    dataSource.open();
                    dataSource.update(masterListData.get(displayData.get(position).getMasterIndex()));
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return displayData.size();
    }
}
