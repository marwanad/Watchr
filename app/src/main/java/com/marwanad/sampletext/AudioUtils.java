package com.marwanad.sampletext;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

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

    private static int[] _sampleRates = new int[] {8000, 11025, 22050, 44100};

    /**
     * Loop through all possible sample Rates because some devices don't support some of the rates/buffer sizes (from SO)
     * @param audioSource
     * @param audioFormat
     * @return
     */
    public static AudioRecord getAudioRecord(int audioSource, int audioFormat)
    {
        for (int r : _sampleRates) {
            for (short audioformat : new short[] {AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT}) {
                for (short channelConfig : new short[] {AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO}) {
                    try {
                        int bufferSize = AudioRecord.getMinBufferSize(r, channelConfig, audioFormat);

                        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                            // check if we can instantiate and have a success
                            AudioRecord recorder = new AudioRecord(audioSource, r, channelConfig, audioFormat, bufferSize);

                            if (recorder.getState() == AudioRecord.STATE_INITIALIZED)
                                return recorder;
                        }
                    }
                    catch (Exception e) {
                    }
                }
            }
        }
        return null;
    }
}
