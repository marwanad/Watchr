package com.marwanad.sampletext;

import android.media.AudioFormat;
import android.media.AudioRecord;

/**
 * Created by marwanad on 2016-04-01.
 */
public class AudioUtils
{
    public static final int SAMPLE_RATE_HZ = 8000;
    public static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    public static int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    public static int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE_HZ, CHANNEL_CONFIG, AUDIO_FORMAT);
}
