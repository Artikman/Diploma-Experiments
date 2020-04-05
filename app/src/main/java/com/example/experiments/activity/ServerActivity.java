package com.example.experiments.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.experiments.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerActivity extends AppCompatActivity {

    EditText txtIP, txtPort, date_measurement, start_time, end_time, time_spent, name, equipment, location, grease_mass, grease_output;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        txtIP = findViewById(R.id.et_ip);
        txtPort = findViewById(R.id.et_port);
        date_measurement = findViewById(R.id.date_measurement);
        start_time = findViewById(R.id.start_time);
        end_time = findViewById(R.id.end_time);
        time_spent = findViewById(R.id.time_spent);
        name = findViewById(R.id.name);
        equipment = findViewById(R.id.equipment);
        location = findViewById(R.id.location);
        grease_mass = findViewById(R.id.grease_mass);
        grease_output = findViewById(R.id.grease_output);
        btn = findViewById(R.id.bt_send);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Socket s = new Socket(txtIP.getText().toString(), Integer.parseInt(txtPort.getText().toString()));
                            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                            dos.writeUTF(date_measurement.getText().toString());
                            dos.writeUTF(start_time.getText().toString());
                            dos.writeUTF(end_time.getText().toString());
                            dos.writeUTF(time_spent.getText().toString());
                            dos.writeUTF(name.getText().toString());
                            dos.writeUTF(equipment.getText().toString());
                            dos.writeUTF(location.getText().toString());
                            dos.writeUTF(grease_mass.getText().toString());
                            dos.writeUTF(grease_output.getText().toString());
                            dos.flush();
                            dos.close();
                            s.close();
                        } catch (NumberFormatException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                t.start();
            }
        });

        Thread thread = new Thread(new MyServerThread());
        thread.start();

    }

    class MyServerThread implements Runnable {

        Socket s;
        ServerSocket ss;
        InputStreamReader isr;
        BufferedReader br;
        String message;
        Handler h = new Handler();

        @Override
        public void run() {
            try {
                ss = new ServerSocket(400);
                while(true) {
                    s = ss.accept();
                    isr = new InputStreamReader(s.getInputStream());
                    br = new BufferedReader(isr);
                    message = br.readLine();

                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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