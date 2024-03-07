package edu.ddb.grocerylist;
import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class  GroceryItemAdapter extends RecyclerView.Adapter {
    private ArrayList<GroceryItem> masterListData;
    private ArrayList<DisplayItem> displayData;
    private View.OnClickListener onItemClickListener;

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

            itemView.setOnClickListener(onItemClickListener);

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

    public GroceryItemAdapter(ArrayList<GroceryItem> data, ArrayList<DisplayItem> displayList, Context context)
    {
        masterListData = data;
        displayData = displayList;
        Log.d(TAG, "GrocryItemAdapter: " + displayData.size());
        parentContext = context;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener)
    {
        Log.d(TAG, "setOnItemClickListener: ");
        onItemClickListener = itemClickListener;
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
        // Maybe two adapters.
        GroceryViewHolder GroceryViewHolder = (GroceryViewHolder) holder;
        GroceryViewHolder.getTvDescription().setText(displayData.get(position).getDescription());

        if(displayData.get(position).getChecked() == 1)
        {
            GroceryViewHolder.getChkOnShoppingList().setChecked(true);
        } else
        {
            GroceryViewHolder.getChkOnShoppingList().setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        return displayData.size();
    }
}
