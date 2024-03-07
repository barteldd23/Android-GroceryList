package edu.ddb.grocerylist;

public class DisplayItem
{
    public DisplayItem(String description, int checked, int masterIndex)
    {
        Description = description;
        Checked = checked;
        MasterIndex = masterIndex;


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

    public int getMasterIndex() {
        return MasterIndex;
    }

    public void setMasterIndex(int masterIndex) {
        this.MasterIndex = masterIndex;
    }

    private int MasterIndex;

}
