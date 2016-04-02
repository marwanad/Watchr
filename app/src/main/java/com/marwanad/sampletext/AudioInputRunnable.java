package com.marwanad.sampletext;

import android.media.AudioRecord;
import android.media.MediaRecorder;

/**
 * Created by marwanad on 2016-04-02.
 */
public class AudioInputRunnable implements Runnable
{
    AudioRecord _audioRecorder;
    private int _sampleRate = AudioUtils.SAMPLE_RATE_HZ;
    private int _audioSource = MediaRecorder.AudioSource.MIC;
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

    @Override
    public void run()
    {

    }
}
