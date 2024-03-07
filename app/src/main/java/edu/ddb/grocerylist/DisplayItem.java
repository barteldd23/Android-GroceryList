package edu.ddb.grocerylist;

public class DisplayItem
{
    public DisplayItem(String description, int checked)
    {
        Description = description;
        Checked = checked;
    }
    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getChecked() {
        return Checked;
    }

    public void setChecked(int checked) {
        this.Checked = checked;
    }

    private String Description;
    private int Checked;

}
