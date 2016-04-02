package com.marwanad.sampletext;

import android.media.AudioFormat;
import android.media.MediaRecorder;

/**
 * Created by marwanad on 2016-04-02.
 */
public class AudioInputRunnable implements Runnable
{
    int mSampleRate = AudioUtils.SAMPLE_RATE_HZ;
    int mAudioSource = MediaRecorder.AudioSource.VOICE_RECOGNITION;
    final int mChannelConfig = AudioFormat.CHANNEL_IN_MONO;
    final int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT;

    @Override
    public void run()
    {

    }
}
