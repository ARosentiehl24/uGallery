package com.arrg.android.app.ugallery.interfaces;

import com.arrg.android.app.ugallery.model.entity.PhoneAlbum;
import com.arrg.android.app.ugallery.model.entity.PhoneMedia;
import com.arrg.android.app.ugallery.view.adapter.MediaSearchAdapter;

import java.util.ArrayList;

public interface SearchPresenter {
    void onCreate();

    MediaSearchAdapter makeQuery(CharSequence s, ArrayList<PhoneAlbum> phoneAlbumArrayList);
}
