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
    private ArrayList<GroceryItem> groceryData;
    private View.OnClickListener onItemClickListener;
    public static final String TAG = "ActorAdapter";

    private Context parentContext;

    public class ActorViewHolder extends RecyclerView.ViewHolder{
        public TextView tvDescription;
        public CheckBox chkOnShoppingList;

        // private View.OnClickListener onClickListener;

        public ActorViewHolder(@NonNull View itemView) {
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

    public GroceryItemAdapter(ArrayList<GroceryItem> data, Context context)
    {
        groceryData = data;
        Log.d(TAG, "GrocryItemAdapter: " + data.size());
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
        return new ActorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        Log.d(TAG, "onBindViewHolder: " + groceryData.get(position));
        ActorViewHolder actorViewHolder = (ActorViewHolder) holder;
        actorViewHolder.getTvDescription().setText(groceryData.get(position).getDescription());
        if(groceryData.get(position).getIsOnShoppingList() == 1)
        {
            actorViewHolder.getChkOnShoppingList().setChecked(true);
        } else
        {
            actorViewHolder.getChkOnShoppingList().setChecked(false);
        }


    }

    @Override
    public int getItemCount() {
        return groceryData.size();
    }
}
