package com.marwanad.sampletext;

import javax.inject.Singleton;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.net.URISyntaxException;

import dagger.Module;
import dagger.Provides;
import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by marwanad on 2016-04-02.
 */
@Module
public class SampleModule
{
    private final Context _app;

    public SampleModule(Context _app)
    {
        this._app = _app;
    }

    @Provides
    @Singleton
    Context provideContext()
    {
        return _app;
    }

    // Provide web socket
    @Provides
    Socket provideWebSocket()
    {
        Socket socket;
        try {
            socket = IO.socket("http://10.9.244.80:4000");
        }
        catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return socket;
    }

    @Provides
    GoogleApiClient provideGoogleApiClient()
    {
        return new GoogleApiClient.Builder(_app)
                .addApi(LocationServices.API)
                .build();
    }

    @Provides
    SharedPreferences provideSharedPrefs()
    {
        return PreferenceManager.getDefaultSharedPreferences(_app);
    }
}
