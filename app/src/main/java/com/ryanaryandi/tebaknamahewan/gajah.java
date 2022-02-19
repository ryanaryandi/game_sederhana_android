package com.ryanaryandi.tebaknamahewan;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class gajah extends AppCompatActivity {
    ImageView gambar;
    Button cek,imah;
    EditText jawab;
    String jawaban;
    TextView skorgame,scoretampil;
    CountDownTimer timer;
    MediaPlayer aujam, aubenar, auberhenti;
    int point = 0;
    private boolean mIsBound = false;
    private MusicService mServ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_gajah);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //mengambil point
        point = getIntent().getIntExtra("PoinT", 0);

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

        gambar = (ImageView) findViewById(R.id.gambar1);
        cek = (Button) findViewById(R.id.buttonCek);
        imah = (Button) findViewById(R.id.home);
        jawab = (EditText) findViewById(R.id.jawab);
        skorgame = (TextView) findViewById(R.id.skor);
        scoretampil = (TextView)findViewById(R.id.score);

        //Memanggil file my_sound
        aujam = MediaPlayer.create(getApplicationContext(), R.raw.jam);
        aubenar = MediaPlayer.create(getApplicationContext(), R.raw.benar);
        auberhenti = MediaPlayer.create(getApplicationContext(),R.raw.salah);
        //Set volume audio agar berbunyi
        aujam.setVolume(100,100);
        //mengulang audio
        aujam.setLooping(true);
        //Memulai audio
        aujam.start();

        //tampil point
        String tampil = String.valueOf(point);
        scoretampil.setText("Score : " + tampil);


//mengambil gambar dari database
        Glide.with(gajah.this).load("https://firebasestorage.googleapis.com/v0/b/tebak-nama-hewan-6069a.appspot.com/o/gambar%2Fgajah.jpg?alt=media&token=35d1231d-5d3f-40f4-a575-d5b5dcb0fdd4")
                .into(gambar);

//membuat aksi pada jawaban
        cek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jawaban = jawab.getText().toString();
                if (jawaban.equals("gajah")) {
                    aubenar.setVolume(10,10);
                    aubenar.start();
                    //point
                    point = point + 10;

                    Toast.makeText(getApplicationContext(), "Jawaban benar", Toast.LENGTH_SHORT).show();
                    Intent pindah = new Intent(gajah.this, ular.class);
                    pindah.putExtra("PoinT", point);
                    startActivity(pindah);
                    timer.cancel();
                    aujam.stop();
                    finish();
                }else if (jawaban.equals("")) {
                    Toast.makeText(getApplicationContext(), "Jawaban tidak boleh kosong", Toast.LENGTH_SHORT).show();

                }else {
                    auberhenti.setVolume(10, 10);
                    auberhenti.start();
                    Toast.makeText(getApplicationContext(), "Jawaban salah", Toast.LENGTH_SHORT).show();
                    Intent pindah = new Intent(gajah.this, ular.class);
                    pindah.putExtra("PoinT", point);
                    startActivity(pindah);
                    timer.cancel();
                    aujam.stop();
                    finish();
                }
            }
        });

        //membuat counterdown
        timer =  new CountDownTimer(20000,1000){

            @Override
            public void onFinish() {
                TextView teks=(TextView) findViewById (R.id.skor);
                teks.setText("Field!!!");
                Toast.makeText(getApplicationContext(), "Waktu habis", Toast.LENGTH_SHORT).show();
                Intent pindah = new Intent(gajah.this, ular.class);
                pindah.putExtra("PoinT", point);
                startActivity(pindah);
                auberhenti.setVolume(10,10);
                auberhenti.start();
                aujam.stop();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                TextView teks=(TextView) findViewById (R.id.skor);
                teks.setText("Seconds Left: "+millisUntilFinished/1000);
            }

        }.start();


        //home
        imah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lanjut = new Intent(gajah.this, depan.class);
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
                aujam.stop();
                timer.cancel();
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
    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this,MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
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
        music.setClass(this,MusicService.class);
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
