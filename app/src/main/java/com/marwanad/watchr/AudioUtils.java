package com.marwanad.watchr;

import android.media.AudioFormat;
import android.media.AudioRecord;

/**
 * Created by marwanad on 2016-04-01.
 */
public class AudioUtils
{
    private static final String TAG = "AudioUtils";
    public static final int SAMPLE_RATE_HZ = 8000;
    public static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    public static int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    public static int BUFFER_SIZE = getBufferSize(AudioUtils.SAMPLE_RATE_HZ, CHANNEL_CONFIG,
            AUDIO_FORMAT);

    public static int getBufferSize(int sampleRateInHz, int channelConfig, int audioFormat)
    {
        int bufferSize = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig,
                audioFormat);
        if (bufferSize < sampleRateInHz) {
            bufferSize = sampleRateInHz;
        }
        return bufferSize;
    }
}
