package com.arrg.android.app.ugallery.interfaces;

import android.content.Context;

public interface GalleryPresenter {

    void onCreate();

    void onBackPressed();

    void onOptionsItemSelected(int itemId);

    void onCentreButtonClick(Context context);

    void onItemClick(int itemIndex, String itemName);
}
