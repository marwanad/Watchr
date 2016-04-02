package com.marwanad.sampletext;

import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

/**
 * Created by marwanad on 2016-04-02.
 */
public class AudioInputRunnable implements Runnable
{
    private static final String TAG = "AudioInputRunnable";
    AudioRecord _audioRecorder;
    final int _channelConfig = AudioUtils.CHANNEL_CONFIG;
    final int _audioFormat = AudioUtils.AUDIO_FORMAT;

    int _totalNumberOfSamples = 0;

    private final AudioInputListener _audioListener;
    private Thread _audioInputThread;
    private boolean _isRunning;

    public AudioInputRunnable(AudioInputListener listener)
    {
        _audioListener = listener;
    }

    public void start()
    {
        if (!_isRunning) {
            _isRunning = true;
            _audioInputThread = new Thread(this);
            _audioInputThread.start();
        }
    }

    public void stop()
    {
        try {
            if (_isRunning) {
                _isRunning = false;
                _audioRecorder.stop();
                _audioRecorder.release();
                _audioInputThread.join();
            }
        }
        catch (InterruptedException e) {
            Log.e(TAG, "InterruptedException.", e);
        }
    }

    @Override
    public void run()
    {
        // uses a 20 ms buffer for processing the audio sample, finding the rms, etc
        short[] twentyMsBuffer = new short[AudioUtils.SAMPLE_RATE_HZ / 50];

        try {
            _audioRecorder = new AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    AudioUtils.SAMPLE_RATE_HZ,
                    _channelConfig,
                    _audioFormat,
                    AudioUtils.BUFFER_SIZE);
            _audioRecorder.startRecording();

            while (_isRunning) {
                int numSamples = _audioRecorder.read(twentyMsBuffer, 0, twentyMsBuffer.length);
                _totalNumberOfSamples += numSamples;
                _audioListener.processSampleFrames(twentyMsBuffer);
            }
            _audioRecorder.stop();
        }
        catch (Throwable x) {
        }
    }
}
