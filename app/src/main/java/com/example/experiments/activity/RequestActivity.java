package com.example.experiments.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.experiments.R;
import com.example.experiments.provider.ExperimentProvider;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class RequestActivity extends AppCompatActivity implements View.OnClickListener {

    final String LOG_TAG = "myLogs";
    PieChart pieChart;

    Button btnAll, btnFunc, btnExperiment, btnSort, btnGroup, btnHaving;
    EditText etFunc, epExperiment, etExpName;
    RadioGroup rgSort;

    ExperimentProvider.DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        pieChart = (PieChart) findViewById(R.id.piechart);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.99f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(60f);

        ArrayList<PieEntry> yValues = new ArrayList<>();
        yValues.add(new PieEntry(27f, "Наименование"));
        yValues.add(new PieEntry(14f, "Оборудование"));
        yValues.add(new PieEntry(36f, "Масса смазки"));
        yValues.add(new PieEntry(23f, "Выход смазки"));

        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

        PieDataSet dataSet = new PieDataSet(yValues, "Эксперимент");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);

        btnAll = (Button) findViewById(R.id.btnAll);
        btnAll.setOnClickListener(this);

        btnFunc = (Button) findViewById(R.id.btnFunc);
        btnFunc.setOnClickListener(this);

        btnExperiment = (Button) findViewById(R.id.btnPeople);
        btnExperiment.setOnClickListener(this);

        btnSort = (Button) findViewById(R.id.btnSort);
        btnSort.setOnClickListener(this);

        btnGroup = (Button) findViewById(R.id.btnGroup);
        btnGroup.setOnClickListener(this);

        btnHaving = (Button) findViewById(R.id.btnHaving);
        btnHaving.setOnClickListener(this);

        etFunc = (EditText) findViewById(R.id.etFunc);
        epExperiment = (EditText) findViewById(R.id.etPeople);
        etExpName = (EditText) findViewById(R.id.etRegionPeople);

        rgSort = (RadioGroup) findViewById(R.id.rgSort);

        dbHelper = new ExperimentProvider.DBHelper(this);
        db = dbHelper.getWritableDatabase();
        Cursor c = db.query("obtainingSamples", null, null, null, null, null, null);
        if(c.getCount() == 0) {
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
                    findViewById(R.id.grease_output)).getText().toString());
            Log.d(LOG_TAG, "id = " + db.insert("obtainingSamples", null, values));
        }
        c.close();
        dbHelper.close();
        onClick(btnAll);
    }

    public void onClick(View v) {

        db = dbHelper.getWritableDatabase();

        // данные с экрана
        String sFunc = etFunc.getText().toString();
        String sPeople = epExperiment.getText().toString();
        String sRegionPeople = etExpName.getText().toString();

        String[] columns = null;
        String selection = null;
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;

        Cursor c = null;

        switch (v.getId()) {

            case R.id.btnAll:
                Log.d(LOG_TAG, "--- Все записи ---");
                c = db.query("obtainingSamples", null, null, null, null, null, null);
                break;
            case R.id.btnFunc:
                Log.d(LOG_TAG, "--- Функция " + sFunc + " ---");
                columns = new String[] { sFunc };
                c = db.query("obtainingSamples", columns, null, null, null, null, null);
                break;
            case R.id.btnPeople:
                Log.d(LOG_TAG, "--- Дата измерения больше " + sPeople + " ---");
                selection = "date_measurement > ?";
                selectionArgs = new String[] { sPeople };
                c = db.query("obtainingSamples", null, selection, selectionArgs, null, null,
                        null);
                break;
            // Население по региону
            case R.id.btnGroup:
                Log.d(LOG_TAG, "--- Эксперименты по наименованию ---");
                columns = new String[] { "name" };
                groupBy = "name";
                c = db.query("obtainingSamples", columns, null, null, groupBy, null, null);
                break;
            case R.id.btnHaving:
                Log.d(LOG_TAG, "--- Масса смазки больше " + sRegionPeople
                        + " ---");
                columns = new String[] { "grease_mass", "sum(grease_mass) as grease_mass" };
                groupBy = "grease_mass";
                having = "sum(grease_mass) > " + sRegionPeople;
                c = db.query("obtainingSamples", columns, null, null, groupBy, having, null);
                break;
            case R.id.btnSort:
                // сортировка по
                switch (rgSort.getCheckedRadioButtonId()) {
                    // наименование
                    case R.id.rName:
                        Log.d(LOG_TAG, "--- Сортировка по наименованию ---");
                        orderBy = "name";
                        break;
                    // население
                    case R.id.rPeople:
                        Log.d(LOG_TAG, "--- Сортировка по населению ---");
                        orderBy = "equipment";
                        break;
                    // регион
                    case R.id.rRegion:
                        Log.d(LOG_TAG, "--- Сортировка по региону ---");
                        orderBy = "location";
                        break;
                }
                c = db.query("obtainingSamples", null, null, null, null, null, orderBy);
                break;
        }

        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : c.getColumnNames()) {
                        str = str.concat(cn + " = "
                                + c.getString(c.getColumnIndex(cn)) + "; ");
                    }
                    Log.d(LOG_TAG, str);

                } while (c.moveToNext());
            }
            c.close();
        } else
            Log.d(LOG_TAG, "Cursor is null");

        dbHelper.close();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
