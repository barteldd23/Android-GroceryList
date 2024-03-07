package edu.ddb.grocerylist;
import android.content.Context;
import android.util.Log;
import android.util.Xml;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class FileIO
{
    public static final String TAG = "FileIOMethods";

    public void writeFile(String fileName,
                          AppCompatActivity activity,
                          String[] items)
    {
        try
        {
            OutputStreamWriter writer = new OutputStreamWriter(activity.openFileOutput(fileName, Context.MODE_PRIVATE));
            String line = "";

            for(int counter = 0; counter < items.length; counter++)
            {
                line = items[counter];
                if (counter < items.length - 1)
                {
                    line += "\r\n";
                }
                writer.write(line);
                Log.d(TAG, "writeFile: " + line);
            }
            writer.close();


        } catch (FileNotFoundException e)
        {
            Log.e(TAG, "writeFile: FileNotFoundException" + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "writeFile: IOException" + e.getMessage());
        } catch(Exception e){
            Log.i(TAG, "writeFile: " + e.getMessage());
        }
    }

    public ArrayList<String> readFile(String fileName,
                                      AppCompatActivity activity)
    {
        ArrayList<String> items = new ArrayList<String>();

        try
        {
            InputStream inputStream = activity.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                items.add(line);
            }
            bufferedReader = null;
            inputStreamReader.close();
            inputStream.close();



        } catch(Exception e){
            Log.d(TAG, "readFile: " + e.getMessage());
        }

        return items;

    }

    public ArrayList<GroceryItem> ReadFromXMLFile(String fileName,
                                            AppCompatActivity activity)
    {
        ArrayList<GroceryItem> groceryItems = new ArrayList<GroceryItem>();
        Log.d(TAG, "ReadFromXMLFile: Start");
        try
        {
            InputStream inputStream = activity.openFileInput(fileName);
            XmlPullParser xmlPullParser = Xml.newPullParser();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            xmlPullParser.setInput(inputStreamReader);

            Log.d(TAG, "ReadFromXMLFile: Pre While");
            while(xmlPullParser.getEventType() != XmlPullParser.END_DOCUMENT)
            {
                Log.d(TAG, "ReadFromXMLFile: In While");
                if(xmlPullParser.getEventType() == XmlPullParser.START_TAG)
                {
                    Log.d(TAG, "ReadFromXMLFile: inEventType == StartTag");
                    // Check if we are at the object we want to read.
                    if(xmlPullParser.getName().equals("groceryitem"))
                    {
                        Log.d(TAG, "ReadFromXMLFile: GetName = groceryitem");
                        // Read its properties.

                        String description = xmlPullParser.getAttributeValue(null,"description");
                        int isOnShoppingList = Integer.parseInt(xmlPullParser.getAttributeValue(null,"isonshoppinglist"));
                        int isInCart = Integer.parseInt(xmlPullParser.getAttributeValue(null,"isincart"));

                        GroceryItem groceryItem = new GroceryItem(description, isOnShoppingList,isInCart);

                        // put it in the thing we are returning.
                        groceryItems.add(groceryItem);

                        Log.d(TAG, "ReadFromXMLFile: " + groceryItem.toString());
                    }
                }
                // Go to next object
                Log.d(TAG, "ReadFromXMLFile: Getting Next");
                xmlPullParser.next();
            }

            // close your stuff
            inputStreamReader.close();
            inputStream.close();
        }
        catch(Exception e)
        {
            Log.d(TAG, "ReadFromXMLFile: " + e.getMessage());
        }

        // Seems to need the return after the catch
        Log.d(TAG, "ReadFromXMLFile: End");
        return groceryItems;
    }

    public void WriteXMLFile(String filename,
                             AppCompatActivity activity,
                             ArrayList<GroceryItem> groceryItems)
    {
        try
        {
            Log.d(TAG, "WriteXMLFile: Start");
            XmlSerializer serializer = Xml.newSerializer();
            File file = new File(filename);
            OutputStreamWriter outputStreamWriter = null;

            outputStreamWriter = new OutputStreamWriter(activity.getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE));
            serializer.setOutput(outputStreamWriter);

            serializer.startDocument("UTF-8", true);
            // XML Tag for our list of items
            serializer.startTag("","groceryitems");
            serializer.attribute("","number", String.valueOf(groceryItems.size()));

            for(GroceryItem groceryItem : groceryItems)
            {
                // XML Tag for a specific item.
                serializer.startTag("","groceryitem");
                serializer.attribute("","description",String.valueOf(groceryItem.getDescription()));
                serializer.attribute("","isonshoppinglist",String.valueOf(groceryItem.getIsOnShoppingList()));
                serializer.attribute("","isincart",String.valueOf(groceryItem.getIsInCart()));
                serializer.endTag("","actor");
                Log.d(TAG, "WriteXMLFile: " + groceryItem.toString());
            }
            serializer.endTag("","groceryitems");
            serializer.endDocument();

            // clear and close
            serializer.flush();
            outputStreamWriter.close();
            Log.d(TAG, "WriteXMLFile: Wrote " + groceryItems.size());
            Log.d(TAG, "WriteXMLFile: End");
        }
        catch(Exception e)
        {
            Log.d(TAG, "WriteXMLFile: " + e.getMessage());
        }
    }
}