package com.example.experiments.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.experiments.R;
import com.example.experiments.provider.ExperimentProvider;
import com.google.android.material.navigation.NavigationView;

public class ObtainingSamplesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private EditText date_measurement, start_time, end_time, time_spent, name, equipment, location, grease_mass, grease_output;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obtaining_samples);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        date_measurement = findViewById(R.id.date_measurement);
        start_time = findViewById(R.id.start_time);
        end_time = findViewById(R.id.end_time);
        time_spent = findViewById(R.id.time_spent);
        name = findViewById(R.id.name);
        equipment = findViewById(R.id.equipment);
        location = findViewById(R.id.location);
        grease_mass = findViewById(R.id.grease_mass);
        grease_output = findViewById(R.id.grease_output);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case R.id.send_email:
                    Intent intent1 = new Intent(ObtainingSamplesActivity.this, EmailActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.sample_request:
                    Intent intent2 = new Intent(ObtainingSamplesActivity.this, RequestActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.send_server:
                    Intent intent3 = new Intent(ObtainingSamplesActivity.this, ServerActivity.class);
                    startActivity(intent3);
                    break;
            }

            drawerLayout.closeDrawer(GravityCompat.START);

            return true;
    }

    public void deleteAllExperiments(View view) {
        String URL =
                "content://com.example.contentproviderdemo.Experiment1Prov/experiments1";
        Uri friends = Uri.parse(URL);
        int count = getContentResolver().delete(friends, null, null);
        String countNum = "DeleteAll: " + count + " records are deleted.";
        Toast.makeText(getBaseContext(), countNum, Toast.LENGTH_LONG).show();
    }

    public void addExperiment(View view) {
        ContentValues values = new ContentValues();
        values.put(ExperimentProvider.DATE_MEASUREMENT, ((EditText)
                findViewById(R.id.date_measurement)).getText().toString());
        values.put(ExperimentProvider.START_TIME, ((EditText)
                findViewById(R.id.start_time)).getText().toString());
        values.put(ExperimentProvider.END_TIME, ((EditText)
                findViewById(R.id.end_time)).getText().toString());
        values.put(ExperimentProvider.TIME_SPENT, ((EditText)
                findViewById(R.id.time_spent)).getText().toString());
        values.put(ExperimentProvider.NAME, ((EditText)
                findViewById(R.id.name)).getText().toString());
        values.put(ExperimentProvider.EQUIPMENT, ((EditText)
                findViewById(R.id.equipment)).getText().toString());
        values.put(ExperimentProvider.LOCATION, ((EditText)
                findViewById(R.id.location)).getText().toString());
        values.put(ExperimentProvider.GREASE_MASS, ((EditText)
                findViewById(R.id.grease_mass)).getText().toString());
        values.put(ExperimentProvider.GREASE_OUTPUT, ((EditText)
                findViewById(R.id.grease_mass)).getText().toString());
        Uri uri = getContentResolver().insert(ExperimentProvider.CONTENT_URI,
                values);
        assert uri != null;
        Toast.makeText(getBaseContext(), "AddNew: " + uri.toString()
                + " inserted!", Toast.LENGTH_LONG).show();
    }

    /*public void showAllExperiments(View view) {
        String URL =
                "content://com.example.contentproviderdemo.Experiment1Prov/experiments1";
        Uri friends = Uri.parse(URL);
        Cursor c = getContentResolver().query(friends, null, null, null, "name");
        String result = "ShowAll:";

        if(!c.moveToFirst()) {
            Toast.makeText(this, result + "no content yet!",
                    Toast.LENGTH_LONG).show();
        } else {
            do {
                result = result + "\n" +
                        c.getString(c.getColumnIndex(ExperimentProvider.NAME)) + " с id "
                        + c.getString(c.getColumnIndex(ExperimentProvider.ID))
                        + " имеет дату измерения: ,"
                        + c.getString(c.getColumnIndex(ExperimentProvider.DATE_MEASUREMENT))
                        + " время начала: ,"
                        + c.getString(c.getColumnIndex(ExperimentProvider.START_TIME))
                        + " время окончания: ,"
                        + c.getString(c.getColumnIndex(ExperimentProvider.END_TIME))
                        + " время затрачено: ,"
                        + c.getString(c.getColumnIndex(ExperimentProvider.TIME_SPENT))
                        + " оборудование: ,"
                        + c.getString(c.getColumnIndex(ExperimentProvider.EQUIPMENT))
                        + " расположение: ,"
                        + c.getString(c.getColumnIndex(ExperimentProvider.LOCATION))
                        + " масса смазки: ,"
                        + c.getString(c.getColumnIndex(ExperimentProvider.GREASE_MASS))
                        + " выход смазки: ,"
                        + c.getString(c.getColumnIndex(ExperimentProvider.GREASE_OUTPUT));
            } while (c.moveToNext());
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        }
    }*/
}