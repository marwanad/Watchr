package com.marwanad.watchr;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.socket.client.Manager;
import io.socket.client.Socket;

/**
 * Created by marwanad on 2016-04-02.
 */
@Module
public class WatchrModule
{
    private final Context _app;

    public WatchrModule(Context _app)
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
            Manager manager = new Manager(new URI("http://10.8.235.65:3000/"));
//            Manager manager = new Manager(new URI("http://10.9.244.80:3000"));
            socket = manager.socket("/android");
            socket.connect();
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
