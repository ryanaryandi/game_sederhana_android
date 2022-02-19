package com.ryanaryandi.tebaknamahewan;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class satu extends AppCompatActivity {
    ImageView maung;
    Button cek, imah;
    EditText jawab;
    String jawaban;
    TextView skorgame,scoretampil;
    CountDownTimer timer;
    MediaPlayer aujam, aubenar, ausalah, backsound, auberhenti;

    int point = 0, benar = 0, salah = 0, waktuhabis = 0;

    private boolean mIsBound = false;
    private MusicService mServ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_satu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//music baground
        doBindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        startService(music);

//home music
        HomeWatcher mHomeWatcher;

        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                    aujam.pause();
                    timer.cancel();
                }
            }

            @Override
            public void onHomeLongPressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                    aujam.pause();
                    timer.cancel();
                }
            }
        });
        mHomeWatcher.startWatch();


//batas
        maung = (ImageView) findViewById(R.id.gambar1);
        cek = (Button) findViewById(R.id.buttonCek);
        imah = (Button) findViewById(R.id.home);
        jawab = (EditText) findViewById(R.id.jawab);
        skorgame = (TextView) findViewById(R.id.skor);
        scoretampil = (TextView)findViewById(R.id.score);

        //Memanggil file my_sound
        aujam = MediaPlayer.create(getApplicationContext(), R.raw.jam);
        aubenar = MediaPlayer.create(getApplicationContext(), R.raw.benar);
        ausalah = MediaPlayer.create(getApplicationContext(), R.raw.gagal);
        auberhenti = MediaPlayer.create(getApplicationContext(),R.raw.salah);
        backsound = MediaPlayer.create(getApplicationContext(), R.raw.backsound);
        //Set volume audio agar berbunyi
        //backsound.setVolume(100,100);
        aujam.setVolume(100, 100);
        //mengulang audio
        aujam.setLooping(true);
        //backsound.setLooping(true);
        //Memulai audio
        //backsound.start();
        aujam.start();

        //tampil point
        String tampil = String.valueOf(point);
        scoretampil.setText("Score : " + tampil);

//mengambil gambar dari database
        Glide.with(satu.this).load("https://firebasestorage.googleapis.com/v0/b/tebak-nama-hewan-6069a.appspot.com/o/gambar%2Fmaung.jpeg?alt=media&token=93ae9dca-aff2-4256-b27c-bd8ff111b3bf")
                .into(maung);


//membuat aksi pada jawaban
        cek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jawaban = jawab.getText().toString();

                if (jawaban.equals("harimau")) {
                    aubenar.setVolume(10, 10);
                    aubenar.start();

                    point = point + 10;
                    benar = benar + 1;

                    Toast.makeText(getApplicationContext(), "Jawaban benar", Toast.LENGTH_SHORT).show();
                    Intent pindah = new Intent(satu.this, dua.class);
                    pindah.putExtra("PoinT", point);
                    pindah.putExtra("Benar", benar);
                    pindah.putExtra("Salah", salah);
                    pindah.putExtra("Waktuhabis", waktuhabis);
                    startActivity(pindah);
                    timer.cancel();
                    aujam.stop();
                    finish();

                }else if (jawaban.equals("")) {
                    Toast.makeText(getApplicationContext(), "Jawaban tidak boleh kosong", Toast.LENGTH_SHORT).show();

                }else{
                    auberhenti.setVolume(10, 10);
                    auberhenti.start();
                    salah = salah + 1;
                    Toast.makeText(getApplicationContext(), "Jawaban salah", Toast.LENGTH_SHORT).show();
                    Intent pindah = new Intent(satu.this, dua.class);
                    pindah.putExtra("Benar", benar);
                    pindah.putExtra("Salah", salah);
                    pindah.putExtra("Waktuhabis", waktuhabis);
                    startActivity(pindah);
                    timer.cancel();
                    aujam.stop();
                    finish();
                }
            }
        });

        //membuat counterdown
        timer = new CountDownTimer(20000, 1000) {

            @Override
            public void onFinish() {
                TextView teks = (TextView) findViewById(R.id.skor);
                teks.setText("Field!!!");

                waktuhabis = waktuhabis + 1;

                Toast.makeText(getApplicationContext(), "Waktu habis", Toast.LENGTH_SHORT).show();
                Intent pindah = new Intent(satu.this, dua.class);
                pindah.putExtra("PoinT", point);
                pindah.putExtra("Benar", benar);
                pindah.putExtra("Salah", salah);
                pindah.putExtra("Waktuhabis", waktuhabis);
                startActivity(pindah);
                auberhenti.setVolume(10, 10);
                auberhenti.start();
                aujam.stop();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                TextView teks = (TextView) findViewById(R.id.skor);
                teks.setText("Seconds Left: " + millisUntilFinished / 1000);
            }

        }.start();


        //home
        imah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lanjut = new Intent(satu.this, depan.class);
                startActivity(lanjut);
                timer.cancel();
                aujam.stop();
                finish();
            }
        });
    }

    //back klik
    public void onBackPressed() {
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
                timer.cancel();
                aujam.stop();
                finish();
            }
        });
        builder.setNegativeButton("Lanjut Main", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //music baground
    private ServiceConnection Scon = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder) binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService() {
        bindService(new Intent(this, MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mServ != null) {
            mServ.resumeMusic();
            aujam.start();
            timer.start();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        doUnbindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        stopService(music);

    }

    @Override
    protected void onPause() {
        super.onPause();

        PowerManager pm = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (pm != null) {
            isScreenOn = pm.isScreenOn();
        }

        if (!isScreenOn) {
            if (mServ != null) {
                mServ.pauseMusic();
            }
        }
    }

}
