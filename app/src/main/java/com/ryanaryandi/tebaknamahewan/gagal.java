package com.ryanaryandi.tebaknamahewan;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class gagal extends AppCompatActivity {
Button imah;
TextView scoretampil,jawabb,jawabs,jawabw;

    int point = 0, benar = 0, salah = 0, waktuhabis = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_gagal);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        scoretampil = (TextView)findViewById(R.id.gagal);
        jawabb = (TextView)findViewById(R.id.jawabanbenar);
        jawabs = (TextView)findViewById(R.id.jawabansalah);
        jawabw = (TextView)findViewById(R.id.jawabanwaktuhabis);

        //mengambil point
        point = getIntent().getIntExtra("PoinT", 0);
        benar = getIntent().getIntExtra("Benar", 0);
        salah = getIntent().getIntExtra("Salah", 0);
        waktuhabis = getIntent().getIntExtra("Waktuhabis", 0);

        String tampil = String.valueOf(point);
        String bb = String.valueOf(benar);
        String ss = String.valueOf(salah);
        String ww = String.valueOf(waktuhabis);
        scoretampil.setText(tampil);
        jawabb.setText("Jawaban benar   : " +bb);
        jawabs.setText("Jawaban salah   : " +ss);
        jawabw.setText("Waktu habis     : " +ww);

//batas

        imah = (Button) findViewById(R.id.home);

        //home
        imah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(gagal.this, depan.class));
                finish();
            }
        });
    }

        //back klik
        public void onBackPressed () {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage("Apakah Anda Yakin Untuk Keluar?");
            builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Exit Aplikasi
                    Intent exit = new Intent(Intent.ACTION_MAIN);
                    exit.addCategory(Intent.CATEGORY_HOME);
                    exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(exit);
                }
            });
            builder.setNegativeButton("Lanjut Main", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(gagal.this, depan.class));
                    finish();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
    }
    }
