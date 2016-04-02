package com.marwanad.sampletext;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.util.Log;

import java.text.DecimalFormat;

import javax.inject.Inject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by marwanad on 2016-04-02.
 */
public class SampleService extends Service implements AudioInputListener
{
    @Inject Socket _socket;

    private AudioInputRunnable _audioInput;

    private static final String TAG = "SampleService";
    double _gain = 2500.0 / Math.pow(10.0, 90.0 / 20.0);
    double _smoothRMS;
    double _alpha = 0.9;

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        ((SampleApplication) getApplication()).getSampleComponent().inject(this);
        _audioInput = new AudioInputRunnable(this);

        connectToSocketWithBindInfo(getIpAddress());

        _audioInput.start();

        return START_STICKY;
    }

    /**
     * Processes audio frames within the last 20ms, finds rms, does some filtering
     * @param sampleFrame
     */
    @Override
    public void processSampleFrames(short[] sampleFrame)
    {
        // Find rms value
        double rms = 0;
        for (int i = 0; i < sampleFrame.length; i++) {
            rms += sampleFrame[i] * sampleFrame[i];
        }
        rms = Math.sqrt(rms / sampleFrame.length);
        _smoothRMS = _smoothRMS * _alpha + (1 - _alpha) * rms;
        final double rmsdB = 20.0 * Math.log10(_gain * _smoothRMS);

        final DecimalFormat df = new DecimalFormat("##");

        String dBText = df.format(20 + rmsdB);
        int dBFraction = (int) (Math.round(Math.abs(rmsdB * 10))) % 10;
        String smoothedDBValue = dBText + "." + dBFraction;

        if (Double.valueOf(smoothedDBValue) > 66) {
            Log.d(TAG, "Emitting smoothed dB value to server : " + smoothedDBValue);
            _socket.emit("msg", smoothedDBValue);
        }
    }

    private String getIpAddress()
    {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
    }

    private void connectToSocketWithBindInfo(final String ip)
    {
        _socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args)
            {
                Log.d(TAG, "Connecting to socket with ip address:" + ip);
                _socket.emit("msg", ip);
            }

        });
        _socket.connect();
    }

    private void stopRecording()
    {
        if (_audioInput != null) {
            _audioInput.stop();
        }
        if (_socket != null) {
            _socket.disconnect();
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        stopRecording();
    }
}
