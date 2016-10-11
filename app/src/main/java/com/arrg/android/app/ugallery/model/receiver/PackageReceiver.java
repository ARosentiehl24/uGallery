package com.arrg.android.app.ugallery.model.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PackageReceiver extends BroadcastReceiver {

    public PackageReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String appPackage = intent.getData().getEncodedSchemeSpecificPart();

        switch (intent.getAction()) {
            case Intent.ACTION_PACKAGE_ADDED:
                break;
            case Intent.ACTION_PACKAGE_REMOVED:
                break;
        }
    }
}
