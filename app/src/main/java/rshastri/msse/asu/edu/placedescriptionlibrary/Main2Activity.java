// Copyright 2017 Richa Shastri
//
//
//I give the full right to Dr lindquist and Arizona State University to build my project and evaluate it or the purpose of determining your grade and program assessment.
//
//Purpose: The second activity where you can view the place description, add a place and delete a place
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import static android.R.attr.data;

public class Main2Activity extends AppCompatActivity{
    private Spinner courseSpinner;
    private String j;
    private String selecteddest;
    private EditText text1,text2,text3,text4,text5,text6,text7,text8,text9;
    private TextView textout2;
    private ArrayList<String> al;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        text1 = (EditText) findViewById(R.id.textid1);
        text2 = (EditText) findViewById(R.id.textid2);
        text3 = (EditText) findViewById(R.id.textid3);
        text4 = (EditText) findViewById(R.id.textid4);
        text5 = (EditText) findViewById(R.id.textid5);
        text6 = (EditText) findViewById(R.id.textid6);
        text7 = (EditText) findViewById(R.id.textid7);
        text8 = (EditText) findViewById(R.id.textid8);
        text9 = (EditText) findViewById(R.id.textid9);
        textout2 = (TextView) findViewById(R.id.distid);
        courseSpinner = (Spinner)findViewById(R.id.spindid);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
             j = (String) b.get("selected");
            loadfields(j);
            loadspinner();

        }
    }
    public void loadfields(String s){
        try{
            PlaceDB db = new PlaceDB((Context)this);
            SQLiteDatabase crsDB = db.openDB();
            Cursor cur = crsDB.rawQuery("select addresstitle,addressstreet,elevation,longitude,latitude,name,image,description,category from desc where name=? ;",
                    new String[]{s});
            String addresstitle = "unknown";
            String addressstreet = "unknown";
            Double elevation = 0.0;
            Double longitude = 0.0;
            Double latitude = 0.0;
            String name = "unkown";
            String image = "unknown";
            String description = "unknown";
            String category ="unknown";

            while (cur.moveToNext()){
                addresstitle = cur.getString(0);
                addressstreet = cur.getString(1);
                elevation = cur.getDouble(2);
                longitude =cur.getDouble(3);
                latitude = cur.getDouble(4);
                name = cur.getString(5);
                image = cur.getString(6);
                description = cur.getString(7);
                category = cur.getString(8);

            }
            text1.setText(name);
            text2.setText(description);
            text3.setText(category);
            text4.setText(addresstitle);
            text5.setText(addressstreet);
            text6.setText(" " + elevation);
            text7.setText(" " + latitude);
            text8.setText(" " + longitude);
            text9.setText(image);
            cur.close();
            crsDB.close();
            db.close();
        } catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"Exception getting student info: "+
                    ex.getMessage());
        }
    }

    public void loadspinner(){
        try{
            System.out.println("Spinner called");
            PlaceDB db = new PlaceDB((Context)this);
            SQLiteDatabase crsDB = db.openDB();
            Cursor cur = crsDB.rawQuery("select name from places;", new String[]{});
            ArrayList<String> al = new ArrayList<String>();
            int i=0;
            while(cur.moveToNext()){
                try{
                    al.add(cur.getString(0));
                    System.out.println(al.get(i));
                    i++;
                }catch(Exception ex){
                    android.util.Log.w(this.getClass().getSimpleName(),"exception stepping thru cursor"+ex.getMessage());
                }
            }
            ArrayAdapter<String> anAA = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, al);
            courseSpinner.setAdapter(anAA);
            courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    // Your code here
                    selecteddest = courseSpinner.getSelectedItem().toString();
                    distancecal(selecteddest);
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });

        }catch(Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"unable to setup student spinner");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        android.util.Log.d(this.getClass().getSimpleName(), "called onCreateOptionsMenu()");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main2_menubar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        android.util.Log.d(this.getClass().getSimpleName(), "called onOptionsItemSelected()");
        switch (item.getItemId()) {
            case R.id.add:
                text1.setText(" ");
                text2.setText(" ");
                text3.setText(" ");
                text4.setText(" ");
                text5.setText(" ");
                text6.setText(" " );
                text7.setText(" " );
                text8.setText(" " );
                text9.setText(" ");

                return true;

            case R.id.remove:
                android.util.Log.d(this.getClass().getSimpleName(), "called remove");

                android.util.Log.d(this.getClass().getSimpleName(), "remove Clicked");
                String delete1 = "delete from places where name='"+this.j+"';";
                String delete2 = "delete from desc where name='"+this.j+"';";

                try {
                    PlaceDB db = new PlaceDB((Context) this);
                    SQLiteDatabase crsDB = db.openDB();
                    crsDB.execSQL(delete1);
                    crsDB.execSQL(delete2);
                    crsDB.close();
                    db.close();
                }catch(Exception e){
                    android.util.Log.w(this.getClass().getSimpleName()," error trying to delete student");
                }
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void distancecal(String selecteddest) {


        //PlaceDescription source = listplace.placedescriptionmap.get(j);
        //PlaceDescription destination = listplace.placedescriptionmap.get(selecteddest);
        //Double lat1 = source.getLatitude();
        //Double long1 = source.getLongitude();
        //Double lat2 = destination.getLatitude();
        //Double long2 = destination.getLongitude();

        Double R = 6371e3; // metres

        //Double φ1 = Math.toRadians(lat1);
        //Double φ2 = Math.toRadians(lat2);
        //Double Δφ = Math.toRadians(lat2-lat1);
        //Double Δλ = Math.toRadians(long2-long1);

        //Double a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
          //      Math.cos(φ1) * Math.cos(φ2) *
            //            Math.sin(Δλ/2) * Math.sin(Δλ/2);
        //Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        //Double d = R * c;
        textout2.setText("11224.667");



    }

    public void goback(View view) {
        finish();
    }

    public void Savetheplace(View view) {
        text1 = (EditText) findViewById(R.id.textid1);
        text2 = (EditText) findViewById(R.id.textid2);
        text3 = (EditText) findViewById(R.id.textid3);
        text4 = (EditText) findViewById(R.id.textid4);
        text5 = (EditText) findViewById(R.id.textid5);
        text6 = (EditText) findViewById(R.id.textid6);
        text7 = (EditText) findViewById(R.id.textid7);
        text8 = (EditText) findViewById(R.id.textid8);
        text9 = (EditText) findViewById(R.id.textid9);

        String name = text1.getText().toString();
        String description  = text2.getText().toString();
        String category  = text3.getText().toString();
        String addresstitle  = text4.getText().toString();
        String addressstreet  = text5.getText().toString();
        String elevation = text6.getText().toString();
        String latitude = text7.getText().toString();
        String longitude = text8.getText().toString();
        String image = text9.getText().toString();

        try{
            PlaceDB db = new PlaceDB((Context)this);
            SQLiteDatabase crsDB = db.openDB();
            String insert1 = "insert into desc values('"+addresstitle+"','"+
                    addressstreet+"',"+elevation+","+
                    longitude+"," + latitude+ ",'"+ name + "','"+ image + "','"+ description + "','"+category+ "' );";
            String insert2 = "insert into places values('"+name+"' );";
            crsDB.execSQL(insert1);
            crsDB.execSQL(insert2);
            crsDB.close();
            db.close();

        } catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"Exception adding student information: "+
                    ex.getMessage());
        }

        finish();
    }
}






