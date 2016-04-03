package com.marwanad.watchr;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.marwanad.watchr.ui.activity.EvacuateActivity;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;

import javax.inject.Inject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by marwanad on 2016-04-02.
 */
public class WatchrService extends Service implements AudioInputListener
{
    @Inject Socket _socket;
    @Inject GoogleApiClient _googleApiClient;
    private AudioInputRunnable _audioInput;

    protected Location _currentLocation;
    protected String _lastUpdateTime;

    private static final String TAG = "WatchrService";
    private static final String ALERT_NOISE_EVENT = "alert-noise";
    private static final String ALERT_EVACUATE_EVENT = "alert-evacuate";
    private static final String ALERT_LOCATION_EVENT = "alert-location";

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
        ((WatchrApplication) getApplication()).getSampleComponent().inject(this);
        _audioInput = new AudioInputRunnable(this);

        connectToSocketWithBindInfo();

        _audioInput.start();
        _googleApiClient.connect();

        final LocationRequest locationRequest = createLocationRequest();
        new Handler().postDelayed(new Runnable() {
            public void run()
            {
                startLocationUpdates(locationRequest);
            }
        }, 500);

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

        if (Double.valueOf(smoothedDBValue) > 77) {
            Log.d(TAG, "Emitting smoothed dB value to server : " + smoothedDBValue);
            _socket.emit(ALERT_NOISE_EVENT, getIpAddress(), _currentLocation.getLatitude(), _currentLocation.getLongitude(), smoothedDBValue);
        }
    }

    private String getIpAddress()
    {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
    }

    private void connectToSocketWithBindInfo()
    {
        _socket.on(ALERT_EVACUATE_EVENT, new Emitter.Listener() {
            @Override
            public void call(Object... args)
            {
                Log.d(TAG, "got alert evacuate event");
                sendNotification();
            }
        });
        _socket.connect();
    }

    private void sendNotification()
    {
        Intent i = new Intent(this, EvacuateActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
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

    private LocationRequest createLocationRequest()
    {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    public void startLocationUpdates(LocationRequest locationRequest)
    {
        LocationServices.FusedLocationApi.requestLocationUpdates(_googleApiClient, locationRequest, new LocationListener() {
            @Override
            public void onLocationChanged(Location location)
            {
                _currentLocation = location;
                _lastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                _socket.emit(ALERT_LOCATION_EVENT, getIpAddress(), _currentLocation.getLatitude(), _currentLocation.getLongitude());
                Log.d(TAG, "Lat: " + String.valueOf(_currentLocation.getLatitude()));
                Log.d(TAG, "Long: " + String.valueOf(_currentLocation.getLongitude()));
                Log.d(TAG, "Last Update: " + _lastUpdateTime);
            }
        });
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        stopRecording();
    }
}
