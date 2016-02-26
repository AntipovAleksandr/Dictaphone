package com.example.dictaphone;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.os.EnvironmentCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.os.Handler;

import java.io.File;

public class DictaphoneActivity extends Activity  {

    private TextView statusText;
    private Button recordBtn, playBtn;
    private String text;

    public int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    public int channelConfigurationOut = AudioFormat.CHANNEL_OUT_MONO;
    public int channelConfigurationIn = AudioFormat.CHANNEL_IN_MONO;
    int frequency = 11025;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictaphone);

        statusText = (TextView) findViewById(R.id.statusText);
        recordBtn = (Button) findViewById(R.id.record);
        playBtn = (Button) findViewById(R.id.play);

//        String filePath = "/sdcard/voice8K16bitmono.pcm";
//
//        File file = new File(EnvironmentCompat.getStorageState(filePath),"raw.pcm");

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordThread();
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();

            }
        });
    }
    final Handler handler = new Handler();

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateText(text);

        }
    };


    private void updateText(String update_text) {
        statusText.setText(update_text);

    }

    private void recordThread() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                text = "Запись...";
                handler.post(runnable);
                record();
                text = "Готово!";
                handler.post(runnable);
            }
        });
        thread.start();
    }

    int bufferSize = 160* AudioTrack.getMinBufferSize(frequency,channelConfigurationOut,audioFormat);

    public AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency, channelConfigurationIn,
            audioFormat, bufferSize);

    public AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency, channelConfigurationOut,
            audioFormat,4096, AudioTrack.MODE_STREAM);

    short [] buffer = new short[bufferSize];



    public void record() {

        audioRecord.startRecording();
        audioRecord.read(buffer, 0, bufferSize);
        audioRecord.stop();
    }
    public void play(){
        audioTrack.play();
        audioTrack.write(buffer, 0 , bufferSize);

    }

}
