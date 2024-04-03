package edu.ddb.grocerylist;

import androidx.annotation.NonNull;

public class GroceryItem
{
    public GroceryItem(String description)
    {
        Id = -1;
        Description = description;
        IsOnShoppingList = 0;
        IsInCart = 0;
    }
    public GroceryItem(String description, int isOnShoppingList, int isInCart)
    {
        Id = -1;
        Description = description;
        IsOnShoppingList = isOnShoppingList;
        IsInCart = isInCart;
    }
    public GroceryItem(int id,String description, int isOnShoppingList, int isInCart)
    {
        Id = id;
        Description = description;
        IsOnShoppingList = isOnShoppingList;
        IsInCart = isInCart;
    }



    private int Id;
    private String Description;
    private int IsOnShoppingList;
    private int IsInCart;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getIsOnShoppingList() {
        return IsOnShoppingList;
    }

    public void setIsOnShoppingList(int isOnShoppingList) {
        IsOnShoppingList = isOnShoppingList;
    }

    public int getIsInCart() {
        return IsInCart;
    }

    public void setIsInCart(int isInCart) {
        IsInCart = isInCart;
    }

    @NonNull
    public String toString()
    {
        return Id + "|" + Description + "|" + IsOnShoppingList + "|" + IsInCart;
    }
}
