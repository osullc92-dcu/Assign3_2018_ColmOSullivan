/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified by Colm O'Sullivan on December 15th 2018
 */
package com.example.android.flavor;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * {@link FlavorActivity} shows a list of Android platform releases.
 * For each release, display the name, version number, and image.
 */
public class FlavorActivity extends AppCompatActivity {


    final ArrayList<AndroidFlavor> androidFlavors = new ArrayList<AndroidFlavor>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flavor);

        // Create an ArrayList of AndroidFlavor objects
        androidFlavors.add(new AndroidFlavor("Exercise Bike", "€100", R.drawable.exercise_bike));
        androidFlavors.add(new AndroidFlavor("Treadmill", "€130", R.drawable.treadmill));
        androidFlavors.add(new AndroidFlavor("Rowing Machine", "€230", R.drawable.rowing_machine));
        androidFlavors.add(new AndroidFlavor("Cross Trainer", "€145", R.drawable.cross_trainer));
        androidFlavors.add(new AndroidFlavor("Steps Machine", "€200", R.drawable.steps_machine));
        androidFlavors.add(new AndroidFlavor("Bench Press Set", "€80", R.drawable.bench_press));
        androidFlavors.add(new AndroidFlavor("Barbell Set", "€65", R.drawable.barbell_set));
        androidFlavors.add(new AndroidFlavor("Dumbbell Set", "€105", R.drawable.dumbbell_set));
        androidFlavors.add(new AndroidFlavor("Top Quality Trainer", "€40 - €210", R.drawable.trainers));
        androidFlavors.add(new AndroidFlavor("Incline Bench", "€45", R.drawable.incline_bench));
        androidFlavors.add(new AndroidFlavor("Leg Press", "€25", R.drawable.leg_press));
        androidFlavors.add(new AndroidFlavor("Decline Bench", "€55", R.drawable.decline_bench));

        // Create an {@link AndroidFlavorAdapter}, whose data source is a list of
        // {@link AndroidFlavor}s. The adapter knows how to create list item views for each item
        // in the list.
        AndroidFlavorAdapter flavorAdapter = new AndroidFlavorAdapter(this, androidFlavors);

        // Get a reference to the ListView, and attach the adapter to the listView.
        ListView listView = (ListView) findViewById(R.id.listview_flavor);
        listView.setAdapter(flavorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //AndroidFlavorAdapter flavorAdapter = new AndroidFlavorAdapter(this, androidFlavors);
                //String selectedValue = androidFlavors.get(position).toString();
                //AndroidFlavor currentAndroidFlavor = getItem(position);
                Toast.makeText(
                        parent.getContext(),
                        "You have selected "
                                + androidFlavors.get(position),
                        Toast.LENGTH_LONG).show();

            }
        });
    }
}

