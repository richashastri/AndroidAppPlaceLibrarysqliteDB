// Copyright 2017 Richa Shastri
//
//
//I give the full right to Dr lindquist and Arizona State University to build my project and evaluate it or the purpose of determining your grade and program assessment.
//
//Purpose: Main activity to view the place description
//
// Ser423 Mobile Applications
//see http://pooh.poly.asu.edu/Mobile
//@author Richa Shastri Richa.Shastri@asu.edu
//        Software Engineering, CIDSE, ASU Poly
//@version April 10, 2017


package rshastri.msse.asu.edu.placedescriptionlibrary;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import static android.R.attr.onClick;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayAdapter<String> adapter;
    private ArrayList<String> placeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.util.Log.d(this.getClass().getSimpleName(), "Oncreate is called");
        loadplaces();
    }

    public void loadplaces(){
        try{
            PlaceDB db = new PlaceDB((Context)this);
            SQLiteDatabase crsDB = db.openDB();
            Cursor cur = crsDB.rawQuery("select name from places;", new String[]{});
            placeList = new ArrayList<>();
            while(cur.moveToNext()){
                try{
                    placeList.add(cur.getString(0));
                }catch(Exception ex){
                    android.util.Log.w(this.getClass().getSimpleName(),"exception stepping thru cursor"+ex.getMessage());
                }
            }
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, placeList);
            ListView listView = (ListView) findViewById(R.id.placelist);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(this);

        }catch(Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"unable to setup student spinner");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (position >= 0 && position <= placeList.size()) {
            System.out.println(position);
            Intent displayStud = new Intent(this, Main2Activity.class);
            displayStud.putExtra("selected", placeList.get(position));
            android.util.Log.d(this.getClass().getSimpleName(), "selection made:: ");
            android.util.Log.d(this.getClass().getSimpleName(), String.valueOf(placeList.get(position)));
            startActivityForResult(displayStud,1);
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        android.util.Log.d(this.getClass().getSimpleName(), "calling on activity result");
        loadplaces();
    }
}

