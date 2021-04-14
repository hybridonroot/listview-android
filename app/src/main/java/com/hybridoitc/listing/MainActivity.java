package com.hybridoitc.listing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;

import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    ArraySet<File> files_list = null;
    // Array of strings...
    String[] mobileArray = {"Misbah Ur Rehman","Ahmad Tahir","Waseem Ullah","Ikram",
            "Umar"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, mobileArray);

        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
    }

    private List<File> getListFiles(File parentDir) {
        // On first call, parentDir is your sdcard root path
        ArrayList<File> inFiles = new ArrayList<File>(); // initialize an array list to store file names

        File[] files = parentDir.listFiles(); // list all files in this directory

        for (File file : files) {

            if (file.isDirectory()) { // if the file is a directory

                inFiles.addAll(getListFiles(file)); // **CALL THIS RECURSIVELY TO GET ALL LOWER LEVEL FILES**

            }

        }
        return inFiles;
    }
}