package com.example.memorableplaces;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    static ArrayList<String> places = new ArrayList<String>();
    static ArrayList<LatLng> locations = new ArrayList<LatLng>();
    static ArrayAdapter arrayAdapter;
    SharedPreferences sharedPreferences;

    ImageView imageView2;
    ImageView imageView3;

    public void deleteLocation(View view){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setTitle("Do you wish to do that?")
                .setMessage("You will be called a noob from everyone.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setIcon(android.R.drawable.sym_def_app_icon)
                                .setTitle("God gave you another chance")
                                .setMessage("YOU WILL SERIOUSLY BE CALLED A NOOB UNTIL DEATH IF YOU CLICK YES")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        sharedPreferences.edit().remove("places").commit();
                                        sharedPreferences.edit().remove("lats").commit();
                                        sharedPreferences.edit().remove("longs").commit();
                                        places.clear();
                                        arrayAdapter.notifyDataSetChanged();

                                        if(places.size() == 0){
                                            places.add("Add a new place");
                                            arrayAdapter.notifyDataSetChanged();
                                        }
                                        Toast.makeText(MainActivity.this, "YOU ARE A CERTIFIED NOOB. DON'T SHOW YOUR FACE AGAIN", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(MainActivity.this, "YOU ARE NO NOOB. DIE IN PEACE", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "SMART MOVE KID", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
        imageView2.setVisibility(View.VISIBLE);
        imageView3.setVisibility(View.VISIBLE);

        new CountDownTimer(10001, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                Log.i("Millis Left", String.valueOf(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                imageView2.setVisibility(View.INVISIBLE);
                imageView3.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView2 =  findViewById(R.id.imageView2);
        imageView3 =  findViewById(R.id.imageView3);

        imageView2.setVisibility(View.INVISIBLE);
        imageView3.setVisibility(View.INVISIBLE);

        sharedPreferences = this.getSharedPreferences("com.example.memorableplaces", Context.MODE_PRIVATE);
        ArrayList<String> latitudes = new ArrayList<String>();
        ArrayList<String> longitudes = new ArrayList<String>();

        places.clear();
        latitudes.clear();
        longitudes.clear();
        locations.clear();

        try {
            places = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places", ObjectSerializer.serialize(new ArrayList<String>())));
            latitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lats", ObjectSerializer.serialize(new ArrayList<String>())));
            longitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("longs", ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (places.size() > 0 && latitudes.size() > 0 && longitudes.size() > 0) {
            if(places.size() == latitudes.size() && latitudes.size() == longitudes.size()){
                for(int i =0;i<latitudes.size();i++){
                    locations.add(new LatLng(Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i))));
                }
            }
        } else{
            places.add("Add a new place");
            locations.add(new LatLng(0, 0));
        }
        ListView listView = findViewById(R.id.listView);



        arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, places);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("placeNumber", position);
                startActivity(intent);
            }
        });
    }
}