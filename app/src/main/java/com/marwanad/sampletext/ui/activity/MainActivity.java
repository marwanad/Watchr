package com.marwanad.sampletext.ui.activity;

import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ProgressBar;

import com.marwanad.sampletext.AudioUtils;
import com.marwanad.sampletext.R;

import java.io.DataOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
{
    private AudioRecord _audioRecorder;
    private boolean _socketStatus = false;
    private int _bufferSize = AudioUtils.BUFFER_SIZE;
    short[] buffer;
    private boolean _isRecording;
    private Thread _audioThread;
    DataOutputStream output;
    @Bind(R.id.toolbar) Toolbar _toolbar;
    @Bind(R.id.progressBar) ProgressBar _progressBar;

    @OnClick(R.id.stop_fab)
    public void stopAudioRecord()
    {
        if (_audioRecorder != null) {
            _isRecording = false;
            _audioRecorder.stop();
            _audioThread = null;
            _audioRecorder.release();
            _audioRecorder = null;
            Log.d("SampleText", "Stopping audio recording");
        }
    }

    @OnClick(R.id.start_fab)
    public void launchAudioRecord()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(_toolbar);
    }
}
