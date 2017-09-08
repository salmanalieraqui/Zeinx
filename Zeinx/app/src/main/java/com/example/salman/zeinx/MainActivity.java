package com.example.salman.zeinx;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.jar.Manifest;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {

    private ArrayList<SongInfo> _songs = new ArrayList<SongInfo>();
    RecyclerView recyclerView;
    SeekBar seekBar;
    SongAdapter songAdapter;
    MediaPlayer mediaPlayer;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
  //      SongInfo s = new SongInfo("arijit sings", "link to websuite", "http://hd.djring.com/320/484709/Pyaar%20ka%20nagma%20-%20Arijit%20Singh%20ft%20AC%20Music%20(DJJOhAL.Com).mp3");
  //      _songs.add(s);
        songAdapter = new SongAdapter(this, _songs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(songAdapter);
        songAdapter.setOnitemClickListener(new SongAdapter.OnitemClickListener() {
            @Override
            public void onItemClick(final Button b, View v, SongInfo obj, int position) {
                try {
                    if (b.getText().toString().equals("Stop")) {
                        b.setText("Play");
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    } else {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(obj.getSongUrl());
                        mediaPlayer.prepareAsync();
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mediaPlayer.start();
                                seekBar.setProgress(0);
                                seekBar.setMax(mediaPlayer.getDuration());
                                b.setText("Stop");
                            }
                        });
                    }
                } catch (IOException e) {
                }
            }
        });
        CheckPermission();
        Thread t=new MyThread();
        t.start();


    }
    public class MyThread extends Thread{
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                if(mediaPlayer!=null) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void CheckPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
                return;
            }
        } else {
            loadSongs();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       switch(requestCode){
           case 123:
               if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                   loadSongs();
               }else{
                   Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
                   CheckPermission();


               }
               break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

       }

    }
    private void loadSongs(){
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection= MediaStore.Audio.Media.IS_MUSIC+"!=0";
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                do {
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    SongInfo s=new SongInfo(name,artist,url);
                    _songs.add(s);
                }while(cursor.moveToNext());
            }
            cursor.close();
            songAdapter=new SongAdapter(this,_songs);
        }
    }
}
