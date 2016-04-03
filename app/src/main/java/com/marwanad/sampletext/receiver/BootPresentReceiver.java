package com.marwanad.sampletext.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.marwanad.sampletext.WatchrService;

/**
 * Created by marwanad on 2016-04-02.
 */
public class BootPresentReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            context.startService(new Intent(context, WatchrService.class));
        }
    }
}
